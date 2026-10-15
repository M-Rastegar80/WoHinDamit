# Phase 1 – Checkliste und offene Entscheidungen

**Zeitraum:** 01.10.2026–31.10.2026
**Ziel:** Anforderungen und Grunddesign vor weiterer größerer Implementierung abstimmen.

## 1. Dokumentierte Arbeitsergebnisse

- [x] Backend-Anforderungen und Frontend-Bedarf strukturiert.
- [x] Architekturübersicht und Datenfluss dokumentiert.
- [x] Logisches Datenmodell mit Kern-Entities und Beziehungen beschrieben.
- [x] ER-Diagramm als Mermaid-Datei vorbereitet.
- [x] Decision Flow, Regelstruktur, Priorität und No-Match-Fall dokumentiert.
- [x] Aktuelle Prototyp-API und geplante Endpunkte getrennt aufgelistet.
- [x] Offizielle Wiener Datenquellen vorrecherchiert; Lizenz, Formate und Datenlücken festgehalten.
- [x] Test- und Fehlerkonventionen vorgeschlagen.

## 2. Review vor formaler Abnahme

- [ ] Entity-Modell mit vorhandenem `database.md` und aktuellem Code abgleichen.
- [ ] Mit Mohamad API-Pfade und JSON-Verträge vereinbaren.
- [ ] Prioritätskonvention entscheiden. Aktueller Code: größere Zahl gewinnt; Beispielanforderungen verwenden teilweise `1` als höchste Priorität.
- [ ] JSON/JSONB für `Rule.conditions` gegen normalisierte `rule_conditions`-Tabelle abwägen.
- [ ] Entscheiden, ob die öffentliche Entscheidung den Pfad `/api/decisions` oder `/api/recommendations` verwendet.
- [ ] No-Match-Verhalten festlegen (`404` aktuell im Prototyp versus `422`/`NO_MATCH`-Body).
- [ ] Festlegen, wann `itemId` erforderlich ist.
- [ ] Standorttyp und Annahme-/Materialmodell finalisieren; ggf. `item_locations` durch ein Materialmodell ersetzen.
- [ ] Feldmapping und genauer Datenendpunkt für Altstoffsammelstellen prüfen.
- [ ] Dynamische Termine mobiler Problemstoffsammlung und Quellen für Repair-/Reuse-Angebote prüfen.
- [ ] ER-Modell und API-Vertrag im Team freigeben.

## 3. Empfohlene Abschlusskriterien für Phase 1

Phase 1 kann als **inhaltlich vorbereitet, aber noch reviewpflichtig** gelten. Formell abgeschlossen ist sie erst, wenn die offenen Architektur-/API-Entscheidungen dokumentiert und von den zuständigen Teammitgliedern bestätigt wurden.

## 4. Nächste Schritte nach Review

1. Datenmodell finalisieren und Migrationsstrategie auswählen.
2. API-Vertrag mit Frontend abstimmen.
3. Externe Datenformate praktisch prüfen und Testdateien sichern.
4. Danach priorisiert Phase 2/Implementation planen, ohne das Prototyping mit formaler Abnahme zu verwechseln.
