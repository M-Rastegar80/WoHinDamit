WoHinDamit? – API Design
1. Ziel

Das Backend stellt dokumentierte HTTP-Schnittstellen bereit, über die das Frontend Daten abrufen und Empfehlungen anfordern kann.

Die API bildet die Verbindung zwischen Frontend, Backend, Datenbank und Entscheidungslogik.

2. Grundsätze

Die API soll:

klar strukturiert sein

validierte Eingaben verwenden

eindeutige Antworten liefern

Fehler strukturiert zurückgeben

nur notwendige Daten an das Frontend übertragen

unabhängig von der internen Datenbankstruktur bleiben

3. API-Basis

Geplante Basis:

/api


Beispielsweise:

/api/categories
/api/items
/api/questions
/api/recommendations
/api/locations


Die endgültige Versionierung kann später ergänzt werden, zum Beispiel:

/api/v1

4. Categories
GET /api/categories

Liefert die verfügbaren Kategorien.

Response
{
  "categories": [
    {
      "id": 1,
      "name": "Elektrogeräte",
      "description": "Elektrische und elektronische Geräte"
    },
    {
      "id": 2,
      "name": "Möbel",
      "description": "Möbel und Einrichtungsgegenstände"
    }
  ]
}

5. Items
GET /api/items

Liefert Gegenstände.

Optional kann nach einem Suchbegriff gefiltert werden.

Beispiel
GET /api/items?search=fahrrad

Response
{
  "items": [
    {
      "id": 10,
      "name": "Fahrrad",
      "category_id": 4
    }
  ]
}

GET /api/items/{id}

Liefert Informationen zu einem bestimmten Gegenstand.

Beispiel
GET /api/items/10

Response
{
  "id": 10,
  "name": "Fahrrad",
  "category_id": 4,
  "description": "Fahrräder und ähnliche Fahrzeuge"
}

6. Questions
GET /api/items/{id}/questions

Liefert die für einen Gegenstand relevanten Fragen.

Beispiel
GET /api/items/10/questions

Response
{
  "questions": [
    {
      "id": 1,
      "text": "Ist der Gegenstand noch funktionsfähig?",
      "answer_type": "boolean"
    },
    {
      "id": 2,
      "text": "Ist der Gegenstand reparierbar?",
      "answer_type": "boolean"
    }
  ]
}

7. Recommendation
POST /api/recommendations

Ermittelt anhand des Gegenstands und der Antworten eine Empfehlung.

Request
{
  "item_id": 10,
  "answers": [
    {
      "question_id": 1,
      "value": false
    },
    {
      "question_id": 2,
      "value": true
    }
  ]
}

Response
{
  "recommendation": {
    "action": "REPAIR",
    "title": "Reparieren",
    "reason": "Der Gegenstand ist reparierbar.",
    "priority": 1
  }
}

8. Locations
GET /api/locations

Liefert passende Standorte.

Mögliche Filter:

type
category
latitude
longitude
radius

Beispiel
GET /api/locations?type=REPAIR_WORKSHOP&category=Fahrräder

Response
{
  "locations": [
    {
      "id": 101,
      "name": "Beispiel Repair Werkstatt",
      "type": "REPAIR_WORKSHOP",
      "address": "Beispielstraße 10, 1160 Wien",
      "latitude": 48.214,
      "longitude": 16.315,
      "phone": "+43 1 123456",
      "website": "https://example.at"
    }
  ]
}

9. Search
GET /api/search

Die Suchfunktion ermöglicht die Suche nach Gegenständen.

Beispiel
GET /api/search?q=fahrrad

Response
{
  "results": [
    {
      "id": 10,
      "name": "Fahrrad",
      "category": "Fahrräder"
    }
  ]
}

10. Fehlerbehandlung

Die API verwendet geeignete HTTP-Statuscodes.

Status	Bedeutung
200	Anfrage erfolgreich
201	Ressource erfolgreich erstellt
400	Ungültige Anfrage
404	Ressource nicht gefunden
422	Eingabe kann nicht verarbeitet werden
500	Interner Serverfehler
503	Externe Datenquelle nicht verfügbar
11. Fehlerformat

Fehler sollen möglichst einheitlich zurückgegeben werden.

Beispiel:

{
  "error": {
    "code": "ITEM_NOT_FOUND",
    "message": "Der angegebene Gegenstand wurde nicht gefunden."
  }
}


Interne technische Informationen sollen nicht an Benutzer*innen zurückgegeben werden.

12. Validierung

Das Backend validiert alle Eingaben.

Beispiele:

item_id muss existieren

question_id muss existieren

Antwortwerte müssen zum Fragetyp passen

Koordinaten müssen gültige Werte besitzen

Radius darf keine ungültigen Werte enthalten

13. API und Datenbank

Das Frontend greift niemals direkt auf die Datenbank zu.

Der Datenfluss lautet:

Frontend
   |
   | HTTP Request
   v
Backend API
   |
   v
Business Logic
   |
   v
Database

14. API und externe Daten

Externe Datenquellen werden ebenfalls über das Backend verarbeitet.

External Data Source
        |
        v
Backend
        |
        v
Validation / Normalization
        |
        v
Database
        |
        v
API
        |
        v
Frontend


Das Frontend benötigt keinen direkten Zugriff auf externe Datenquellen.

15. Zukünftige Erweiterungen

Falls optionale Funktionen umgesetzt werden, können zusätzliche Endpunkte ergänzt werden.

Beispiele:

POST /api/posts
GET /api/posts
GET /api/posts/{id}

POST /api/favorites
DELETE /api/favorites/{id}

GET /api/opening-hours


Diese Endpunkte gehören nicht zum verpflichtenden Kernumfang.