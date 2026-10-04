WoHinDamit? – Decision Logic
1. Ziel

Die Entscheidungslogik bestimmt anhand des ausgewählten Gegenstands und seiner Eigenschaften, welche Handlung für den Gegenstand empfohlen wird.

Die Entscheidung basiert auf Regeln, die in der Datenbank gespeichert und gepflegt werden.

Die Anwendung soll eine nachvollziehbare Empfehlung und eine Begründung liefern.

2. Priorität

Die grundlegende Priorität der Empfehlungen lautet:

1. Reparieren
2. Verkaufen / Weitergeben / Spenden
3. Recyceln / fachgerecht Entsorgen


Die Priorität bedeutet nicht, dass immer automatisch repariert werden muss.

Die tatsächlichen Bedingungen der jeweiligen Regel bestimmen, ob eine Empfehlung geeignet ist.

3. Mögliche Empfehlungen

Das System verwendet folgende grundlegende Aktionen:

Action	Bedeutung
REPAIR	Reparieren
SELL	Verkaufen
GIVE_AWAY	Weitergeben
DONATE	Spenden
RECYCLE	Recyceln
DISPOSE	Fachgerecht entsorgen
4. Eingaben

Die Entscheidungslogik benötigt insbesondere:

ausgewählten Gegenstand

Kategorie

Antworten auf Zustandsfragen

weitere relevante Eigenschaften

Beispiel:

Gegenstand: Fahrrad

Kategorie: Fahrräder

Antworten:
- funktionsfähig = nein
- reparierbar = ja
- vollständig = ja

5. Entscheidungsablauf

Der grundsätzliche Ablauf:

User selects item
        |
        v
Determine category
        |
        v
Load relevant questions
        |
        v
User answers questions
        |
        v
Load active rules
        |
        v
Evaluate rules
        |
        v
Select best matching recommendation
        |
        v
Return recommendation + reason

6. Regelprinzip

Regeln werden nicht ausschließlich im Quellcode definiert.

Die Regeln sollen in der Datenbank gespeichert werden.

Eine Regel besteht grundsätzlich aus:

Kategorie

Bedingungen

Empfehlung

Priorität

Begründung

Aktivierungsstatus

Beispiel:

Kategorie: Fahrrad

Bedingung:
reparierbar = true

Empfehlung:
REPAIR

Priorität:
1

Begründung:
Das Fahrrad kann repariert werden.

7. Beispielregeln
Regel 1 – Reparatur
IF
reparierbar = true

THEN
REPAIR


Begründung:

Der Gegenstand kann repariert und weiterverwendet werden.

Regel 2 – Weitergabe / Spende
IF
reparierbar = false
AND
noch_verwendbar = true

THEN
GIVE_AWAY / DONATE / SELL


Begründung:

Der Gegenstand ist nicht reparierbar, kann aber noch verwendet werden.


Die konkrete Auswahl zwischen Verkaufen, Weitergeben und Spenden kann von Gegenstand und weiteren Eigenschaften abhängen.

Regel 3 – Recycling
IF
reparierbar = false
AND
noch_verwendbar = false
AND
recycelbar = true

THEN
RECYCLE


Begründung:

Der Gegenstand ist nicht mehr sinnvoll verwendbar und sollte recycelt werden.

Regel 4 – Fachgerechte Entsorgung
IF
reparierbar = false
AND
noch_verwendbar = false
AND
recycelbar = false

THEN
DISPOSE


Begründung:

Der Gegenstand sollte fachgerecht entsorgt werden.

8. Regelpriorität

Wenn mehrere Regeln gleichzeitig zutreffen, wird die Priorität berücksichtigt.

Beispiel:

Priority 1 → REPAIR
Priority 2 → SELL / GIVE_AWAY / DONATE
Priority 3 → RECYCLE
Priority 4 → DISPOSE


Die Regel mit der höchsten fachlichen Priorität soll bevorzugt werden.

9. Nachvollziehbarkeit

Jede Empfehlung muss eine verständliche Begründung zurückgeben.

Beispiel:

{
  "action": "REPAIR",
  "reason": "Der Gegenstand ist reparierbar.",
  "priority": 1
}


Damit kann das Frontend dem Benutzer erklären, warum diese Empfehlung ausgewählt wurde.

10. Keine passende Regel

Wenn keine Regel passt, darf das System keine erfundene Empfehlung erzeugen.

Stattdessen soll eine geeignete Fallback-Antwort zurückgegeben werden.

Beispiel:

{
  "action": null,
  "reason": "Für diesen Gegenstand konnte keine passende Regel gefunden werden."
}

11. Erweiterbarkeit

Die Entscheidungslogik soll so aufgebaut werden, dass neue Kategorien und Regeln hinzugefügt werden können, ohne die gesamte Anwendung ändern zu müssen.

Neue Regeln sollen möglichst über die Datenbank gepflegt werden können.

12. Grundprinzip

Die Entscheidungslogik folgt dem Prinzip:

Gegenstand
    +
Kategorie
    +
Zustand
    +
weitere Eigenschaften
    |
    v
Datenbankregeln
    |
    v
beste passende Regel
    |
    v
Empfehlung
    +
Begründung

13. Technische Festlegungen

13.1 Speicherung der Regeln

Die Entscheidungsregeln werden in der PostgreSQL-Datenbank gespeichert.
Sie werden nicht ausschließlich im Java-Quellcode definiert.

13.2 Bedingungen

Die Bedingungen einer Regel werden als JSONB gespeichert.

Beispiel:

{
"repairable": true,
"functional": false
}

13.3 Ermittlung passender Regeln

Bei einer Anfrage werden nur aktive Regeln berücksichtigt.

Eine Regel passt, wenn:

- die Kategorie des Gegenstands zur Kategorie der Regel passt, sofern eine Kategorie angegeben ist,
- der konkrete Gegenstand zur Regel passt, sofern ein Item angegeben ist,
- alle Bedingungen der Regel erfüllt sind.

13.4 Priorität

Wenn mehrere Regeln passen, wird die Regel mit der höheren Priorität bevorzugt.

Dabei gilt:

1 = höchste Priorität
2 = zweite Priorität
3 = dritte Priorität
4 = niedrigste Priorität

Bei gleicher Priorität wird eine spezifischere Regel bevorzugt.
Eine Regel für einen konkreten Gegenstand ist spezifischer als eine allgemeine Kategorie-Regel.

13.5 Ergebnis

Die ausgewählte Regel liefert:

- action
- reason
- priority

Beispiel:

{
"action": "REPAIR",
"reason": "Der Gegenstand ist reparierbar.",
"priority": 1
}

13.6 Keine passende Regel

Wenn keine passende Regel gefunden wird, wird keine Empfehlung erfunden.

Stattdessen wird ein Fallback-Ergebnis zurückgegeben:

{
"action": null,
"reason": "Für diesen Gegenstand konnte keine passende Regel gefunden werden."
}

13.7 Erweiterbarkeit

Neue Kategorien, Gegenstände und Regeln sollen über die Datenbank ergänzt werden können,
ohne die grundlegende Entscheidungslogik im Backend ändern zu müssen.