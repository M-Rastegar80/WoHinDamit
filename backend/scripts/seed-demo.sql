BEGIN;
DO $$
DECLARE
  category_key bigint;
BEGIN
  PERFORM pg_advisory_xact_lock(7080802026);
  IF EXISTS (SELECT 1 FROM categories WHERE name = 'Elektronik (Test)') THEN
    RAISE NOTICE 'Test category already exists; no changes made.';
    RETURN;
  END IF;
  INSERT INTO categories(name, description, active)
  VALUES ('Elektronik (Test)', 'Lokale Beispieldaten zum Testen der Entscheidungsregeln.', true)
  RETURNING id INTO category_key;

  INSERT INTO questions(category_id, code, question_text, answer_type, order_index, required, active)
  VALUES
    (category_key, 'functional', 'Funktioniert das Geraet noch?', 'BOOLEAN', 1, true, true),
    (category_key, 'repairable', 'Kann das Geraet repariert werden?', 'BOOLEAN', 2, true, true);

  INSERT INTO recommendations(title, description, priority, active, action)
  VALUES
    ('Reparatur pruefen', 'Testempfehlung: Eine geeignete Reparaturstelle pruefen.', 30, true, 'REPAIR'),
    ('Weitergeben oder spenden', 'Testempfehlung: Ein funktionierendes Geraet weitergeben.', 20, true, 'DONATE'),
    ('Fachgerecht recyceln', 'Testempfehlung: Eine geeignete Sammelstelle fuer Elektrogeraete pruefen.', 10, true, 'RECYCLE')
  ON CONFLICT (action) DO NOTHING;

  INSERT INTO rules(category_id, recommendation_id, priority, conditions, reason, active)
  SELECT category_key, id, 300, '{"functional":false,"repairable":true}'::jsonb,
    'Testregel: Das Geraet ist defekt, aber reparierbar.', true
  FROM recommendations WHERE action = 'REPAIR';
  INSERT INTO rules(category_id, recommendation_id, priority, conditions, reason, active)
  SELECT category_key, id, 200, '{"functional":true}'::jsonb,
    'Testregel: Das Geraet funktioniert und kann weitergegeben werden.', true
  FROM recommendations WHERE action = 'DONATE';
  INSERT INTO rules(category_id, recommendation_id, priority, conditions, reason, active)
  SELECT category_key, id, 100, '{"functional":false,"repairable":false}'::jsonb,
    'Testregel: Das Geraet funktioniert nicht und ist nicht reparierbar.', true
  FROM recommendations WHERE action = 'RECYCLE';
END $$;
COMMIT;
