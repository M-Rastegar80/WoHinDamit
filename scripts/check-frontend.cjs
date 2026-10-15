const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');
class Element {
  constructor(tag = 'div') { this.tag = tag; this.value = ''; this.children = []; this.handlers = {}; this.dataset = {}; }
  append(...nodes) { this.children.push(...nodes); }
  replaceChildren(...nodes) { this.children = nodes; }
  setAttribute() {}
  addEventListener(name, fn) { this.handlers[name] = fn; }
  requestSubmit() { this.pending = this.handlers.submit({ preventDefault() {} }); }
}
async function main() {
  const ids = Object.fromEntries(['connection', 'category', 'questions', 'submit', 'result', 'decision', 'status'].map(id => [id, new Element()]));
  let fail = false;
  let payload;
  const context = {
    document: { getElementById: id => ids[id], createElement: tag => new Element(tag) },
    window: { location: { origin: 'http://localhost:8080' } },
    URL, AbortController, setTimeout, clearTimeout,
    fetch: async (url, options = {}) => {
      if (url.endsWith('/api/categories')) return response([{ id: 7, name: 'Elektronik' }]);
      if (url.endsWith('/questions')) return response([
        { id: 1, code: 'functional', questionText: 'Funktioniert?', answerType: 'BOOLEAN', required: true, orderIndex: 1 },
        { id: 2, code: 'repairable', questionText: 'Reparierbar?', answerType: 'BOOLEAN', required: true, orderIndex: 2 }
      ]);
      if (url.endsWith('/api/decisions')) {
        payload = JSON.parse(options.body);
        return fail ? response(null, 404) : response({ action: 'REPAIR', title: 'Reparatur prüfen', description: 'Test', reason: 'Reparierbar' });
      }
      throw new Error('Unexpected API request: ' + url);
    }
  };
  function response(body, status = 200) { return { ok: status === 200, status, json: async () => body }; }
  vm.createContext(context);
  vm.runInContext(fs.readFileSync(require('node:path').join(__dirname, '../backend/src/main/resources/static/script.js'), 'utf8'), context);
  await ids.connection.pending;
  assert.equal(ids.category.children[1].textContent, 'Elektronik', 'automatic category loading');
  ids.category.value = '7';
  await ids.category.handlers.change();
  assert.equal(ids.questions.children.length, 2, 'questions loaded from API');
  ids.questions.children[0].children[1].value = 'false';
  ids.questions.children[1].children[1].value = 'true';
  await ids.decision.handlers.submit({ preventDefault() {} });
  assert.deepEqual(payload, { categoryId: 7, answers: { functional: false, repairable: true } });
  assert.equal(ids.result.hidden, false);
  assert.equal(ids.result.children[0].textContent, 'Reparatur prüfen');
  fail = true;
  await ids.decision.handlers.submit({ preventDefault() {} });
  assert.equal(ids.result.hidden, true, 'old result cleared on failure');
  assert.match(ids.status.textContent, /Keine passende Empfehlung/);
  assert.equal(ids.submit.disabled, false, 'retry available');
  console.log('PASS: automatic categories, dynamic questions, typed answers, API recommendation, no-match and retry.');
}
main().catch(error => { console.error(error); process.exitCode = 1; });
