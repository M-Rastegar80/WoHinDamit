# WoHinDamit – Externe Standortdaten und Data Gaps

**Status:** Recherche und Datenquellenbewertung für die Teamabstimmung
**Projektphase:** Phase 1 – Analyse und Basisdesign
**Recherchestand:** 10. Oktober 2026
**Zielgebiet:** Wien, Österreich

## 1. Ziel

Das Projekt WoHinDamit verwendet externe Standortdaten, um Benutzerinnen und Benutzern geeignete Stellen für Reparatur, Weitergabe, Wiederverwendung, Recycling und fachgerechte Entsorgung anzuzeigen.

Das Backend soll externe Daten abrufen oder importieren, validieren, normalisieren und in der eigenen PostgreSQL-Datenbank speichern.

Die Daten sollen über die eigene API bereitgestellt werden. Das Frontend greift nicht direkt auf externe Datenquellen zu.

Dieses Dokument unterscheidet zwischen:

- **Quelle gefunden:** Eine geeignete offizielle Informations- oder Datensatzseite wurde identifiziert.
- **Metadaten geprüft:** Formate, Lizenz oder wesentliche Felder sind anhand der verfügbaren Metadaten dokumentiert.
- **Import noch offen:** Ein produktiver, wiederholbarer Import wurde noch nicht als implementiert bestätigt.
- **Data Gap:** Informationen fehlen, sind unvollständig oder müssen aus weiteren Quellen ermittelt werden.

Die Recherche bedeutet nicht, dass alle Datenquellen bereits technisch integriert oder sämtliche Felder vollständig geprüft wurden.

## 2. Zusammenfassung der Recherche

Für kommunale Abgabestellen in Wien gibt es offizielle, maschinenlesbare Open-Data-Datensätze der Stadt Wien. Der Open-Data-Katalog führt unter anderem Mistplätze, stationäre und mobile Problemstoffsammelstellen sowie Altstoffsammelstellen. Für diese Datensätze werden WFS-Distributionen in Formaten wie CSV, JSON und GML angeboten. Für die genannten Datensätze weist der Katalog CC BY 4.0 aus.

Quellen:

- [Mistplätze Standorte Wien – data.gv.at](https://www.data.gv.at/katalog/dataset/54fc7750-8540-4c22-8d46-9a6a2641c41a)
- [Problemstoffsammelstellen Standorte Wien – data.gv.at](https://www.data.gv.at/datasets/47c7ffa9-65ba-41d1-9461-4e5458afd62e?locale=de)
- [Mobile Problemstoffsammelstellen Standorte Wien – data.gv.at](https://www.data.gv.at/datasets/d2c6f6f5-2236-4217-8361-3297b6369979?locale=de)
- [Open-Data-Katalog der Stadt Wien – Datensätze und Formate](https://www.data.gv.at/katalog/dataset/?organization=stadt-wien&res_format=gml&res_format=svg%2Bxml)

Für Reparatur, Weitergabe und Wiederverwendung gibt es außerdem offizielle Informationsseiten der Stadt Wien sowie Anbieter-Websites. Eine einheitliche offene Datenquelle, die alle benötigten Standorte und Attribute in einem vollständigen Datensatz enthält, wurde im Rahmen dieser Recherche nicht bestätigt.

## 3. Priorität der Datenquellen

Die Datenquellen werden in folgender Reihenfolge untersucht:

1. Offizielle, maschinenlesbare Open-Data-Datensätze der Stadt Wien.
2. Offizielle Informationsseiten und Standortverzeichnisse öffentlicher Einrichtungen.
3. Offizielle Websites von Reparatur- und Wiederverwendungsanbietern.
4. Weitere geprüfte Anbieterinformationen, sofern sie fachlich notwendig sind und zuverlässig aktuell gehalten werden können.

Open-Data-Datensätze sind für den automatisierten Import besonders geeignet. Informationen, die nur auf Websites oder in Terminlisten vorhanden sind, müssen gesondert geprüft und verarbeitet werden.

## 4. Datenquelle A – Mistplätze der MA 48

**Status:** Offizieller Open-Data-Datensatz gefunden; sehr guter Kandidat für den ersten Import.

**Datensatz:** Mistplätze Standorte Wien

**Metadaten:**

[https://www.data.gv.at/katalog/dataset/54fc7750-8540-4c22-8d46-9a6a2641c41a](https://www.data.gv.at/katalog/dataset/54fc7750-8540-4c22-8d46-9a6a2641c41a)

**CSV-WFS:**

[Datensatz als CSV abrufen](https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:MISTPLATZOGD&srsName=EPSG:4326&outputFormat=csv)

**JSON-WFS:**

[Datensatz als JSON abrufen](https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:MISTPLATZOGD&srsName=EPSG:4326&outputFormat=json)

**Lizenz:** CC BY 4.0 laut Datensatzmetadaten.

### Beschriebene Felder

| Quellfeld | Bedeutung | Geplante Verwendung |
|---|---|---|
| `FID` | Feature-ID | Externe Referenz, sofern für den Datensatz stabil |
| `SHAPE` | Punktgeometrie | Geografische Koordinaten |
| `BEZIRK` | Wiener Gemeindebezirk | Bezirk |
| `ADRESSE` | Adresse | Standortadresse |
| `OBJEKT` | Objekt- oder Standortbezeichnung | Name beziehungsweise Bezeichnung, nach Prüfung |
| `OEFFNUNGSZEIT` | Öffnungszeiten | Quelltext zunächst unverändert übernehmen |
| `WEBLINK1` | Weiterführender Link | Quelle oder offizielle Standortinformationen |

Die Metadaten geben die Stadt Wien als veröffentlichende Stelle an und beschreiben die Daten als von der MA 48 erstellt und gepflegt. Im Katalog ist die Aktualisierungsfrequenz als kontinuierlich angegeben.

### Bewertung

Der Datensatz ist ein geeigneter Ausgangspunkt für offizielle kommunale Abgabestandorte mit Adresse und Geodaten.

### Data Gaps

- Die Annahmebedingungen für einzelne Materialien sind nicht vollständig als einfache Standortattribute dokumentiert.
- Mengenbegrenzungen, Ausnahmen und besondere Annahmeregeln müssen gegebenenfalls separat recherchiert werden.
- Öffnungszeiten liegen möglicherweise als Text vor und sind nicht automatisch für eine verlässliche Prüfung „heute geöffnet“ geeignet.
- Vor dem produktiven Import müssen die tatsächlichen Feldwerte, der Umgang mit Koordinaten und die Eindeutigkeit der externen IDs getestet werden.

## 5. Datenquelle B – Stationäre Problemstoffsammelstellen

**Status:** Offizieller Open-Data-Datensatz gefunden; Feldmapping und Importdetails vor Umsetzung nochmals prüfen.

**Datensatz:** Problemstoffsammelstellen Standorte Wien

**Metadaten:**

[https://www.data.gv.at/datasets/47c7ffa9-65ba-41d1-9461-4e5458afd62e?locale=de](https://www.data.gv.at/datasets/47c7ffa9-65ba-41d1-9461-4e5458afd62e?locale=de)

Der Open-Data-Katalog führt für diesen Datensatz WFS-Distributionen, unter anderem in CSV, JSON und GML. Als Lizenz wird CC BY 4.0 ausgewiesen.

### Beschriebene Felder

| Quellfeld | Bedeutung | Geplante Verwendung |
|---|---|---|
| `FID` | Feature-ID | Externe Referenz |
| `SHAPE` | Punktgeometrie | Koordinaten |
| `BEZIRK` | Wiener Gemeindebezirk | Bezirk |
| `ADRESSE` | Adresse | Standortadresse |
| `OEFFNUNGSZEIT` | Öffnungszeiten | Quelltext oder später validierte Struktur |
| `ANMERKUNG` | Zusätzliche Anmerkung | Zusatzinformation |
| `TELEFON` | Telefonnummer | Optionales Kontaktfeld |
| `WEITERE_INFORMATION` | Weiterführender Link | Link zur offiziellen Information |

Die konkrete Feldstruktur muss beim ersten technischen Abruf gegen die aktuelle Datensatzdefinition geprüft werden.

### Bewertung

Diese Quelle ergänzt die Mistplätze um stationäre Problemstoffsammelstellen. Sie ist für den Entsorgungsbereich besonders relevant.

### Data Gaps

- Öffnungszeiten können im Quellformat nicht direkt maschinenlesbar sein.
- Materialannahme und Einschränkungen müssen fachlich geprüft werden.
- Telefonnummern oder weitere Informationen können fehlen oder sich ändern.
- Koordinaten und externe IDs müssen vor dem Import validiert werden.

## 6. Datenquelle C – Mobile Problemstoffsammelstellen

**Status:** Offizieller Open-Data-Datensatz gefunden; Terminbezug muss gesondert behandelt werden.

**Datensatz:** Mobile Problemstoffsammelstellen Standorte Wien

**Metadaten:**

[https://www.data.gv.at/datasets/d2c6f6f5-2236-4217-8361-3297b6369979?locale=de](https://www.data.gv.at/datasets/d2c6f6f5-2236-4217-8361-3297b6369979?locale=de)

Der Datensatz bietet unter anderem CSV-, JSON- und GML-Ausgaben. Die Metadaten nennen CC BY 4.0 als Lizenz und führen den Datensatz als von der MA 48 gepflegt.

### Beschriebene Felder

| Quellfeld | Bedeutung | Geplante Verwendung |
|---|---|---|
| `FID` | Feature-ID | Externe Referenz |
| `SHAPE` | Punktgeometrie | Koordinaten |
| `BEZIRK` | Wiener Gemeindebezirk | Bezirk |
| `ADRESSE` | Adresse | Standortadresse |
| `WEITERE_INFORMATION` | Informationslink | Offizielle Zusatzinformation |
| `TERMINE` | Link zu Terminen und Standorten | Verweis auf aktuelle Termine |
| `STANDORT_ID` | Verknüpfung zur Terminliste | Zuordnung zum Terminbestand |

### Bewertung

Mobile Sammelstellen sind nicht wie dauerhaft geöffnete, stationäre Standorte zu behandeln. Die Information über einen Standort reicht nicht aus, um dessen Verfügbarkeit an einem bestimmten Tag zu bestimmen.

### Data Gaps

- Die Termine müssen separat auf Aktualität geprüft oder über die offizielle Terminseite zugänglich gemacht werden.
- Ein Standort darf nicht ohne Prüfung als aktuell verfügbar dargestellt werden.
- Es muss geklärt werden, wie Terminänderungen oder ausgefallene Termine verarbeitet werden.
- `STANDORT_ID` und `FID` haben unterschiedliche Aufgaben und dürfen nicht ohne Prüfung gleichgesetzt werden.

Für die erste Version kann die Anwendung auf die offizielle Terminseite verweisen, statt nicht verifizierte Termine selbst anzuzeigen.

## 7. Datenquelle D – Altstoffsammelstellen

**Status:** Offizielle Datenquelle identifiziert; Feldmapping für den Import noch zu prüfen.

### Offizielle Stadtplan-Seite

[Altstoffsammelstellen, Hundesackerlspender und weitere Einrichtungen im Stadtplan](https://www.wien.gv.at/umwelt/stadtplan-saubere-stadt)

Die Stadt Wien beschreibt dort den Stadtplan „Saubere Stadt“ mit öffentlichen Altstoffsammelbehältern, Mistplätzen und Problemstoffsammelstellen. Die Standortinformationen können unter anderem Adresse, Art der Altstoffe und weiterführende Links umfassen.

### Open-Data-Katalog

[Open-Data-Katalog der Stadt Wien – Datensätze und Formate](https://www.data.gv.at/katalog/dataset/?organization=stadt-wien&res_format=gml&res_format=svg%2Bxml)

Der Katalog führt einen Datensatz mit der Bezeichnung „Altstoffsammelstellen Standorte Wien“. Als verfügbare Formate sind unter anderem WFS, CSV, JSON und GML aufgeführt.

### Bewertung

Diese Quelle ist für öffentliche Sammelbehälter und die Zuordnung von Materialien zu Sammelmöglichkeiten relevant.

### Data Gaps

- Der konkrete WFS-Endpunkt muss aus den aktuellen Metadaten ermittelt werden.
- Die tatsächlichen Feldnamen und Attributdefinitionen müssen vor dem Import geprüft werden.
- Die genaue Annahmeart je Sammelstelle oder Behälter muss verlässlich abgebildet werden.
- Ein allgemeiner Standortdatensatz darf nicht automatisch als Nachweis gelten, dass jeder beliebige Gegenstand dort abgegeben werden kann.

Die technische Integration beginnt erst nach Prüfung des aktuellen Datenformats und der fachlichen Bedeutung der Felder.

## 8. Datenquelle E – Reparaturmöglichkeiten und Weitergabe

**Status:** Offizielle Informationsquellen gefunden; ein vollständiger einheitlicher Open-Data-Feed wurde in dieser Recherche nicht bestätigt.

### Stadt Wien – Reparatur, Flohmärkte und karitative Einrichtungen

[Offizielle Übersicht der Stadt Wien](https://www.wien.gv.at/umwelt/reparaturmoeglichkeiten-flohmaerkte-einrichtungen)

Die Stadt Wien verweist unter anderem auf:

- ReparaturNetzwerk Wien;
- R.U.S.Z Reparatur- und Service-Zentrum;
- Repair-Cafés;
- Leih- und Weitergabeangebote;
- karitative Einrichtungen;
- Flohmärkte und Gebrauchtwarenangebote.

Diese Übersicht ist eine geeignete Quelle, um weitere Anbieter und mögliche Datenquellen für Reparatur, Wiederverwendung und Weitergabe zu identifizieren.

### 48er-Tandler

[Offizielle Website des 48er-Tandlers](https://48ertandler.wien.gv.at/)

Die offizielle Website bietet Standort- und Öffnungszeiteninformationen zu den 48er-Tandler-Standorten.

Diese Quelle kann für Wiederverwendungs- und Gebrauchtwarenangebote relevant sein. Vor einem automatischen Import muss jedoch geklärt werden, welche Informationen maschinenlesbar und für die geplante Verwendung geeignet sind.

### Bewertung und Data Gaps

- Ein einheitlicher Datensatz mit allen Reparaturwerkstätten, Repair-Cafés und Weitergabestellen wurde nicht bestätigt.
- Standorte, Öffnungszeiten und Angebote müssen möglicherweise auf verschiedenen Websites geprüft werden.
- Termine, angebotene Gegenstandskategorien und Kapazitäten können sich ändern.
- Eine Erwähnung auf einer Informationsseite ist nicht automatisch ein Nachweis für eine dauerhaft verfügbare Dienstleistung.
- Private Anbieter dürfen nur aufgenommen werden, wenn Quelle, Datenverwendung und Aktualisierung ausreichend geklärt sind.

Für diese Gruppe ist ein schrittweiser Ansatz sinnvoll: zunächst zuverlässige offizielle Anbieterinformationen erfassen, anschließend mögliche offene Datenquellen oder geprüfte Einträge ergänzen.

## 9. Übersicht der Quellen

| Quelle | Geplante Verwendung | Format / Zugang | Lizenzstatus | Stand |
|---|---|---|---|---|
| MA 48 – Mistplätze | Kommunale Abgabestellen | WFS, CSV, JSON und Geodatenformate | CC BY 4.0 laut Metadaten | Quelle gefunden, Metadaten geprüft |
| Stationäre Problemstoffsammelstellen | Problemstoffentsorgung | WFS und verschiedene Datenformate laut Katalog | CC BY 4.0 laut Metadaten | Quelle gefunden, Importdetails prüfen |
| Mobile Problemstoffsammelstellen | Mobile Sammlung und Termine | WFS, CSV, JSON, GML und weitere Formate | CC BY 4.0 laut Metadaten | Quelle gefunden, Terminverknüpfung offen |
| Altstoffsammelstellen | Öffentliche Sammelbehälter | WFS, CSV, JSON und weitere Formate laut Katalog | Lizenz auf der konkreten Distribution prüfen | Quelle gefunden, Feldmapping offen |
| Stadt Wien – Reparatur und Weitergabe | Anbieter finden und Standortinformationen prüfen | Offizielle Informationsseite und externe Links | Nutzungsbedingungen je Quelle prüfen | Informationsquelle gefunden |
| 48er-Tandler | Wiederverwendung und Gebrauchtwaren | Offizielle Website | Nutzungsbedingungen für eine Übernahme prüfen | Informationsquelle gefunden |
| Reparaturwerkstätten und Repair-Cafés | Reparaturmöglichkeiten | Verschiedene Anbieter-Websites und mögliche offene Quellen | Je Quelle prüfen | Vollständige Datenbasis offen |
| Gebrauchtwaren- und Gebrauchtwagenhändler | Verkauf und Weitergabe | Geprüfte Anbieterinformationen oder geeignete Datensätze | Je Quelle prüfen | Weitere Recherche notwendig |

Die Lizenzangaben gelten für die jeweiligen Katalogeinträge beziehungsweise Distributionen. Vor einem produktiven Import sind die Bedingungen des tatsächlich verwendeten Downloads oder Dienstes nochmals zu prüfen.

## 10. Zielmodell für importierte Standortdaten

Die externen Datensätze werden auf ein einheitliches internes Modell abgebildet.

| Internes Feld | Bedeutung | Regel |
|---|---|---|
| `id` | Interne Standort-ID | Wird von der eigenen Datenbank erzeugt |
| `location_type_id` | Interner Standorttyp | Zuordnung über ein kontrolliertes Mapping |
| `name` | Standortname | Nur übernehmen, wenn geliefert oder zuverlässig abgeleitet |
| `address` | Adresse | Quellwert übernehmen und gegebenenfalls normalisieren |
| `postal_code` | Postleitzahl | Optional; nicht aus unsicheren Annahmen erzeugen |
| `city` | Stadt | Nur übernehmen oder zuverlässig normalisieren |
| `district` | Bezirk | Aus der Quelle übernehmen |
| `latitude` | Breitengrad | Aus validierter WGS84-Geometrie |
| `longitude` | Längengrad | Aus validierter WGS84-Geometrie |
| `phone` | Telefonnummer | Optional; andernfalls `NULL` |
| `email` | E-Mail-Adresse | Optional; andernfalls `NULL` |
| `website` | Website | Optional; andernfalls `NULL` |
| `opening_hours` | Öffnungszeiten | Quelltext zunächst erhalten, Parsing getrennt prüfen |
| `accepted_materials` | Angenommene Materialien | Nur bei verlässlichem Nachweis |
| `source` | Quelle oder Organisation | Für importierte Datensätze dokumentieren |
| `external_id` | Externe Referenz | Verwenden, wenn stabil und eindeutig |
| `source_url` | Datensatz- oder Standortlink | Wenn verfügbar |
| `source_updated_at` | Aktualisierungszeit laut Quelle | Nur setzen, wenn die Quelle den Wert liefert |
| `last_imported_at` | Zeitpunkt des Imports | Durch das Backend setzen |
| `active` | Aktivstatus | Abhängig von Validierung und Aktualisierungsstrategie |

Die internen Felder bilden das angestrebte Modell ab. Es ist noch zu prüfen, ob die aktuelle Entity `Location` alle benötigten Felder bereits besitzt.

Fehlende Kontaktdaten und Öffnungszeiten werden nicht erfunden.

## 11. Normalisierung

Die Quelldaten können unterschiedliche Feldnamen und Formate verwenden. Sie werden deshalb vor der Speicherung normalisiert.

### Beispiel

**Externe Felder:**

```text
FID
SHAPE
BEZIRK
ADRESSE
OBJEKT
OEFFNUNGSZEIT
WEBLINK1
```

**Zielmodell:**

```text
source
external_id
name
address
district
latitude
longitude
opening_hours
website
source_url
last_imported_at
```

Das Mapping muss für jeden Datensatz dokumentiert werden. Unklare Felder dürfen nicht ohne Prüfung einer internen Bedeutung zugewiesen werden.

### Koordinaten

Die Quelle kann Geometrien in einem bestimmten Koordinatenreferenzsystem liefern. Für die Standortsuche wird WGS84 benötigt, sofern das interne Modell geografische Breiten- und Längengrade verwendet.

Vor dem Import müssen das Koordinatenreferenzsystem und die Achsenreihenfolge geprüft werden. Vertauschte Koordinaten können zu falschen Kartenpositionen führen.

### Öffnungszeiten

Öffnungszeiten werden zunächst als Quelltext oder gemäß dem bestätigten Zieldatentyp gespeichert. Eine Umwandlung in strukturierte Tages- und Zeitintervalle ist getrennt zu testen.

Eine Anzeige „jetzt geöffnet“ darf erst eingeführt werden, wenn die Öffnungszeiten korrekt normalisiert wurden und Ausnahmen, Feiertage sowie Datenaktualität ausreichend berücksichtigt werden.

## 12. Datenqualität

Vor der Speicherung und Veröffentlichung sind mindestens folgende Punkte zu prüfen:

- Sind die Pflichtangaben des jeweiligen Quelltyps vorhanden?
- Ist die Quell-ID eindeutig oder gibt es eine andere verlässliche Methode zur Duplikaterkennung?
- Sind Adresse und Name plausibel?
- Sind die Koordinaten vorhanden und gültig, sofern benötigt?
- Sind Koordinaten richtig transformiert und zugeordnet?
- Ist der Standorttyp bekannt?
- Sind Links und Kontaktinformationen plausibel?
- Sind Öffnungszeiten und Termine korrekt als solche eingeordnet?
- Ist die Quelle weiterhin verfügbar?
- Ist die Lizenz für die geplante Verwendung geeignet?
- Gibt es Konflikte zwischen bestehenden und neu importierten Datensätzen?

Ungültige Datensätze werden nachvollziehbar protokolliert. Sie dürfen nicht unbemerkt als verlässliche Standorte an das Frontend weitergegeben werden.

## 13. Import und Aktualisierung

Der vorgesehene Importprozess lautet:

```text
Externe Datenquelle
        |
        v
Datenabruf
        |
        v
Prüfung der Antwort und des Formats
        |
        v
Validierung der Felder
        |
        v
Normalisierung und Mapping
        |
        v
Duplikatprüfung und Zuordnung
        |
        v
Speicherung / Aktualisierung
        |
        v
Importstatus und Fehlerprotokoll
```

### Duplikate vermeiden

Wenn eine Quelle stabile externe IDs bereitstellt, wird eine eindeutige Kombination aus Quelle und externer ID empfohlen:

`UNIQUE(source, external_id)`

Die konkrete Umsetzung muss berücksichtigen, dass manche Quellen keine stabile ID bereitstellen oder unterschiedliche IDs für Standort und Termin verwenden.

### Aktualisierung

Vorhandene Datensätze sollen nicht bei jedem Import blind überschrieben werden.

Die Aktualisierungsstrategie muss festlegen:

- welche Felder aus der Quelle führend sind;
- welche manuell gepflegten Angaben geschützt werden;
- wie entfernte oder nicht mehr gelieferte Standorte behandelt werden;
- wie Importfehler erkannt und dokumentiert werden;
- wie Quelländerungen nachvollziehbar bleiben.

Das Intervall für den Import wird je Quelle festgelegt. Ein automatischer, regelmäßiger Import ist vorgesehen, aber im aktuellen Prototyp noch nicht als umgesetzt zu betrachten.

## 14. Lizenz und Quellenangabe

Für die genannten kommunalen Datensätze weist der Open-Data-Katalog CC BY 4.0 aus.

Bei der Verwendung müssen die geltenden Lizenzbedingungen und der erforderliche Quellenvermerk eingehalten werden.

Ein möglicher Quellenvermerk lautet:

„Datenquelle: Stadt Wien – MA 48, [Name des Datensatzes], CC BY 4.0, [Link zum Datensatz].“

Bei bearbeiteten Daten kann zusätzlich kenntlich gemacht werden, dass die Daten normalisiert oder verändert wurden.

Vor jeder produktiven Integration sind Lizenz und Nutzungsbedingungen der konkreten Quelle und Distribution erneut zu prüfen. Für Anbieter-Websites gelten nicht automatisch dieselben Bedingungen wie für kommunale Open Data.

## 15. Behandlung von Data Gaps

Nicht für jeden Standorttyp existiert eine vollständige, maschinenlesbare und frei nutzbare Quelle.

Daher gelten folgende Grundsätze:

| Datenlage | Behandlung |
|---|---|
| Vollständige und verlässliche Daten | Validieren, normalisieren und verwenden |
| Teilweise Daten | Vorhandene Informationen verwenden und fehlende Angaben als nicht verfügbar behandeln |
| Unklare Datenqualität | Vor Veröffentlichung prüfen oder vorerst nicht verwenden |
| Keine geeignete Quelle | Datenlücke dokumentieren; keine vollständige Abdeckung behaupten |
| Quelle vorübergehend nicht verfügbar | Importfehler protokollieren; vorhandene gespeicherte Daten nur gemäß Aktualitätsregeln verwenden |
| Mobile oder terminabhängige Standorte | Aktuelle Termine separat prüfen oder auf offizielle Terminseiten verweisen |

Die Anwendung darf nicht den Eindruck vermitteln, sämtliche Reparatur-, Verkaufs-, Recycling- und Entsorgungsstandorte in Wien vollständig abzudecken, solange dies nicht nachgewiesen ist.

## 16. Erste Importstrategie

Für die erste technische Umsetzung wird folgender Ansatz empfohlen:

**Schritt 1:** Mistplätze der MA 48 importieren.

**Schritt 2:** Stationäre Problemstoffsammelstellen ergänzen.

**Schritt 3:** Altstoffsammelstellen und die Zuordnung von Sammelbehältern zu Materialarten untersuchen.

**Schritt 4:** Mobile Problemstoffsammelstellen integrieren, sobald Terminverknüpfung und Aktualität geklärt sind.

**Schritt 5:** Reparaturwerkstätten, Repair-Cafés, Weitergabestellen und Gebrauchtwarenangebote anhand geeigneter Quellen ergänzen.

**Schritt 6:** Importwiederholungen, Duplikaterkennung, Datenaktualisierung und Fehlerprotokollierung testen.

Diese Reihenfolge priorisiert zunächst klar identifizierbare kommunale Datensätze. Sie stellt keine Aussage darüber dar, dass damit bereits alle relevanten Standorte abgedeckt werden.

## 17. Offene Aufgaben und Entscheidungen

Vor der Umsetzung sind folgende Punkte zu klären:

1. Exakte Felddefinitionen und Download-URLs der ausgewählten Datensätze beim ersten technischen Abruf überprüfen.
2. Lizenzangaben für jede tatsächlich verwendete Distribution dokumentieren.
3. Feldmapping und Koordinatensysteme anhand realer Daten testen.
4. Stabile externe IDs identifizieren und eine Duplikatstrategie definieren.
5. Materialannahme und Standorttypen fachlich verlässlich zuordnen.
6. Quelle und Aktualisierungsverfahren für mobile Sammeltermine festlegen.
7. Recherche zu Reparaturwerkstätten, Repair-Cafés und Weitergabestellen fortführen.
8. Entity `Location` und das interne Datenmodell mit dem Datenbankdesign abgleichen.
9. Importintervall, Fehlerprotokollierung und Umgang mit entfernten Datensätzen festlegen.
10. Eine verständliche Quellen- und Lizenzanzeige für das Frontend definieren.

## 18. Status des Projekts

Der erste Schwerpunkt der Recherche sind offizielle Open-Data-Datensätze der Stadt Wien, insbesondere Mistplätze und Problemstoffsammelstellen.

Die Datensatzseiten und mögliche maschinenlesbare Formate sind identifiziert. Die genaue Feldstruktur, die produktiven Importabläufe, das vollständige Mapping der Materialannahme und die Integration weiterer Standorttypen benötigen noch technische beziehungsweise fachliche Prüfung.

Die Standort-API und die externe Importpipeline sind als geplante Arbeit zu behandeln, solange sie nicht implementiert und erfolgreich getestet wurden.

## 19. Zusammenfassung

Die Stadt Wien stellt offizielle Daten zu mehreren Kategorien kommunaler Abgabestellen bereit. Diese Datensätze bilden einen geeigneten Einstieg für die externe Standortdatenintegration von WoHinDamit.

Für jede Quelle werden Lizenz, Felder, Koordinaten, externe IDs, Aktualisierung und Datenlücken dokumentiert. Die Daten werden durch das Backend verarbeitet und über die eigene API bereitgestellt.

Das wichtigste Prinzip lautet:

**Nur verlässliche und nachvollziehbare Informationen werden als gesichert dargestellt. Fehlende oder ungeklärte Daten werden dokumentiert und nicht erfunden.**
