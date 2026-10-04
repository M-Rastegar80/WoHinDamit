WoHinDamit? – Final Data Model
1. Ziel

Die Datenbank bildet die zentrale Datenbasis der Anwendung.

Sie speichert:

Gegenstände

Kategorien

Fragen

Antwortmöglichkeiten

Entscheidungsregeln

Empfehlungen

Standorttypen

Standorte

Die Datenbank soll eine Erweiterung des Systems ermöglichen, ohne die gesamte Anwendung verändern zu müssen.

2. Entity Overview

Das Datenmodell besteht aus folgenden Hauptentitäten:

Category
   |
   +------< Item
   |
   +------< Question
               |
               +------< AnswerOption

Item / Category
       |
       +------< Rule >------ Recommendation


LocationType
       |
       +------< Location

3. Category

Eine Kategorie gruppiert ähnliche Gegenstände.

Beispiele

Elektrogeräte

Möbel

Kleidung

Fahrräder

Fahrzeuge

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
name	String	Name der Kategorie
description	Text	Beschreibung
parent_id	Integer / UUID / NULL	Übergeordnete Kategorie
active	Boolean	Kategorie aktiv/inaktiv
Beziehung
Category 1 ─── N Item
Category 1 ─── N Question

4. Item

Ein Item beschreibt einen konkreten Gegenstand.

Beispiele

Fahrrad

Laptop

Kühlschrank

Waschmaschine

Sofa

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
category_id	Integer / UUID	Kategorie
name	String	Name
description	Text	Beschreibung
active	Boolean	Aktiv/inaktiv
Beziehung
Category 1 ─── N Item


Ein Item gehört zu genau einer Hauptkategorie.

5. Question

Eine Question beschreibt eine Frage, die Benutzer*innen zum Zustand oder zu Eigenschaften eines Gegenstands beantworten.

Beispiele

Ist der Gegenstand noch funktionsfähig?

Ist der Gegenstand reparierbar?

Ist der Gegenstand vollständig?

Ist der Gegenstand noch verwendbar?

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
category_id	Integer / UUID	Kategorie
question_text	Text	Fragetext
answer_type	String	Antworttyp
order_index	Integer	Reihenfolge
required	Boolean	Muss beantwortet werden
active	Boolean	Aktiv/inaktiv
Beziehung
Category 1 ─── N Question

6. AnswerOption

Eine AnswerOption beschreibt eine mögliche Antwort auf eine Frage.

Beispiele

Ja

Nein

Teilweise

Unbekannt

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
question_id	Integer / UUID	Frage
value	String	Interner Wert
label	String	Angezeigter Text
order_index	Integer	Reihenfolge
Beziehung
Question 1 ─── N AnswerOption

7. Recommendation

Recommendation beschreibt eine mögliche Handlungsempfehlung.

Mögliche Aktionen
REPAIR
SELL
GIVE_AWAY
DONATE
RECYCLE
DISPOSE

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
action	String / Enum	Handlung
title	String	Anzeigename
description	Text	Beschreibung
priority	Integer	Priorität
active	Boolean	Aktiv/inaktiv
8. Rule

Rule beschreibt eine Entscheidungsregel.

Die Regeln bilden das Herzstück der Entscheidungslogik.

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
category_id	Integer / UUID / NULL	Kategorie
item_id	Integer / UUID / NULL	Optional konkreter Gegenstand
recommendation_id	Integer / UUID	Ergebnis
priority	Integer	Priorität
conditions	JSON	Bedingungen
reason	Text	Begründung
active	Boolean	Aktiv/inaktiv
Beispiel
{
  "repairable": true,
  "functional": false
}


Ergebnis:

REPAIR


Begründung:

Der Gegenstand ist reparierbar und kann weiterverwendet werden.

Beziehungen
Category 1 ─── N Rule
Item 1 ─── N Rule
Recommendation 1 ─── N Rule


category_id oder item_id können NULL sein, wenn die Regel allgemeiner gilt.

9. LocationType

LocationType definiert die Art eines Standorts.

Beispiele
MISTPLATZ
SAMMELSTELLE
REPAIR_WORKSHOP
REPAIR_CAFE
GIVE_AWAY_CENTER
SECOND_HAND_SHOP
USED_CAR_DEALER

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
name	String	Name
description	Text	Beschreibung
active	Boolean	Aktiv/inaktiv
10. Location

Location beschreibt einen konkreten Standort.

Felder
Feld	Typ	Beschreibung
id	Integer / UUID	Primärschlüssel
location_type_id	Integer / UUID	Standorttyp
name	String	Name
address	String	Adresse
postal_code	String	Postleitzahl
city	String	Stadt
latitude	Decimal	Breitengrad
longitude	Decimal	Längengrad
phone	String / NULL	Telefonnummer
email	String / NULL	E-Mail
website	String / NULL	Website
opening_hours	JSON / Text / NULL	Öffnungszeiten
source	String	Datenquelle
source_id	String / NULL	Externe ID
last_updated	DateTime	Letzte Aktualisierung
active	Boolean	Aktiv/inaktiv
Beziehung
LocationType 1 ─── N Location

11. Location und Eignung

Ein Standort ist nicht automatisch für jeden Gegenstand geeignet.

Daher wird später eine zusätzliche Zuordnung benötigt.

Geplante Beziehung:

Item N ─── N Location


Diese Beziehung kann über eine Zwischentabelle umgesetzt werden:

ItemLocation

Geplante Felder
Feld	Typ	Beschreibung
item_id	Integer / UUID	Gegenstand
location_id	Integer / UUID	Standort
suitable	Boolean	Geeignet
note	Text / NULL	Zusatzinformation

Damit kann beispielsweise gespeichert werden:

Fahrrad
   ↓
Repair-Café A
   ↓
geeignet = true


aber:

Waschmaschine
   ↓
Repair-Café A
   ↓
geeignet = false

12. External Data

Für externe Daten werden zusätzliche Informationen gespeichert.

Wichtige Felder in Location:

source
source_id
last_updated


Damit kann ein Standort aus einer externen Quelle eindeutig erkannt und später aktualisiert werden.

13. Beziehungen – Gesamtübersicht
                    ┌──────────────┐
                    │   Category   │
                    └──────┬───────┘
                           │
                 ┌─────────┴─────────┐
                 │                   │
                 ▼                   ▼
             ┌───────┐          ┌──────────┐
             │ Item  │          │ Question │
             └───┬───┘          └────┬─────┘
                 │                    │
                 │                    ▼
                 │              ┌─────────────┐
                 │              │AnswerOption │
                 │              └─────────────┘
                 │
                 ▼
             ┌────────┐
             │  Rule  │
             └───┬────┘
                 │
                 ▼
          ┌──────────────┐
          │Recommendation│
          └──────────────┘


       ┌──────────────┐
       │ LocationType │
       └───────┬──────┘
               │
               ▼
          ┌──────────┐
          │ Location │
          └────┬─────┘
               │
               │
          ItemLocation
               │
               │
               ▼
             Item

14. Datenfluss

Der Datenfluss innerhalb des Systems:

Benutzer*in
    |
    ▼
Gegenstand auswählen
    |
    ▼
Category / Item
    |
    ▼
Questions
    |
    ▼
AnswerOptions
    |
    ▼
Rule Evaluation
    |
    ▼
Recommendation
    |
    ▼
passende Locations
    |
    ▼
Karte

15. Datenbank-Grundsätze

Die Datenbank soll:

eindeutige IDs verwenden

Beziehungen über Foreign Keys abbilden

ungültige Referenzen verhindern

aktive und inaktive Datensätze unterscheiden können

externe Datenquellen nachvollziehbar speichern

Erweiterungen ermöglichen

16. Noch zu prüfen

Vor der Implementierung müssen folgende Punkte technisch festgelegt werden:

konkretes Datenbankmanagementsystem

Integer oder UUID als ID

genaue JSON-Struktur von Rule.conditions

genaue Berechnung der Entfernung

Indexierung der Standortkoordinaten

Importstrategie für externe Daten

Aktualisierungsintervall der externen Daten