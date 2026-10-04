WoHinDamit? – External Data Sources
1. Ziel

Das Projekt verwendet externe Standortdaten, um Nutzer*innen geeignete Stellen für Reparatur, Weitergabe, Recycling und fachgerechte Entsorgung anzeigen zu können.

Die externen Daten werden vom Backend abgerufen, geprüft, aufbereitet und in der eigenen Datenbank zwischengespeichert.

2. Primäre Datenquelle – Stadt Wien / MA 48

Als wichtige Datenquelle wird der Open-Data-Datensatz:

„Mistplätze Standorte Wien“

verwendet.

Der Datensatz wird von der Stadt Wien veröffentlicht und von der Magistratsabteilung 48 (MA 48) erstellt und gepflegt.

Der Datensatz enthält unter anderem:

Standort-ID

Koordinaten

Wiener Gemeindebezirk

Adresse

Art des Standortes

Öffnungszeiten

Weblink

Die Daten werden für Wien bereitgestellt.

3. Datenformat

Für den automatisierten Import kann insbesondere der WFS-Dienst der Stadt Wien verwendet werden.

Ein JSON-Format ist für die Backend-Verarbeitung besonders geeignet.

Die Daten sollen vom Backend abgerufen und anschließend in das interne Datenmodell übertragen werden.

4. Datenverarbeitung

Der geplante Ablauf:

Externe Datenquelle
        |
        v
Abruf durch Backend
        |
        v
Validierung
        |
        v
Normalisierung
        |
        v
Mapping auf internes Datenmodell
        |
        v
Speicherung in Database
        |
        v
API
        |
        v
Frontend / Karte

5. Normalisierung

Externe Daten verwenden möglicherweise andere Feldnamen und Strukturen als die eigene Datenbank.

Daher werden die Daten auf ein einheitliches internes Format übertragen.

Beispiel:

Externe Daten:

ADRESSE
BEZIRK
SHAPE
OBJEKT
OEFFNUNGSZEIT
WEBLINK1

        ↓

Interne Datenbank:

address
postal_code
latitude
longitude
location_type
opening_hours
website

6. Datenqualität

Vor der Speicherung müssen externe Daten geprüft werden.

Insbesondere:

Ist ein Name vorhanden?

Ist eine Adresse vorhanden?

Sind Koordinaten gültig?

Ist der Standorttyp bekannt?

Sind Öffnungszeiten vorhanden?

Ist die externe Quelle erreichbar?

Fehlende Daten werden nicht erfunden.

Nicht vorhandene Informationen werden als NULL bzw. nicht verfügbar gespeichert.

7. Aktualisierung

Externe Standortdaten können sich ändern.

Daher soll gespeichert werden:

externe Quelle

externe ID

Zeitpunkt des letzten Imports

Zeitpunkt der letzten Aktualisierung

Dadurch können bestehende Datensätze aktualisiert werden, ohne Duplikate zu erzeugen.

8. Zwischenspeicherung

Das Frontend greift nicht direkt auf externe Datenquellen zu.

Die Daten werden im Backend verarbeitet und in der eigenen Datenbank gespeichert.

Vorteile:

schnellere Anfragen

weniger Abhängigkeit von externen Diensten

bessere Kontrolle der Datenqualität

einheitliches Datenformat

externe Ausfälle können abgefangen werden

9. Weitere Standortdaten

Falls die Datenqualität und Verfügbarkeit ausreichend sind, sollen weitere Quellen untersucht werden.

Mögliche Datenarten:

Sammelstellen

Reparaturwerkstätten

Repair-Cafés

Weitergabestellen

Gebrauchtwarenhändler

Gebrauchtwagenhändler

Diese Daten werden nur eingebunden, wenn eine geeignete offene oder geprüfte Datenquelle verfügbar ist.

10. Umgang mit Datenlücken

Nicht für jeden Standorttyp müssen vollständige offene Daten verfügbar sein.

Daher gilt:

Daten vorhanden
    ↓
verwenden

Daten teilweise vorhanden
    ↓
verwenden + fehlende Informationen kennzeichnen

Keine geeignete Datenquelle
    ↓
nicht als vollständig abgedeckt darstellen


Die Anwendung darf keine vollständige Abdeckung aller Standorte versprechen.

11. Quellenangabe

Bei der Verwendung von Open Government Data der Stadt Wien muss die jeweilige Datenquelle entsprechend den Nutzungsbedingungen angegeben werden.

Für verwendete Daten soll im Projekt dokumentiert werden:

Name der Quelle

URL

Lizenz

Abrufdatum

verwendete Felder

Transformationsschritte

12. Erste geplante Datenquelle
Quelle	Typ	Verwendung	Status
Stadt Wien / MA 48 – Mistplätze	Open Data / WFS	Entsorgung	Geprüft
Stadt Wien – Sammelstellen	Open Data	Recycling	Zu prüfen
Reparaturwerkstätten	Open Data / geprüfte Einträge	Reparatur	Zu prüfen
Repair-Cafés	Open Data / geprüfte Einträge	Reparatur	Zu prüfen
Gebrauchtwarenhändler	Open Data / geprüfte Einträge	Weitergabe / Verkauf	Zu prüfen
Gebrauchtwagenhändler	Open Data / geprüfte Einträge	Fahrzeuge	Zu prüfen
13. Technische Aufgabe des Backends

Das Backend übernimmt später:

Abruf der externen Daten

Prüfung der Antwort

Validierung der Daten

Normalisierung

Mapping auf das interne Datenmodell

Speicherung in der Datenbank

Aktualisierung bestehender Datensätze

Behandlung von Fehlern

Protokollierung des Imports

14. Status

Der erste geprüfte Datenquellentyp ist:

Mistplätze der MA 48 / Stadt Wien

Weitere Quellen werden im weiteren Projektverlauf untersucht und nur bei ausreichender Datenqualität integriert.