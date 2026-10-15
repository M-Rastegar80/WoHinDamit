"use strict";
const $ = id => document.getElementById(id);
let base = "", revision = 0, requestRevision = 0;
let questions = [];
const controls = new Map();
function status(message, error = false) { $("status").textContent = message; $("status").dataset.error = String(error); }
function clearResult() { $("result").replaceChildren(); $("result").hidden = true; }
function resetQuestions() { questions = []; controls.clear(); $("questions").replaceChildren(); $("submit").disabled = true; clearResult(); }
async function api(path, options = {}) {
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), 15000);
  try {
    const response = await fetch(base + path, { ...options, signal: controller.signal });
    if (!response.ok) {
      if (response.status === 404 && path === "/api/decisions") throw new Error("Keine passende Empfehlung gefunden.");
      const body = await response.json().catch(() => null);
      throw new Error(body?.error || body?.message || `Serverfehler (${response.status}).`);
    }
    return await response.json();
  } catch (error) {
    if (error.name === "AbortError") throw new Error("Der Server antwortet nicht rechtzeitig. Bitte erneut versuchen.");
    if (error instanceof TypeError) throw new Error("Server nicht erreichbar. Bitte Server-Adresse, Serverbetrieb und Browser-Zugriff (CORS) prüfen.");
    throw error;
  } finally { clearTimeout(timeout); }
}
function option(value, label) { const node = document.createElement("option"); node.value = value; node.textContent = label; return node; }
function select() { const node = document.createElement("select"); node.append(option("", "Bitte wählen")); return node; }
const booleanTypes = new Set(["BOOLEAN", "YES_NO", "TRUE_FALSE"]);
const numberTypes = new Set(["INTEGER", "NUMBER", "DECIMAL"]);
const singleTypes = new Set(["SINGLE_CHOICE", "SELECT"]);
const multiTypes = new Set(["MULTIPLE_CHOICE", "MULTI_SELECT"]);
async function renderQuestion(question, token) {
  const type = (question.answerType || "").trim().toUpperCase();
  const group = document.createElement("fieldset");
  const legend = document.createElement("legend"); legend.textContent = question.questionText + (question.required ? " *" : ""); group.append(legend);
  let input;
  if (booleanTypes.has(type)) { input = select(); input.append(option("true", "Ja"), option("false", "Nein")); }
  else if (numberTypes.has(type)) { input = document.createElement("input"); input.type = "number"; input.step = type === "INTEGER" ? "1" : "any"; }
  else if (["TEXT", "STRING"].includes(type)) { input = document.createElement("input"); input.type = "text"; }
  else if (singleTypes.has(type) || multiTypes.has(type)) {
    const options = await api(`/api/questions/${encodeURIComponent(question.id)}/answer-options`);
    if (token !== revision) return;
    if (!Array.isArray(options)) throw new Error("Ungültige Antwortoptionen vom Server.");
    if (singleTypes.has(type)) { input = select(); options.forEach(item => input.append(option(item.value, item.label))); }
    else {
      input = document.createElement("div");
      options.forEach(item => { const label = document.createElement("label"); const box = document.createElement("input"); box.type = "checkbox"; box.value = item.value; label.append(box, document.createTextNode(item.label)); input.append(label); });
    }
  } else throw new Error(`Nicht unterstützter Fragetyp: ${type || "unbekannt"}`);
  if (token !== revision) return;
  if (!multiTypes.has(type)) { input.required = Boolean(question.required); input.setAttribute("aria-label", question.questionText); }
  group.append(input); controls.set(question.code, { input, type }); $("questions").append(group);
}
$("connection").addEventListener("submit", async event => {
  event.preventDefault(); const token = ++revision; ++requestRevision; resetQuestions(); $("category").disabled = true;
  $("category").replaceChildren(option("", "Kategorien werden geladen…"));
  try {
    const url = new URL(window.location.origin);
    if (!["http:", "https:"].includes(url.protocol) || url.username || url.password || url.search || url.hash) throw new Error("Bitte eine HTTP/HTTPS-Server-Adresse ohne Zugangsdaten oder URL-Parameter eingeben.");
    base = url.href.replace(/\/$/, ""); status("Verbindung wird hergestellt…");
    const categories = await api("/api/categories"); if (token !== revision) return;
    if (!Array.isArray(categories)) throw new Error("Ungültige Kategorien vom Server.");
    $("category").replaceChildren(option("", "Bitte Kategorie wählen")); categories.forEach(item => $("category").append(option(item.id, item.name)));
    $("category").disabled = categories.length === 0; status(categories.length ? "Verbunden. Bitte Kategorie wählen." : "Verbunden, aber noch keine Kategorien vorhanden.");
  } catch (error) { if (token === revision) { $("category").replaceChildren(option("", "Zuerst mit dem Server verbinden")); $("category").disabled = true; status(error.message, true); } }
});
$("category").addEventListener("change", async () => {
  const token = ++revision; ++requestRevision; resetQuestions(); if (!$("category").value) { status("Bitte Kategorie wählen."); return; }
  status("Fragen werden geladen…");
  try {
    const data = await api(`/api/categories/${encodeURIComponent($("category").value)}/questions`); if (token !== revision) return;
    if (!Array.isArray(data)) throw new Error("Ungültige Fragen vom Server.");
    questions = [...data].sort((a,b) => (a.orderIndex || 0) - (b.orderIndex || 0));
    if (new Set(questions.map(q => q.code)).size !== questions.length || questions.some(q => !q.code)) throw new Error("Die Fragecodes sind ungültig oder nicht eindeutig.");
    for (const question of questions) { await renderQuestion(question, token); if (token !== revision) return; }
    $("submit").disabled = false; status(questions.length ? "Bitte Fragen beantworten. * Pflichtfrage" : "Für diese Kategorie sind keine Fragen hinterlegt.");
  } catch (error) { if (token === revision) { resetQuestions(); status(error.message, true); } }
});
$("decision").addEventListener("input", () => { ++requestRevision; clearResult(); if ($("category").value && controls.size === questions.length) $("submit").disabled = false; });
$("decision").addEventListener("submit", async event => {
  event.preventDefault(); const token = revision; const attempt = ++requestRevision; clearResult();
  try {
    const answers = Object.create(null);
    for (const question of questions) {
      const { input, type } = controls.get(question.code);
      if (multiTypes.has(type)) { const values = [...input.querySelectorAll("input:checked")].map(box => box.value); if (question.required && !values.length) throw new Error(`Bitte beantworten: ${question.questionText}`); if (values.length) answers[question.code] = values; }
      else if (input.value !== "") { answers[question.code] = booleanTypes.has(type) ? input.value === "true" : numberTypes.has(type) ? Number(input.value) : input.value; }
    }
    $("submit").disabled = true; status("Empfehlung wird geladen…");
    const result = await api("/api/decisions", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ categoryId: Number($("category").value), answers }) });
    if (token !== revision || attempt !== requestRevision) return;
    const actions = { REPAIR: "Reparieren", SELL: "Verkaufen", GIVE_AWAY: "Weitergeben", DONATE: "Spenden", RECYCLE: "Recyceln", DISPOSE: "Entsorgen" };
    const heading = document.createElement("h2"); heading.textContent = result.title || actions[result.action] || "Empfehlung"; $("result").append(heading);
    for (const text of [actions[result.action], result.description, result.reason]) { if (text) { const p = document.createElement("p"); p.textContent = text; $("result").append(p); } }
    $("result").hidden = false; status("Empfehlung erhalten.");
  } catch (error) { if (token === revision && attempt === requestRevision) status(error.message, true); }
  finally { if (token === revision && attempt === requestRevision) $("submit").disabled = false; }
});
status("Kategorien werden geladen…");
$("connection").requestSubmit();
