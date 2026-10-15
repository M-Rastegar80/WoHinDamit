# WoHinDamit? – Requirements

**Projekt:** WoHinDamit? – Digitale Recycling- und Weitergabehilfe für Wien
**Schuljahr:** 2026/27
**Projektart:** Webbasierter Prototyp
**Dokumentstatus:** Entwurf für Phase 1 – fachliche Abstimmung und Freigabe durch das Projektteam erforderlich
**Zielgebiet:** Wien, Österreich

---

## 1. Projektübersicht

WoHinDamit? ist eine webbasierte Anwendung, die Nutzer*innen dabei unterstützt, für alte, defekte oder nicht mehr benötigte Gegenstände eine sinnvolle nächste Handlung zu finden.

Abhängig von Gegenstand, Kategorie, Zustand und weiteren relevanten Eigenschaften soll das System eine nachvollziehbare Empfehlung geben. Wenn eine geeignete Anlaufstelle bekannt ist, soll die Anwendung passende Standorte in Wien anzeigen.

Das System verwendet eigene, in der Datenbank gespeicherte Entscheidungsregeln. Diese Regeln sollen möglichst ohne Änderungen am Java-Code gepflegt und erweitert werden können.

Die grundsätzliche fachliche Orientierung lautet:

1. Reparieren, wenn dies sinnvoll und möglich ist.
2. Verkaufen, weitergeben oder spenden, wenn der Gegenstand noch genutzt werden kann.
3. Recyceln oder fachgerecht entsorgen, wenn eine weitere Nutzung nicht sinnvoll oder möglich ist.

Diese Reihenfolge dient als allgemeine Orientierung. Die tatsächliche Empfehlung muss die Eigenschaften des Gegenstands und die passenden Entscheidungsregeln berücksichtigen.

## 2. Zielgruppe und Benutzerführung

Die Anwendung richtet sich an Personen in Wien, die unsicher sind, was sie mit einem alten, defekten oder nicht mehr benötigten Gegenstand machen sollen.

Die Benutzerführung soll einfach, verständlich und ohne besondere technische Kenntnisse nutzbar sein.

Nutzer*innen sollen schrittweise zu einer geeigneten Empfehlung gelangen. Die Anwendung soll nicht nur eine Handlung nennen, sondern auch erklären, warum diese empfohlen wird.

## 3. Funktionale Anforderungen

Die folgenden Anforderungen beschreiben den geplanten Funktionsumfang des Projekts. Die Prioritäten beziehen sich auf den fachlichen Projektumfang und nicht auf die numerische Priorität einzelner Entscheidungsregeln.

### 3.1 Benutzerführung und Gegenstandsauswahl

| ID | Anforderung | Priorität |
|---|---|---|
| FR-01 | Nutzer*innen können aktive Kategorien anzeigen und auswählen. | Muss |
| FR-02 | Nutzer*innen können einen Gegenstand über eine Textsuche finden und auswählen. | Muss |
| FR-03 | Nutzer*innen können einen Gegenstand über seine Kategorie auswählen. | Muss |
| FR-04 | Das System kann zu einer Kategorie die relevanten aktiven Fragen in definierter Reihenfolge liefern. | Muss |
| FR-05 | Das System kann die gültigen Antwortmöglichkeiten einer Frage mit stabilen Werten und verständlichen Bezeichnungen bereitstellen. | Muss |
| FR-06 | Nutzer*innen können den Zustand und weitere relevante Eigenschaften eines Gegenstands mithilfe einfacher Fragen beschreiben. | Muss |

### 3.2 Entscheidungssystem und Empfehlungen

| ID | Anforderung | Priorität |
|---|---|---|
| FR-07 | Das Backend validiert die übermittelten Antworten. | Muss |
| FR-08 | Das System verwendet in der Datenbank gespeicherte Entscheidungsregeln. | Muss |
| FR-09 | Die Regeln berücksichtigen je nach Anwendungsfall Gegenstand, Kategorie, Zustand, Reparierbarkeit und weitere relevante Eigenschaften. | Muss |
| FR-10 | Das System prüft, welche aktiven Regeln zu den übermittelten Antworten passen. | Muss |
| FR-11 | Wenn mehrere Regeln passen, wird eine vom Projektteam festgelegte Prioritätsregel angewendet. | Muss |
| FR-12 | Das System liefert eine Empfehlung mit Handlung, Titel, Beschreibung und nachvollziehbarer Begründung. | Muss |
| FR-13 | Das System behandelt den Fall, dass keine passende Regel gefunden wird, ausdrücklich und dokumentiert. | Muss |
| FR-14 | Entscheidungsregeln sollen ohne Änderung am Java-Code gepflegt und erweitert werden können. | Muss |

Mögliche Handlungen sind beispielsweise Reparieren, Verkaufen, Weitergeben, Spenden, Recyceln oder fachgerechtes Entsorgen. Die endgültigen Aktionswerte müssen im Datenmodell und in der API einheitlich definiert werden.

Die fachliche Reihenfolge der Handlungsmöglichkeiten ist von der numerischen Priorität einer Regel zu unterscheiden. Die konkrete Bedeutung der numerischen Regelpriorität muss vor der endgültigen Freigabe eindeutig festgelegt werden.

### 3.3 Standortsuche und Standortinformationen

| ID | Anforderung | Priorität |
|---|---|---|
| FR-15 | Das System stellt geeignete Standorte in Wien bereit. | Muss |
| FR-16 | Die Standorte können nach Standorttyp gefiltert werden. | Muss |
| FR-17 | Soweit entsprechende Daten vorhanden sind, werden Standorte nach ihrer Eignung für den jeweiligen Gegenstand berücksichtigt. | Muss |
| FR-18 | Soweit geeignete Koordinaten und eine Benutzerposition oder gewählte Position vorhanden sind, kann die Entfernung berücksichtigt werden. | Muss für die geplante Standortsuche; Umsetzung abhängig von den verfügbaren Daten |
| FR-19 | Für einen Standort werden Name, Adresse, Standorttyp und verfügbare Kontaktinformationen angezeigt. | Muss |
| FR-20 | Soweit Daten verfügbar sind, können Koordinaten, Öffnungszeiten und weitere relevante Informationen angezeigt werden. | Muss im Rahmen der Datenverfügbarkeit |
| FR-21 | Fehlende oder unbekannte Informationen werden als nicht verfügbar behandelt und nicht erfunden. | Muss |

Zu den vorgesehenen Standorttypen gehören insbesondere:

- Mistplätze
- stationäre und mobile Sammelstellen für Problemstoffe
- weitere geeignete Sammelstellen
- Reparaturwerkstätten
- Repair-Cafés
- Weitergabe- und Spendenstellen
- Gebrauchtwarenhändler
- bei geeigneten Anwendungsfällen Gebrauchtwagenhändler

Die tatsächliche Abdeckung hängt von den verfügbaren, geeigneten und verlässlich aufbereitbaren Datenquellen ab. Eine vollständige Abdeckung aller Standorte wird nicht vorausgesetzt.

Die Anwendung soll eine Kartenansicht ermöglichen. Die Anzeige konkreter Standorte setzt voraus, dass geeignete Standortdaten vorhanden sind.

### 3.4 Webanwendung und Frontend-Kommunikation

| ID | Anforderung | Priorität |
|---|---|---|
| FR-22 | Die Anwendung wird als responsive Webanwendung umgesetzt. | Muss |
| FR-23 | Die Anwendung soll auf Desktop- und mobilen Geräten sinnvoll nutzbar sein. | Muss |
| FR-24 | Das Frontend kommuniziert über dokumentierte HTTP-API-Schnittstellen mit dem Backend. | Muss |
| FR-25 | Die Antworten des Backends müssen in einem klar definierten und einheitlichen Format vorliegen. | Muss |

### 3.5 Backend

| ID | Anforderung | Priorität |
|---|---|---|
| FR-26 | Das Backend verarbeitet Anfragen des Frontends. | Muss |
| FR-27 | Das Backend liest und verarbeitet Daten aus der Datenbank. | Muss |
| FR-28 | Das Backend führt die Entscheidungslogik aus. | Muss |
| FR-29 | Das Backend stellt Empfehlungen und zugehörige Informationen bereit. | Muss |
| FR-30 | Das Backend stellt verfügbare Standortdaten bereit. | Muss |
| FR-31 | Das Backend unterstützt die Verarbeitung externer Standortdaten. | Muss für den Projektumfang; die konkrete Importumsetzung wird geplant |
| FR-32 | Das Backend verarbeitet nur ausreichend validierte Eingaben. | Muss |
| FR-33 | Das Backend behandelt Fehler einheitlich und nachvollziehbar. | Muss |

### 3.6 Datenbank

| ID | Anforderung | Priorität |
|---|---|---|
| FR-34 | Die Datenbank speichert Kategorien und deren Hierarchie. | Muss |
| FR-35 | Die Datenbank speichert Gegenstände und deren Zuordnung zu Kategorien. | Muss |
| FR-36 | Die Datenbank speichert Fragen, Antwortmöglichkeiten, Reihenfolge und Pflichtstatus. | Muss |
| FR-37 | Die Datenbank speichert Entscheidungsregeln einschließlich Bedingungen, Priorität und Aktivstatus. | Muss |
| FR-38 | Die Datenbank speichert Empfehlungen beziehungsweise die zugehörigen Handlungsinformationen. | Muss |
| FR-39 | Die Datenbank speichert Standorttypen und Standortinformationen. | Muss |
| FR-40 | Soweit fachlich erforderlich, können Gegenstände oder Handlungen geeigneten Standorten zugeordnet werden. | Muss für die fachlich benötigten Zuordnungen |
| FR-41 | Herkunft und Aktualisierungsinformationen importierter Standortdaten können nachvollzogen werden. | Muss |

Das konkrete Datenmodell einschließlich Beziehungen, Einschränkungen und Indizes wird in `database.md` beschrieben.

### 3.7 Externe Datenquellen

| ID | Anforderung | Priorität |
|---|---|---|
| FR-42 | Geeignete offene Geodaten der Stadt Wien sollen für relevante Sammel- und Entsorgungsstandorte verwendet werden. | Muss |
| FR-43 | Insbesondere werden geeignete Daten zu Mistplätzen und Problemstoffsammelstellen geprüft und für die Integration vorgesehen. | Muss |
| FR-44 | Weitere Altstoffsammelstellen werden berücksichtigt, sofern geeignete Daten verfügbar sind. | Soll |
| FR-45 | Daten zu Reparaturwerkstätten, Repair-Cafés, Weitergabestellen und Gebrauchtwarenhändlern können ergänzt werden, sofern Datenqualität und Nutzungsbedingungen geeignet sind. | Soll |
| FR-46 | Externe Daten werden bei der Integration geprüft, aufbereitet und in ein einheitliches internes Format überführt. | Muss |
| FR-47 | Importierte Daten werden nachvollziehbar zwischengespeichert beziehungsweise in der eigenen Datenbank gespeichert. | Muss |
| FR-48 | Quelle, externe ID und Import- beziehungsweise Aktualisierungszeitpunkt werden dokumentiert, soweit technisch verfügbar und fachlich sinnvoll. | Muss |
| FR-49 | Fehlende Informationen aus externen Quellen werden als fehlend behandelt und nicht durch erfundene Werte ersetzt. | Muss |

Die Verwendung eines Datensatzes setzt voraus, dass seine Struktur, Aktualisierbarkeit, Lizenzbedingungen und Qualität geprüft wurden. Die recherchierten Quellen und ihre Einschränkungen werden in `external-data.md` dokumentiert.

### 3.8 API-Schnittstellen

| ID | Anforderung | Priorität |
|---|---|---|
| FR-50 | Alle für das Frontend vorgesehenen Endpunkte werden dokumentiert. | Muss |
| FR-51 | Für jeden Endpunkt sind Eingaben, Ausgaben und mögliche Fehlerfälle definiert. | Muss |
| FR-52 | HTTP-Statuscodes und Fehlerantworten werden einheitlich verwendet. | Muss |
| FR-53 | Die API verwendet stabile IDs und technische Codes, unabhängig von den angezeigten Übersetzungen. | Muss |
| FR-54 | Implementierte Endpunkte und lediglich geplante Endpunkte werden in der Dokumentation unterschieden. | Muss |

Die derzeit implementierten und die geplanten Endpunkte werden in `api.md` beschrieben.

### 3.9 Validierung und Fehlerbehandlung

| ID | Anforderung | Priorität |
|---|---|---|
| FR-55 | Pflichtfelder und Pflichtantworten werden geprüft. | Muss |
| FR-56 | Übermittelte IDs und Frage-Codes werden auf ihre Gültigkeit geprüft. | Muss |
| FR-57 | Fragen müssen zur ausgewählten Kategorie gehören und aktiv sein. | Muss |
| FR-58 | Antwortwerte und Antworttypen müssen zum jeweiligen Fragetyp passen. | Muss |
| FR-59 | Ungültige Eingaben werden abgelehnt und lösen keine unzulässige Empfehlung aus. | Muss |
| FR-60 | Nicht gefundene Ressourcen werden angemessen behandelt. | Muss |
| FR-61 | Der Fall ohne passende Entscheidungsregel wird ausdrücklich behandelt. | Muss |
| FR-62 | Fehlende Standortdaten und nicht verfügbare Informationen werden angemessen dargestellt. | Muss |
| FR-63 | Die Nichterreichbarkeit einer externen Datenquelle wird berücksichtigt. Vorhandene gültige lokale Daten sollen nicht ohne Not unbrauchbar werden. | Muss |
| FR-64 | Interne Serverfehler werden einheitlich behandelt. Interne Implementierungsdetails dürfen nicht unbeabsichtigt an das Frontend weitergegeben werden. | Muss |

Die konkreten HTTP-Statuscodes und das Format der Fehlerantworten werden in `api.md` und `errors.md` festgelegt.

### 3.10 Tests

| ID | Anforderung | Priorität |
|---|---|---|
| FR-65 | Die Kernfunktionen des Backends werden automatisiert getestet. | Muss |
| FR-66 | Kategorien, Fragen und Antwortmöglichkeiten werden getestet. | Muss |
| FR-67 | Gültige und ungültige Eingaben des Entscheidungssystems werden getestet. | Muss |
| FR-68 | Regelzuordnung, Prioritätsverhalten und Fälle ohne passenden Treffer werden getestet. | Muss |
| FR-69 | Empfehlungen und API-Antworten werden getestet. | Muss |
| FR-70 | Die Fehlerbehandlung und relevante HTTP-Statuscodes werden getestet. | Muss |
| FR-71 | Standortverarbeitung und Datenimport werden getestet, sobald die entsprechenden Funktionen umgesetzt sind. | Muss für die jeweiligen implementierten Funktionen |
| FR-72 | Tests sollen die Services und Controller möglichst getrennt prüfen. | Muss |

Der vorgesehene Testumfang wird in `testing.md` konkretisiert.

## 4. Nichtfunktionale Anforderungen

### 4.1 Nachvollziehbarkeit

Jede erzeugte Empfehlung soll auf nachvollziehbaren, in der Datenbank gespeicherten Entscheidungsregeln beruhen. Die Begründung muss für Nutzer*innen verständlich sein.

### 4.2 Wartbarkeit und Erweiterbarkeit

Neue Kategorien, Gegenstände, Fragen, Antwortmöglichkeiten und Regeln sollen möglichst ohne grundlegende Änderungen an der Architektur ergänzt werden können.

Die Verantwortlichkeiten von Controllern, DTOs, Services, Repositories, Entitäten und der Verarbeitung externer Daten sollen klar getrennt bleiben.

### 4.3 Datenqualität

Externe Daten müssen einer nachvollziehbaren Quelle zugeordnet werden können. Veraltete, unvollständige oder fehlerhafte Daten müssen berücksichtigt werden. Fehlende Angaben dürfen nicht als gesicherte Informationen ausgegeben werden.

### 4.4 Zuverlässigkeit

Fehlerhafte Benutzereingaben und Probleme mit externen Datenquellen dürfen nicht unkontrolliert zu falschen Empfehlungen oder unerwarteten Serverfehlern führen.

### 4.5 Testbarkeit

Die zentralen fachlichen Regeln und die wichtigsten API-Funktionen müssen mit automatisierten Tests überprüfbar sein.

### 4.6 Benutzerfreundlichkeit

Die Anwendung soll eine verständliche Benutzerführung, nachvollziehbare Empfehlungen und eine klare Darstellung verfügbarer Standortinformationen bieten.

### 4.7 Schnittstellenkonsistenz

API-Endpunkte, DTOs, Statuscodes und Fehlerantworten müssen konsistent dokumentiert und zwischen Frontend und Backend abgestimmt werden.

### 4.8 Datenschutz und Sicherheit

Es sollen nur Daten verarbeitet werden, die für die vorgesehenen Funktionen erforderlich sind. Standortbezogene Funktionen müssen die vom Benutzer gewählte oder ausdrücklich freigegebene Position verwenden. Die Anwendung soll Eingaben validieren und interne technische Details nicht unnötig offenlegen.

## 5. Zusatzanforderungen und optionale Funktionen

Die folgenden Funktionen sind optional. Sie dürfen erst umgesetzt werden, wenn die Kernanforderungen ausreichend erfüllt sind.

### 5.1 Posting-Funktion

Eine einfache Posting-Funktion könnte folgende Möglichkeiten bieten:

- Foto eines Gegenstands hochladen
- Beschreibung hinzufügen
- Zustand oder Defekt angeben
- Abholung oder Lieferung auswählen
- Verschenken oder Reparatur anbieten
- Interesse an einem Posting zeigen

Diese Funktion erweitert den Kernumfang und ist nicht Voraussetzung für das grundlegende Entscheidungssystem.

### 5.2 Automatische Bilderkennung

Ein Foto könnte automatisch analysiert werden, um eine passende Kategorie vorzuschlagen. Die automatische Bilderkennung ist nicht Bestandteil des Kernsystems.

### 5.3 Weitere optionale Funktionen

- Routenplanung
- Favoriten
- automatische Prüfung von Öffnungszeiten
- Administrationsbereich
- Bereich „Wertstoff-Wissen Wien / Gut zu wissen“

Die Umsetzung erfolgt nur, wenn ausreichend Zeit zur Verfügung steht und die Kernfunktionen funktionieren.

## 6. Nicht-Ziele

Folgende Funktionen gehören ausdrücklich nicht zum Kernumfang:

- native Android-App
- native iOS-App
- vollständiger Online-Marktplatz
- Chat-System
- Zahlungsabwicklung
- Versandabwicklung
- Vermittlung oder Abschluss von Verträgen
- rechtsverbindliche Beratung
- vollständige Abdeckung aller externen Standortdaten
- automatische Bilderkennung als Kernfunktion
- vollständiges Routing als Kernfunktion

## 7. Technische Rahmenbedingungen

Das Projekt wird als Client-Server-Anwendung mit einer Weboberfläche, einem Backend und einer relationalen Datenbank umgesetzt.

Die grundlegende Architektur lautet:

```text
Frontend
   |
   | HTTP / API
   v
Backend
   |
   +----------> PostgreSQL-Datenbank
   |
   +----------> Externe Datenquellen
```

Für das Backend sind Spring Boot und Java vorgesehen. Als Datenbank wird PostgreSQL verwendet. Der Datenzugriff erfolgt über die im Projekt vorgesehene Persistenzschicht.

Das Backend stellt dokumentierte REST-API-Endpunkte bereit. Die konkreten Verantwortlichkeiten und Datenflüsse werden in `architecture.md` beschrieben.

Der Prototyp konzentriert sich zunächst auf Wien und ausgewählte Kategorien. Die Anwendung soll in einer geeigneten Serverumgebung bereitgestellt werden.

## 8. Projektumfang und Ergebnisse

Der geplante Gesamtumfang umfasst:

- responsives Webfrontend
- Backend
- Datenbank und Datenmodell
- datenbankgestütztes Entscheidungssystem
- dokumentierte API-Schnittstellen
- Verarbeitung ausgewählter externer Standortdaten
- Standortsuche und Kartenansicht
- automatisierte Tests
- Serverbetrieb und Deployment
- technische Dokumentation
- Projektdokumentation
- Projektpräsentation

Die vollständige Umsetzung aller Punkte ist nicht automatisch durch dieses Dokument bestätigt. Der tatsächliche Umsetzungsstand muss anhand des Codes, der Tests und der Dokumentation geprüft werden.

## 9. Abgrenzung der Projektphasen

### Phase 1 – Analyse und Basisdesign

**Zeitraum: 01.10.2026 bis 31.10.2026**

Ziele dieser Phase sind:

- Anforderungen sammeln, strukturieren und abstimmen
- Backend-Anforderungen konkretisieren
- Zielarchitektur und Verantwortlichkeiten definieren
- Datenmodell und Beziehungen entwerfen
- Entscheidungslogik und Prioritätsverhalten spezifizieren
- API-Verträge einschließlich Fehlerfällen dokumentieren
- externe Datenquellen untersuchen und ihre Eignung bewerten
- Teststrategie und Fehlerbehandlung planen
- technische Dokumentation erstellen und offene Entscheidungen festhalten

### Phase 2 – Datenbankdesign

**Zeitraum: 01.10.2026 bis 31.10.2026**

Ziele dieser Phase sind:

- Entitäten und Beziehungen fachlich prüfen
- Primär- und Fremdschlüssel festlegen
- Datenintegrität und erforderliche Constraints definieren
- Indizes und geeignete Datentypen planen
- Speicherung von Entscheidungsregeln und Standortdaten abstimmen
- das Datenmodell mit API und Entscheidungslogik abgleichen

Die Zeiträume beider Phasen überschneiden sich. Die Aufgaben sollen deshalb abgestimmt und gemeinsam bearbeitet werden.

### Abgrenzung zur Implementierung

Ein vorhandener Prototyp kann bereits Teile der geplanten Funktionen enthalten. Das bedeutet nicht automatisch, dass alle Anforderungen fachlich abgestimmt, vollständig implementiert oder getestet sind.

Insbesondere die endgültige Prioritätsregel, der Umgang mit fehlenden Treffern, der genaue Umfang der Standortsuche und die Datenimportstrategie müssen vor einer abschließenden Freigabe geklärt werden.

## 10. Offene Entscheidungen vor der Freigabe

Die folgenden Punkte müssen vom Projektteam abgestimmt und anschließend in den zugehörigen Dokumenten einheitlich festgehalten werden:

1. **Numerische Regelpriorität:** Bedeutet ein kleinerer oder ein größerer Zahlenwert eine höhere Priorität?
2. **Mehrere passende Regeln:** Wie werden Gleichstände behandelt, und soll die Spezifität einer Regel berücksichtigt werden?
3. **Entscheidung ohne Treffer:** Welche HTTP-Antwort und welches Antwortformat werden verwendet, wenn keine Regel passt?
4. **Gegenstand bei einer Entscheidung:** Ist `itemId` optional, oder ist eine Gegenstandsauswahl vor jeder Entscheidung erforderlich?
5. **API-Vertrag:** Welche Felder sind endgültig Bestandteil der öffentlichen Empfehlung, beispielsweise `priority`?
6. **Standortsuche:** Welche Filter, Entfernungskriterien und Informationen können mit den tatsächlich verfügbaren Daten zuverlässig angeboten werden?
7. **Externe Daten:** Welche Datensätze werden verbindlich importiert und wie oft werden sie aktualisiert?
8. **Optionale Funktionen:** Welche Zusatzfunktionen sind angesichts der verfügbaren Zeit realistisch?

Die Entscheidungen sind mit `api.md`, `database.md`, `decision-logic.md`, `external-data.md` und `architecture.md` abzugleichen.

## 11. Abnahmekriterien für den Kernumfang

Der Kernumfang gilt fachlich als ausreichend vorbereitet, wenn:

- die Anforderungen und Prioritäten nachvollziehbar dokumentiert sind;
- die vorgesehene Architektur und das Datenmodell abgestimmt sind;
- die Entscheidungslogik einschließlich Prioritätsverhalten und Fehlerfällen eindeutig beschrieben ist;
- API-Eingaben, Ausgaben und Fehlerantworten dokumentiert sind;
- geeignete externe Datenquellen und ihre Einschränkungen dokumentiert sind;
- ein Testkonzept für die Kernfunktionen vorliegt;
- offene Entscheidungen sichtbar dokumentiert und Verantwortlichkeiten für deren Klärung festgelegt sind.

Die spätere technische Abnahme erfordert zusätzlich eine Überprüfung der Implementierung, erfolgreiche relevante Tests und die Prüfung des tatsächlichen Betriebsverhaltens.

---

**Hinweis:** Dieses Dokument beschreibt die fachlichen und technischen Anforderungen sowie den geplanten Projektumfang. Es ist keine Bestätigung, dass alle genannten Funktionen bereits implementiert sind. Die endgültige Fassung muss vom Projektteam geprüft und freigegeben werden.