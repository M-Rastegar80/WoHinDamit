# WoHinDamit – API Design

**Status:** API-Prototyp implementiert; geplanter API-Umfang dokumentiert.
**Projektphase:** Phase 1 – Analyse und Basisdesign
**Letzte Aktualisierung:** Oktober 2026

## 1. Ziel

Das Backend stellt HTTP-Schnittstellen bereit, über die das Frontend Kategorien, Fragen und Antwortoptionen abrufen und eine Empfehlung für einen Gegenstand anfordern kann.

Die API verbindet das Frontend mit der Business-Logik, der Datenbank und später den externen Standortdatenquellen.

Dieses Dokument unterscheidet zwischen:

- **Implementiert:** im aktuellen Spring-Boot-Prototyp vorhanden.
- **Geplant:** für den weiteren Projektumfang vorgesehen, aber noch nicht als implementiert zu betrachten.
- **Offen:** muss vor der endgültigen Abstimmung mit dem Frontend-Team geklärt werden.

## 2. API-Grundsätze

Die API soll:

- klar strukturierte und nachvollziehbare Endpunkte anbieten;
- Eingaben serverseitig validieren;
- konsistente HTTP-Statuscodes verwenden;
- Fehler in einem dokumentierten Format zurückgeben;
- DTOs anstelle von JPA-Entities als API-Vertrag verwenden;
- nur die für das Frontend notwendigen Daten übertragen;
- unabhängig von internen Datenbankstrukturen bleiben;
- externe Datenquellen ausschließlich über das Backend verarbeiten.

Das Frontend greift niemals direkt auf die Datenbank zu.

## 3. Basis und Datenformat

**Aktuelle API-Basis:**

`/api`

Die API verwendet JSON für Requests und Responses.

Bei Requests mit JSON-Body ist folgender Header zu setzen:

`Content-Type: application/json`

Eine Versionierung, beispielsweise `/api/v1`, kann später ergänzt werden. Die Entscheidung dazu ist noch offen.

## 4. Aktuell implementierte Endpunkte

### 4.1 Kategorien abrufen

**Methode:** `GET`
**Route:** `/api/categories`

Liefert die aktiven Kategorien.

**Erfolgsantwort: `200 OK`**

```json
[
  {
    "id": 2,
    "name": "API Test Category",
    "description": "Temporary API test data"
  }
]
```

Die gezeigten Werte sind Testdaten aus dem aktuellen Prototyp und keine endgültigen fachlichen Beispieldaten.

### 4.2 Fragen einer Kategorie abrufen

**Methode:** `GET`
**Route:** `/api/categories/{categoryId}/questions`

Liefert die aktiven Fragen einer Kategorie, sortiert nach `orderIndex`.

**Beispiel:**

`GET /api/categories/2/questions`

**Erfolgsantwort: `200 OK`**

```json
[
  {
    "id": 1,
    "code": "repairable",
    "questionText": "Ist der Gegenstand reparierbar?",
    "answerType": "BOOLEAN",
    "orderIndex": 1,
    "required": true
  }
]
```

Wenn keine aktiven Fragen vorhanden sind, wird ein leeres JSON-Array `[]` zurückgegeben.

### 4.3 Antwortoptionen einer Frage abrufen

**Methode:** `GET`
**Route:** `/api/questions/{questionId}/answer-options`

Liefert die Antwortoptionen einer existierenden und aktiven Frage, sortiert nach `orderIndex`.

**Beispiel:**

`GET /api/questions/1/answer-options`

**Erfolgsantwort: `200 OK`**

```json
[
  {
    "id": 1,
    "value": "true",
    "label": "Ja",
    "orderIndex": 1
  },
  {
    "id": 2,
    "value": "false",
    "label": "Nein",
    "orderIndex": 2
  }
]
```

Wenn die Frage existiert und aktiv ist, aber noch keine Optionen besitzt, wird `200 OK` mit `[]` zurückgegeben. Eine nicht existierende oder inaktive Frage führt zu `404 Not Found`.

Die Antwortoptionen `Ja` und `Nein` sind aktuelle Testdaten.

### 4.4 Entscheidung auswerten

**Methode:** `POST`
**Route:** `/api/decisions`

Validiert die Antworten, lädt aktive Regeln, prüft deren Bedingungen und wählt die passende Regel mit der höchsten numerischen Priorität. Aus dieser Regel wird die zugehörige Empfehlung erstellt.

#### Request

```json
{
  "categoryId": 2,
  "itemId": 2,
  "answers": {
    "repairable": true,
    "functional": false
  }
}
```

**Feldbeschreibung:**

| Feld | Typ | Bedeutung |
|---|---|---|
| `categoryId` | Long | Pflichtfeld; ID der Kategorie |
| `itemId` | Long oder `null` | Optional; für Regeln mit Gegenstandsbezug erforderlich |
| `answers` | Objekt | Zuordnung von Fragecodes zu Antwortwerten |

Die Schlüssel in `answers` entsprechen den stabilen Fragecodes aus der Datenbank. Die Werte müssen zum jeweiligen `answerType` passen.

#### Erfolgsantwort: `200 OK`

```json
{
  "recommendationId": 1,
  "action": "REPAIR",
  "title": "Repair item",
  "description": "Test recommendation",
  "reason": "The item can be repaired"
}
```

Die dargestellten Inhalte stammen aus Testdaten.

Die aktuelle Response enthält `recommendationId`, `action`, `title`, `description` und `reason`. Eine `priority` wird derzeit nicht zurückgegeben.

#### Aktuelle Validierung

Der Prototyp prüft unter anderem:

- `categoryId` muss vorhanden sein.
- Alle übermittelten Fragecodes müssen zu aktiven Fragen dieser Kategorie gehören.
- Pflichtfragen müssen beantwortet werden.
- Antwortwerte müssen zum Fragetyp passen.
- Bei Auswahlfragen muss der ausgewählte Wert in den konfigurierten Antwortoptionen existieren.
- Es dürfen nur aktive Regeln berücksichtigt werden.
- Die Regel muss zur Kategorie und gegebenenfalls zum Gegenstand passen.

#### Keine passende Regel

Wenn keine Regel alle Bedingungen erfüllt, liefert der aktuelle Prototyp `404 Not Found` ohne Response-Body.

Ob dieser Fall künftig besser durch `422 Unprocessable Content` oder durch eine explizite Antwort wie `200 OK` mit `NO_MATCH` dargestellt werden soll, ist noch mit dem Frontend-Team abzustimmen.

## 5. HTTP-Statuscodes und Fehlerbehandlung

Die aktuell beobachteten Antworten sind:

| Status | Bedeutung |
|---|---|
| `200 OK` | Anfrage erfolgreich |
| `400 Bad Request` | Ungültige Eingabe oder fehlende Pflichtantwort |
| `404 Not Found` | Keine passende Regel oder angeforderte Ressource nicht gefunden |
| `500 Internal Server Error` | Nicht erwarteter interner Fehler |

Der aktuelle `GlobalExceptionHandler` übersetzt `IllegalArgumentException` in `400 Bad Request`.

**Aktuelles Fehlerformat:**

```json
{
  "error": "Answer required for question: functional"
}
```

Ein unbekannter Fragecode führt beispielsweise zu:

```json
{
  "error": "Unknown answer code: unknownQuestion"
}
```

Ein ungültiger Boolean-Wert führt beispielsweise zu:

```json
{
  "error": "Answer for question 'repairable' must be true or false."
}
```

Interne technische Informationen wie Stacktraces oder Datenbankdetails dürfen nicht an Benutzerinnen und Benutzer zurückgegeben werden.

Ein einheitliches Fehlerobjekt mit `code` und `message` ist als Verbesserung vorgesehen, aber noch nicht als aktueller API-Vertrag implementiert.

## 6. Geplante Endpunkte

Die folgenden Endpunkte gehören zum geplanten Umfang. Sie dürfen nicht als bereits implementiert betrachtet werden.

| Methode | Geplante Route | Zweck |
|---|---|---|
| `GET` | `/api/categories/{categoryId}` | Details einer Kategorie abrufen |
| `GET` | `/api/items/search?q={query}` | Gegenstände anhand eines Suchbegriffs suchen |
| `GET` | `/api/items/{itemId}` | Details eines Gegenstands abrufen |
| `GET` | `/api/locations` | Standorte nach Typ, Kategorie, Bezirk oder Umkreis filtern |
| `GET` | `/api/locations/{locationId}` | Details eines Standorts abrufen |

Vor der Implementierung sind Filter, Rückgabeformate, erforderliche Query-Parameter und das Verhalten bei fehlenden Ressourcen festzulegen.

## 7. Geplante Standort-API

Die Standort-API soll passende Annahmestellen, Reparaturwerkstätten und weitere geeignete Standorte liefern.

Als mögliche Filter sind vorgesehen:

- `type`
- `category`
- `district`
- `latitude`
- `longitude`
- `radius`

Ein mögliches Beispiel für eine spätere Anfrage ist:

`GET /api/locations?type=REPAIR_WORKSHOP&latitude=48.20&longitude=16.37&radius=5000`

Diese Route ist nur ein Entwurf. Die endgültigen Parameter, Einheiten und Filterregeln müssen festgelegt werden.

Ein Standort soll – soweit die Datenquelle diese Informationen liefert – folgende Informationen enthalten können:

- `id`
- `name`
- `type`
- `address`
- `postalCode`
- `latitude`
- `longitude`
- `phone`
- `email`
- `website`
- `openingHours`
- `source`
- `lastUpdated`

Nicht verfügbare Angaben werden als `null` oder gemäß einer später festgelegten API-Konvention ausgelassen. Fehlende Daten dürfen nicht erfunden werden.

## 8. Externe Datenquellen

Externe Daten werden ausschließlich vom Backend verarbeitet.

Der geplante Datenfluss lautet:

```text
External Data Source
        |
        v
Backend Import
        |
        v
Validation and Normalization
        |
        v
Database
        |
        v
Location API
        |
        v
Frontend
```

Mögliche Datenquellen sind MA 48, Mistplätze, Altstoffsammelstellen, Problemstoffsammelstellen, Reparaturwerkstätten, Repair-Cafés und Weitergabestellen.

Vor der Implementierung müssen für jede Quelle Format, verfügbare Felder, Nutzungsbedingungen, Aktualisierungsrhythmus, Koordinaten, Kontaktdaten und bekannte Datenlücken dokumentiert werden.

Ein Import-Endpunkt wird nicht ohne weitere Prüfung als öffentliche, anonyme API bereitgestellt. Ob der Import intern, manuell oder über einen geplanten Job erfolgt, ist noch zu entscheiden.

## 9. API und Datenbank

Der vorgesehene Datenfluss für normale API-Anfragen lautet:

```text
Frontend
   |
   | HTTP Request
   v
Controller / API
   |
   v
Service / Business Logic
   |
   v
Repository
   |
   v
Database
   |
   v
DTO / JSON Response
   |
   v
Frontend
```

JPA-Entities bleiben interne Datenmodelle. Das Frontend verwendet die dokumentierten Request- und Response-DTOs und ist nicht von der internen Datenbankstruktur abhängig.

## 10. Validierung

Die Validierung findet im Backend statt.

Mindestens folgende Prüfungen sind erforderlich:

- Pflichtfelder vorhanden;
- Kategorie, Gegenstand und Frage existieren, sofern referenziert;
- Fragecodes gehören zur ausgewählten Kategorie;
- Pflichtfragen sind beantwortet;
- Antwortwerte entsprechen dem jeweiligen Fragetyp;
- Auswahlwerte sind in den konfigurierten Optionen vorhanden;
- Koordinaten und Radius sind bei der Standortsuche gültig;
- optionale Felder werden konsistent behandelt.

Die aktuelle Umsetzung deckt einen Teil der Validierung für den Entscheidungs-Endpunkt ab. Die vollständige Validierung der zukünftigen Item- und Location-Endpunkte folgt bei deren Implementierung.

## 11. Testing

Die API wird durch Unit-Tests, Controller-Tests und manuelle HTTP-Tests geprüft.

Die bereits durchgeführten Tests umfassen unter anderem:

- aktive Kategorien abrufen;
- Fragen einer Kategorie abrufen;
- Antwortoptionen einer aktiven Frage abrufen;
- eine passende Regel auswerten;
- keine passende Regel finden;
- fehlende Pflichtantworten ablehnen;
- unbekannte Fragecodes ablehnen;
- ungültige Boolean-Werte ablehnen;
- nicht konfigurierte Auswahlwerte ablehnen;
- Regeln nach Kategorie, Gegenstand und Priorität prüfen.

Für zukünftige Endpunkte sind ebenfalls erfolgreiche und fehlerhafte Fälle zu definieren.

## 12. Offene Entscheidungen

Vor der endgültigen Abstimmung mit Mohamad und dem Frontend-Team sind folgende Punkte zu klären:

1. **Name des Entscheidung-Endpunkts:** Der Prototyp verwendet `POST /api/decisions`. Im ursprünglichen Entwurf stand `POST /api/recommendations`. Ein kanonischer Name muss vereinbart werden.
2. **Keine passende Regel:** Soll dieser Fall `404`, `422` oder eine explizite `NO_MATCH`-Antwort liefern?
3. **Priorität in der Response:** Soll `priority` Teil der öffentlichen Empfehlung sein?
4. **Pflichtfeld `itemId`:** Wann ist der Gegenstand verpflichtend und wann genügt eine Kategorie?
5. **Fragenmodell:** Werden Fragen ausschließlich Kategorien oder auch einzelnen Gegenständen zugeordnet?
6. **Standortsuche:** Welche Filter, Radius-Einheiten und Sortierung werden benötigt?
7. **Fehlerformat:** Soll ein einheitliches Format mit `code` und `message` eingeführt werden?
8. **API-Versionierung:** Ist `/api` ausreichend oder soll vor dem Frontend-Vertrag `/api/v1` festgelegt werden?

## 13. Nicht verpflichtende Erweiterungen

Mögliche spätere Erweiterungen sind:

- Favoriten;
- Beiträge oder Weitergabe-Angebote;
- Öffnungszeiten als gesonderte Ressource;
- Caching für häufig abgefragte Standortdaten.

Diese Funktionen gehören nicht zum verpflichtenden API-Kern, solange sie nicht im Projektumfang bestätigt wurden.

## 14. Zusammenfassung

Der aktuelle Prototyp stellt Endpunkte zum Abrufen von Kategorien, Fragen und Antwortoptionen sowie zum Auswerten von Entscheidungen bereit.

Die Standort-API, die Gegenstandssuche und weitere Detail-Endpunkte gehören zum geplanten Umfang und müssen noch implementiert und getestet werden.

Das API-Design wird während Phase 1 mit den Anforderungen, dem Datenmodell, der Entscheidungslogik und dem Frontend-Vertrag abgestimmt. Erst nach Klärung der offenen Entscheidungen gilt der Vertrag als für die Integration freigegeben.
