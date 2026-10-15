# WoHinDamit – Phase 1 Dokumentationspaket

**Stand:** 10. Oktober 2026
**Geplanter Zeitraum für Phase 1 und 2:** 01.10.2026–31.10.2026
**Zweck:** Analyse und Basisdesign dokumentieren, bevor weitere größere Backend-Funktionen implementiert werden.

## Dateien

| Datei | Inhalt |
|---|---|
| `docs/requirements.md` | Fachliche und technische Backend-Anforderungen |
| `docs/architecture.md` | Systemarchitektur und Datenfluss |
| `docs/database.md` | Vorgeschlagenes logisches Datenmodell, Beziehungen und Designentscheidungen |
| `docs/er-diagram.mmd` | ER-Diagramm als Mermaid-Quelle |
| `docs/decision-logic.md` | Eingaben, Regelbewertung, Prioritäten und Entscheidungsfluss |
| `docs/api.md` | Aktuelle API-Prototyp-Endpunkte und geplante Ergänzungen |
| `docs/external-data.md` | Recherche zu Wiener Standortdaten, Formaten, Lizenz und Datenlücken |
| `docs/testing.md` | Teststrategie und bereits beobachteter Stand |
| `docs/errors.md` | Einheitliche Fehlerbehandlung und HTTP-Statuscodes |
| `docs/phase-1-checklist.md` | Phase-1-Ergebnisse, offene Entscheidungen und Abnahmekriterien |

## Status richtig interpretieren

Ein Spring-Boot-/PostgreSQL-Prototyp mit mehreren REST-Endpunkten und Tests existiert bereits. Das ist ein vorgezogener Implementierungsstand; es ersetzt nicht die formale Prüfung und Freigabe des Designs. Die Dokumente halten den **aktuellen Prototyp** und den **vorgeschlagenen Zielzustand** auseinander.

## Vorgehensweise

1. Den Ordner `docs/` in das bestehende Repository kopieren bzw. die entsprechenden Dateien mit vorhandenen Dokumenten zusammenführen.
2. Offene Entscheidungen in `phase-1-checklist.md` mit dem Team (insbesondere Mohamad) abstimmen.
3. Erst nach Review das Datenmodell und den API-Vertrag als freigegeben markieren.

## Local API preview

See [local-preview.md](local-preview.md) for startup commands, optional demo data and verified scenarios.
