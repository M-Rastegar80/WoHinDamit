# WoHinDamit – Decision Logic und Decision Flow

**Status:** Fachlicher Entwurf für die Teamabstimmung
**Projektphase:** Phase 1 – Analyse und Basisdesign
**Stand:** Oktober 2026

## 1. Ziel

Die Entscheidungslogik bestimmt anhand der Kategorie, des ausgewählten Gegenstands und seiner Eigenschaften, welche Handlung für diesen Gegenstand empfohlen werden soll.

Die Entscheidungsregeln werden grundsätzlich in der Datenbank gespeichert und gepflegt. Die Java-Anwendung implementiert die allgemeine Auswertung der Regeln, die Validierung der Antworten und die Auswahl einer passenden Empfehlung.

Das System soll eine nachvollziehbare Empfehlung mit einer verständlichen Begründung liefern.

Neue Regeln sollen möglichst ohne Änderungen an der grundlegenden Entscheidungslogik ergänzt werden können.

Dieses Dokument unterscheidet zwischen dem aktuellen Prototyp und dem fachlichen Zielzustand. Die Beispiele in diesem Dokument sind fachliche Entwürfe und keine bereits freigegebenen Produktionsregeln.

## 2. Grundprinzip

Die Entscheidungslogik folgt diesem Ablauf:

```text
Gegenstand oder Kategorie auswählen
              |
              v
Aktive Fragen laden
              |
              v
Antwortoptionen anzeigen
              |
              v
Antworten erfassen
              |
              v
Antworten validieren und normalisieren
              |
              v
Aktive Regeln aus der Datenbank laden
              |
              v
Kategorie- und Gegenstandsbezug prüfen
              |
              v
Regelbedingungen auswerten
              |
              v
Passende Regel auswählen
              |
              v
Recommendation und Begründung zurückgeben
```

Die eigentliche fachliche Entscheidung wird im Backend getroffen. Das Frontend stellt Fragen und Antworten dar und zeigt das Ergebnis an.

## 3. Mögliche Aktionen

Folgende Aktionen kommen für das System grundsätzlich infrage:

| Action | Bedeutung |
|---|---|
| `REPAIR` | Reparieren |
| `SELL` | Verkaufen |
| `GIVE_AWAY` | Weitergeben |
| `DONATE` | Spenden |
| `RECYCLE` | Recyceln |
| `DISPOSE` | Fachgerecht entsorgen |

Die endgültige Liste muss mit dem fachlichen Projektumfang und dem vorhandenen `Action`-Enum abgestimmt werden.

Je nach Gegenstand und Zustand können auch mehrere Handlungswege sinnvoll sein. Die Anwendung darf nicht automatisch eine Handlung empfehlen, wenn die Bedingungen dafür nicht erfüllt sind.

## 4. Eingaben der Decision Engine

Der Entscheidungs-Endpunkt des aktuellen Prototyps verwendet `POST /api/decisions`.

### Beispielrequest

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

### Felder

| Feld | Bedeutung |
|---|---|
| `categoryId` | Erforderliche ID der Kategorie |
| `itemId` | Optionale ID eines konkreten Gegenstands |
| `answers` | Map aus Fragecodes und den zugehörigen Antwortwerten |

Die Fragecodes müssen den technischen Codes aktiver Fragen der angefragten Kategorie entsprechen.

Beispiele für stabile Fragecodes:

- `functional`
- `repairable`
- `complete`
- `stillUsable`

Die endgültigen Fragecodes werden im Datenmodell definiert. Anzeigetexte dürfen nicht als technische Schlüssel verwendet werden, da sie sich ändern oder übersetzt werden können.

## 5. Antwortvalidierung

Bevor Regeln ausgewertet werden, prüft das Backend die Antworten.

### 5.1 Kategorie

`categoryId` muss im Request vorhanden sein.

Der aktuelle Prototyp prüft an dieser Stelle, ob die ID angegeben wurde. Eine vollständige Prüfung der Existenz und Aktivität der Kategorie muss bei der weiteren Implementierung sichergestellt werden.

### 5.2 Fragecodes

Alle übermittelten Antwortschlüssel müssen zu aktiven Fragen der ausgewählten Kategorie gehören.

Eine unbekannte Frage führt zu `400 Bad Request`.

Beispiel einer ungültigen Antwort:

```json
{
  "repairable": true,
  "unknownQuestion": true
}
```

### 5.3 Pflichtfragen

Fragen mit `required = true` müssen beantwortet werden.

Fehlt eine erforderliche Antwort oder ist ihr Wert `null`, wird der Request abgelehnt.

Beispiel einer Fehlerantwort:

```json
{
  "error": "Answer required for question: functional"
}
```

### 5.4 Antworttypen

Die Werte müssen zum `answer_type` der jeweiligen Frage passen.

Der aktuelle Prototyp unterstützt folgende Typgruppen:

| Antworttyp | Zulässige Werte |
|---|---|
| `BOOLEAN`, `YES_NO`, `TRUE_FALSE` | Boolean `true` oder `false`; die Strings `"true"` und `"false"` werden normalisiert |
| `INTEGER` | Ganze Zahlen |
| `NUMBER`, `DECIMAL` | Numerische Werte |
| `TEXT`, `STRING` | Zeichenketten |
| `SINGLE_CHOICE`, `SELECT` | Einzelner konfigurierter Optionswert |
| `MULTIPLE_CHOICE`, `MULTI_SELECT` | Liste konfigurierter Optionswerte |

Diese Typnamen sind vor der endgültigen Freigabe mit dem Datenmodell und den tatsächlich verwendeten Werten in der Datenbank abzugleichen.

Ein nicht unterstützter oder ungültiger Antworttyp führt zu `400 Bad Request`.

### 5.5 Auswahloptionen

Bei `SINGLE_CHOICE` und `SELECT` muss der übermittelte Wert in den Antwortoptionen der jeweiligen Frage existieren.

Bei `MULTIPLE_CHOICE` und `MULTI_SELECT` muss jeder Wert in den konfigurierten Optionen der Frage enthalten sein.

Beispiel:

Konfigurierte Optionen:

```json
[
  {
    "value": "donate",
    "label": "Spenden"
  },
  {
    "value": "sell",
    "label": "Verkaufen"
  }
]
```

Der Wert `"donate"` ist gültig. Der Wert `"repair"` ist ungültig, sofern er für diese Frage nicht konfiguriert wurde.

Antwortoptionen werden anhand ihres maschinenlesbaren `value` validiert. Das sichtbare `label` ist für die Darstellung im Frontend vorgesehen.

## 6. Regeln in der Datenbank

Eine Rule beschreibt, unter welchen Bedingungen eine Recommendation ausgewählt werden darf.

Im aktuellen Datenmodell gehören dazu insbesondere:

| Feld | Bedeutung |
|---|---|
| `id` | ID der Regel |
| `category_id` | Optionaler Kategoriebezug |
| `item_id` | Optionaler Bezug zu einem konkreten Gegenstand |
| `conditions` | Bedingungen der Regel |
| `recommendation_id` | Zugehörige Empfehlung |
| `priority` | Priorität der Regel |
| `reason` | Begründung für die Empfehlung |
| `active` | Aktivierungsstatus |

Die Bedingungen werden im aktuellen Prototyp als Map im Java-Modell und als JSON/JSONB in PostgreSQL abgebildet.

### Beispiel einer Bedingung

```json
{
  "repairable": true,
  "functional": false
}
```

Die Schlüssel entsprechen den Fragecodes. Die Werte werden mit den validierten und normalisierten Antworten verglichen.

Eine Regel darf nicht ausschließlich anhand eines Fragetextes identifiziert werden.

## 7. Ablauf der Regelauswertung

Nach erfolgreicher Validierung wird die Regelauswertung durchgeführt.

1. Aktive Regeln werden aus der Datenbank geladen.
2. Regeln mit unpassendem Kategoriebezug werden ausgeschlossen.
3. Regeln mit einem konkreten Gegenstandsbezug werden ausgeschlossen, wenn der Request nicht denselben Gegenstand angibt.
4. Alle Bedingungen einer verbleibenden Regel werden mit den normalisierten Antworten verglichen.
5. Nur Regeln, deren Bedingungen vollständig erfüllt sind, gelten als passend.
6. Bei mehreren passenden Regeln wird die Prioritätskonvention angewendet.
7. Aus der ausgewählten Regel wird die zugehörige Recommendation zurückgegeben.

Eine Regel mit mehreren Bedingungen gilt im aktuellen Prototyp nur dann als passend, wenn alle Bedingungen erfüllt sind.

### Umgang mit allgemeinen Regeln

Regeln können einen Kategoriebezug, einen Gegenstandsbezug oder beide enthalten.

Der aktuelle Prototyp behandelt `category_id = NULL` als fehlenden Kategorie-Scope und `item_id = NULL` als fehlenden Gegenstands-Scope.

Die fachliche Bedeutung allgemeiner Regeln muss verbindlich festgelegt werden. Insbesondere muss geklärt werden, ob eine Regel ohne Kategorie- und Gegenstandsbezug global gelten darf.

## 8. Beispielregeln

Die folgenden Beispiele dienen der fachlichen Diskussion. Sie sind keine freigegebenen Produktionsregeln.

### Regel A – Reparatur

**Kategorie:** Fahrräder

**Bedingung:**

```json
{
  "repairable": true
}
```

**Mögliche Aktion:** `REPAIR`

**Begründung:**

„Der Gegenstand kann repariert und weiterverwendet werden.“

### Regel B – Weitergabe oder Spende

**Kategorie:** Fahrräder

**Bedingungen:**

```json
{
  "repairable": false,
  "stillUsable": true
}
```

**Mögliche Aktionen:** `GIVE_AWAY`, `DONATE` oder `SELL`

**Begründung:**

„Der Gegenstand ist nicht reparierbar, kann aber noch verwendet werden.“

Die konkrete Auswahl hängt von den fachlichen Kriterien ab, die das Team noch definieren muss.

### Regel C – Recycling

**Kategorie:** Elektrogeräte

**Bedingungen:**

```json
{
  "functional": false,
  "repairable": false
}
```

**Mögliche Aktion:** `RECYCLE`

**Begründung:**

„Eine Reparatur ist nicht möglich. Es soll eine geeignete Sammel- oder Recyclingstelle ermittelt werden.“

Die tatsächliche Empfehlung muss auch die Eigenschaften des Geräts und die verfügbaren fachlichen Entsorgungsinformationen berücksichtigen.

### Regel D – Fachgerechte Entsorgung

**Bedingungen:**

```json
{
  "repairable": false,
  "stillUsable": false,
  "recyclable": false
}
```

**Mögliche Aktion:** `DISPOSE`

**Begründung:**

„Für den Gegenstand ist eine geeignete fachgerechte Entsorgung erforderlich.“

Auch diese Regel muss mit den fachlichen Anforderungen und den geltenden Entsorgungsmöglichkeiten abgestimmt werden.

## 9. Priorität und Konfliktauflösung

Die Priorität bestimmt, welche Regel ausgewählt wird, wenn mehrere Regeln gleichzeitig passen.

**Wichtige offene Inkonsistenz:**

Im älteren fachlichen Entwurf wurde festgelegt:

- Priorität `1` = höchste Priorität
- Priorität `2` = zweite Priorität
- Priorität `3` = dritte Priorität
- Priorität `4` = niedrigste Priorität

Der aktuelle Java-Prototyp sortiert dagegen `Rule.priority` absteigend. Das bedeutet, dass dort ein höherer numerischer Wert gewinnt.

Beide Konventionen widersprechen einander.

**Vor der Freigabe muss das Team eine verbindliche Konvention auswählen.** Anschließend müssen Implementierung, Tests und Dokumentation dieselbe Konvention verwenden.

### Regeln mit gleicher Priorität

Die endgültige Konfliktauflösung bei gleicher Priorität ist noch festzulegen.

Ein möglicher Ansatz ist:

1. Regeln mit konkretem Gegenstandsbezug gegenüber allgemeineren Regeln bevorzugen.
2. Bei weiterhin gleichwertigen Regeln eine deterministische Sortierung verwenden.
3. Falls mehrere Regeln fachlich widersprüchlich sind, die Konfiguration prüfen oder einen eindeutig definierten Fehlerfall auslösen.

Die Bevorzugung spezifischerer Regeln ist im aktuellen Prototyp noch nicht implementiert und darf nicht als bestehendes Verhalten betrachtet werden.

## 10. Ergebnis der Decision Engine

Der aktuelle Prototyp gibt bei einem erfolgreichen Treffer folgende Felder zurück:

```json
{
  "recommendationId": 1,
  "action": "REPAIR",
  "title": "Repair item",
  "description": "Test recommendation",
  "reason": "The item can be repaired"
}
```

Die Felder bedeuten:

| Feld | Bedeutung |
|---|---|
| `recommendationId` | ID der ausgewählten Recommendation |
| `action` | Empfohlene Handlung |
| `title` | Titel der Empfehlung |
| `description` | Beschreibung der Empfehlung |
| `reason` | Begründung aus der ausgewählten Rule |

Das Beispiel verwendet Testdaten aus dem aktuellen Prototyp.

Die Felder `priority` und `matchedRuleId` gehören derzeit nicht zur öffentlichen Response. Ob sie künftig ergänzt werden, ist im API-Design festzulegen.

Die Begründung soll verständlich sein und erklären, warum eine Regel zutrifft. Sie darf nicht nur aus einer internen Regel-ID bestehen.

## 11. Keine passende Regel

Wenn keine Regel passt, darf die Anwendung keine Empfehlung erfinden.

Der aktuelle Controller gibt `404 Not Found` ohne Response-Body zurück.

Ein älterer fachlicher Vorschlag sah stattdessen eine explizite Antwort ohne Aktion vor:

```json
{
  "action": null,
  "reason": "Für diesen Gegenstand konnte keine passende Regel gefunden werden."
}
```

Diese Antwort ist im aktuellen Prototyp noch nicht implementiert.

Das Team muss entscheiden, welches Verhalten künftig verbindlich gilt:

- `404 Not Found`;
- `422 Unprocessable Content`;
- oder eine explizite Antwort mit einem Status wie `NO_MATCH`.

Die gewählte Variante muss in der API-Dokumentation, im Controller und in den Tests einheitlich umgesetzt werden.

## 12. Schutz vor ungültiger Regelkonfiguration

Die Anwendung soll langfristig auch ungültige oder widersprüchliche Regeln erkennen können.

Zu prüfen sind insbesondere:

- unbekannte Fragecodes in `conditions`;
- Bedingungen mit inkompatiblen Werttypen;
- Verweise auf inaktive Fragen oder ungültige Empfehlungen;
- unbeabsichtigt leere Bedingungen;
- widersprüchliche Regeln mit gleicher Priorität;
- fehlende oder unvollständige Begründungen;
- ungültige Kombinationen aus Kategorie- und Gegenstandsbezug.

Der aktuelle Prototyp validiert vor allem die Benutzerantworten. Eine umfassende Prüfung der Regelkonfiguration ist noch als Erweiterung einzuplanen.

## 13. Erweiterbarkeit

Die Entscheidungslogik soll neue Kategorien, Gegenstände, Fragen, Antwortoptionen und Regeln unterstützen, ohne dass für jede neue fachliche Regel ein neuer Java-Sonderfall geschrieben werden muss.

Die allgemeine Auswertung bleibt im Backend. Die konkreten Regelbedingungen, Prioritäten, Empfehlungen und Begründungen werden in der Datenbank gepflegt.

Neue Fragetypen oder komplexere Bedingungsoperatoren müssen dagegen bewusst in der technischen Implementierung ergänzt und getestet werden.

## 14. Tests

Die Decision Engine wird durch Unit-Tests, Controller-Tests und manuelle API-Tests geprüft.

Bereits getestete Fälle des Prototyps umfassen:

- passende Regel und erfolgreiche Empfehlung;
- keine passende Regel;
- Auswahl der Regel mit höherem numerischem Prioritätswert;
- Ausschluss von Regeln mit abweichender Kategorie;
- Ausschluss von Regeln mit abweichendem Gegenstand;
- fehlende Pflichtantwort;
- unbekannter Fragecode;
- ungültiger Boolean-Wert;
- Normalisierung von `"true"` zu `true`;
- Auswahlwert, der nicht in den konfigurierten Optionen existiert.

Weitere fachliche Tests müssen folgen, sobald das Team die Prioritätskonvention, das No-Match-Verhalten und die endgültigen Entscheidungsszenarien festgelegt hat.

Für die Standortempfehlung sind zusätzliche Tests erforderlich, sobald die Location-API und die Eignungslogik implementiert sind.

## 15. Aktueller Prototyp und Zielzustand

Der Prototyp besitzt bereits eine datenbankgestützte Decision Engine. Sie validiert Antworten, lädt aktive Fragen und Regeln, prüft Kategorie- und Gegenstandsbezüge, wertet JSON-Bedingungen aus und gibt eine Empfehlung zurück.

Noch nicht als vollständig umgesetzt zu betrachten sind:

- eine verbindlich abgestimmte Prioritätskonvention;
- die Priorisierung spezifischerer Regeln bei gleicher Priorität;
- eine einheitliche No-Match-Response;
- eine vollständige Prüfung fehlerhafter Regelkonfigurationen;
- die integrierte Bestimmung geeigneter Standorte;
- die Freigabe der Beispielregeln als fachlich gültige Produktionsregeln.

Der Prototyp ist somit eine technische Grundlage, aber noch kein Nachweis dafür, dass alle fachlichen Anforderungen vollständig erfüllt sind.

## 16. Zusammenfassung

Die Decision Engine kombiniert Kategorie, Gegenstand und validierte Antworten mit den aktiven, in der Datenbank gepflegten Regeln.

Nur Regeln, deren Gültigkeitsbereich passt und deren Bedingungen vollständig erfüllt sind, dürfen eine Empfehlung erzeugen.

Die Ausgabe enthält eine Handlung, einen Titel, eine Beschreibung und eine nachvollziehbare Begründung. Wenn keine Regel passt, wird keine Empfehlung erfunden.

Vor der endgültigen Freigabe müssen insbesondere die Prioritätskonvention, die Behandlung gleichwertiger Regeln und das No-Match-Verhalten mit dem Team abgestimmt werden. Danach sind Implementierung, API-Vertrag und Tests entsprechend zu vereinheitlichen.
