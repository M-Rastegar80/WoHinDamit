# Fehlerbehandlung

**Status:** API-Konvention zur Abstimmung

## 1. Einheitliches Fehlerformat

Vorschlag für die erste Version:

```json
{
  "error": "Answer required for question: functional"
}
```

Empfehlung für später: ein stabiles maschinenlesbares `code`-Feld ergänzen, damit das Frontend nicht vom englischen Meldungstext abhängig ist.

```json
{
  "code": "REQUIRED_ANSWER_MISSING",
  "message": "Answer required for question: functional"
}
```

## 2. Statuscode-Konvention

| Status | Verwendung |
|---|---|
| `200 OK` | Anfrage erfolgreich; auch leere Ergebnisliste, wenn die Ressource existiert |
| `400 Bad Request` | Fehlendes Pflichtfeld, unbekannter Frage-Code, falscher Antworttyp oder ungültige Option |
| `404 Not Found` | Angefragte Ressource nicht vorhanden; im aktuellen Prototyp auch kein passender Rule-Treffer |
| `422 Unprocessable Entity` | Alternative für fachlich valide JSON-Struktur, aber fachlich nicht ausführbare Entscheidung; nur verwenden, wenn Teamvertrag dies beschließt |
| `500 Internal Server Error` | Unerwarteter Fehler; keine internen Stacktraces oder Datenbankdetails an Clients senden |
| `503 Service Unavailable` | Optional für nicht verfügbare externe Quellen, wenn die Anfrage davon abhängt |

## 3. Zu behandelnde Fehlerfälle

- fehlende oder null `categoryId`
- Kategorie/Gegenstand nicht vorhanden oder inaktiv
- unbekannter/inaktiver Frage-Code
- fehlende Pflichtantwort
- Antwort passt nicht zu `answer_type`
- Option gehört nicht zu `answer_options` der Frage
- kein passender Rule-Treffer
- widersprüchliche Regeln mit gleicher Priorität
- fehlende/inkonsistente Recommendation an einer Regel
- externer Datenfeed nicht verfügbar, ungültiges Format oder Koordinatenfehler

## 4. Sicherheits- und Wartungshinweise

- Nur bekannte Eingabefehler als 4xx behandeln.
- Unerwartete Fehler mit serverseitigem Logging/Correlation-ID erfassen.
- Keine Passwörter, Verbindungszeichenfolgen oder Stacktraces an das Frontend senden.
- Fehlertexte benutzerfreundlich und separat von internen Logs halten.
