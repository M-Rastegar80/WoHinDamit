WoHinDamit? – System Architecture
1. Ziel

Die Anwendung wird als Client-Server-Webanwendung umgesetzt.

Die Architektur trennt die Benutzeroberfläche, die Geschäftslogik, die Datenbank und externe Datenquellen voneinander.

Dadurch soll das System übersichtlich, wartbar und erweiterbar bleiben.

2. Gesamtarchitektur
┌──────────────────────────────┐
│          Benutzer*in         │
└──────────────┬───────────────┘
               │
               │ HTTPS
               ▼
┌──────────────────────────────┐
│          Frontend            │
│   Suche / Fragen / Karte     │
└──────────────┬───────────────┘
               │
               │ REST API
               ▼
┌──────────────────────────────┐
│           Backend            │
│                              │
│  - API                       │
│  - Business Logic            │
│  - Decision Logic             │
│  - Validation                │
│  - Location Service           │
│  - External Data Processing   │
└───────┬──────────────┬───────┘
        │              │
        │              │
        ▼              ▼
┌───────────────┐  ┌─────────────────────┐
│   Database    │  │ Externe Datenquellen│
│               │  │                     │
│ Categories    │  │ Stadt Wien          │
│ Items         │  │ MA 48               │
│ Questions     │  │ weitere offene Daten│
│ Rules         │  │                     │
│ Locations     │  └─────────────────────┘
└───────────────┘

3. Frontend

Das Frontend ist für die Benutzeroberfläche verantwortlich.

Aufgaben:

Gegenstand suchen

Kategorie auswählen

Zustandsfragen anzeigen

Antworten erfassen

Empfehlung anzeigen

Begründung anzeigen

passende Standorte anzeigen

Karte anzeigen

Kontaktinformationen anzeigen

Das Frontend enthält möglichst wenig fachliche Entscheidungslogik.

Die eigentliche Entscheidungslogik wird im Backend ausgeführt.

4. Backend

Das Backend stellt die zentrale Schnittstelle zwischen Frontend, Datenbank und externen Datenquellen dar.

Aufgaben:

API-Anfragen entgegennehmen

Eingaben validieren

Daten aus der Datenbank laden

Fragen und Antwortmöglichkeiten bereitstellen

Entscheidungsregeln ausführen

Empfehlungen erzeugen

passende Standorte bestimmen

externe Standortdaten verarbeiten

Fehler behandeln

Antworten an das Frontend zurückgeben

5. Decision Logic

Die Entscheidungslogik befindet sich im Backend.

Der Ablauf:

Frontend
   |
   | Item + Answers
   ▼
Backend
   |
   ▼
Load Rules
   |
   ▼
Evaluate Conditions
   |
   ▼
Select Recommendation
   |
   ▼
Recommendation + Reason
   |
   ▼
Frontend


Die Regeln werden grundsätzlich aus der Datenbank geladen.

6. Database

Die Datenbank speichert die projektrelevanten strukturierten Daten.

Dazu gehören insbesondere:

Kategorien

Gegenstände

Fragen

Antwortmöglichkeiten

Regeln

Empfehlungen

Standorttypen

Standorte

Die Datenbank soll außerdem die Pflege und Erweiterung der Entscheidungsregeln ermöglichen.

7. External Data Sources

Externe Datenquellen werden nicht direkt vom Frontend abgefragt.

Stattdessen übernimmt das Backend den Zugriff.

External Source
      |
      ▼
Backend Data Import
      |
      ▼
Validation / Normalization
      |
      ▼
Database
      |
      ▼
Frontend


Dadurch kann das System externe Daten zwischenspeichern und unabhängig von der Verfügbarkeit der externen Quelle verwenden.

8. Standortdaten

Standortdaten können aus unterschiedlichen Quellen stammen.

Geplant sind insbesondere:

offene Daten der Stadt Wien

MA-48-Daten

weitere offene Standortdaten

geprüfte zusätzliche Einträge, falls erforderlich

Die Daten werden nach Möglichkeit vereinheitlicht.

Ein Standort soll unter anderem folgende Informationen besitzen können:

Name

Adresse

Standorttyp

Koordinaten

Kontaktinformationen

Quelle

Aktualisierungszeitpunkt

9. API-Kommunikation

Frontend und Backend kommunizieren über dokumentierte HTTP-Schnittstellen.

Beispiel:

GET /api/categories
GET /api/items
GET /api/items/{id}/questions
POST /api/recommendations
GET /api/locations


Die endgültigen Endpunkte werden im API-Design genauer definiert.

10. Sicherheitsgrundlagen

Das System soll grundlegende Sicherheitsmaßnahmen berücksichtigen.

Dazu gehören:

Validierung von Benutzereingaben

sichere Verarbeitung von API-Anfragen

Schutz vor ungültigen Daten

keine direkte Datenbankverbindung vom Frontend

sichere Serverkonfiguration

Zugriffsschutz für administrative bzw. interne Funktionen

11. Fehlerbehandlung

Fehler sollen innerhalb der jeweiligen Schicht behandelt werden.

Beispiele:

Frontend
→ Benutzerfreundliche Fehlermeldung

Backend
→ Validierung und strukturierte Fehlermeldung

External Data Source
→ Fehler protokollieren und vorhandene Daten verwenden

Database
→ Fehler erkennen und API-Anfrage kontrolliert beantworten


Das System soll keine internen technischen Details an Benutzer*innen weitergeben.

12. Verantwortlichkeiten
Frontend – Mohamad Rastegar Moghadam

Benutzeroberfläche

Such- und Entscheidungsablauf

Kartenansicht

Kontaktinformationen

Frontend-Backend-Integration

Deployment

Server- und Netzwerkdokumentation

Backend – Noushin Azimi

Backend

Datenbank

Entscheidungslogik

externe Daten

dokumentierte Schnittstellen

Datenimport

Fehlerbehandlung

Backend-Tests

technische Dokumentation

Beide Teammitglieder wirken bei Integration, Tests und Projektdokumentation mit.

13. Grundprinzip

Die Architektur folgt dem Prinzip:

Presentation
     ↓
API
     ↓
Business Logic
     ↓
Data Access
     ↓
Database / External Data


Die einzelnen Komponenten sollen möglichst klar voneinander getrennt sein.

14. Erweiterbarkeit

Die Architektur soll spätere optionale Funktionen ermöglichen, ohne den Kern des Systems grundlegend zu verändern.

Mögliche Erweiterungen:

Posting-Funktion

Foto-Upload

Bilderkennung

Favoriten

Routing

Öffnungszeitenprüfung

Administration

Wertstoff-Wissen