# Teststrategie

**Status:** Testkonzept für Phase 1; die Implementierung kann schrittweise wachsen.

## 1. Bereits beobachteter Stand

Der zuletzt gemeldete vollständige Maven-Lauf zeigte **21 Tests, 0 Failures, 0 Errors**. Dieser Stand umfasst unter anderem DecisionService-Tests sowie Controller-Tests für Kategorien und Fragen. Nach späteren Änderungen an der DecisionService-/AnswerOption-Validierung ist vor der nächsten Freigabe erneut `mvnw.cmd clean test` auszuführen und die aktuelle Zahl im Projekt zu dokumentieren.

## 2. Testebenen

| Ebene | Beispiele |
|---|---|
| Unit – DecisionService | gültige Regel, keine passende Regel, höchste Priorität, falsche Kategorie/Item-Scope |
| Unit – Validierung | fehlende Pflichtantwort, unbekannter Frage-Code, ungültiger Boolean, nicht definierte Auswahloption |
| Controller – MockMvc | Erfolg, leere Liste, Frage nicht gefunden, ungültiger Request, JSON-Vertrag |
| Repository/DB-Integration | Verbindung, Insert, Update, Delete, sortierte Queries, Fremdschlüssel und Unique Constraints |
| API-Integration | echter Spring-Kontext mit Testdaten; Endpunkte und Fehlerstatus prüfen |
| Importtests | Koordinatenmapping, fehlende Felder, doppelte externe IDs, wiederholbarer Upsert, Lizenz-/Quellenmetadaten |
| Entscheidungs-Szenarien | Fahrrad reparierbar → REPAIR; nicht reparierbar, aber brauchbar → DONATE/REUSE; Elektrogerät nicht reparierbar → fachlich bestätigte Recycling-/Sammelstellenempfehlung |

## 3. Qualitätsregeln

- Tests müssen deterministisch sein und nicht auf manuell vorhandene Testdaten in einer lokalen Datenbank angewiesen sein.
- Testdaten getrennt von echten Daten halten.
- Bei Prioritätskonflikten explizit testen, welche Prioritätsrichtung gewinnt.
- Negative Tests sollen den Fehlerstatus und, wo stabil, den Fehlercode prüfen.
- Für externe Daten echte Netzaufrufe nicht in jeden Unit-Test einbauen; Importadapter mit Beispieldaten testen und den Dienst separat überwachen.

## 4. Mindestabnahmekriterien

- `mvnw.cmd clean test` erfolgreich.
- Keine unbeabsichtigten Duplikate bei Import.
- Validierungsfehler geben stabile 4xx-Antworten zurück.
- Decision-Szenarien sind mit Fachteam-Regeln und erwarteten Aktionen dokumentiert.
