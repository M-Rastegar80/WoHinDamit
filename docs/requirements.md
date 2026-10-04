WoHinDamit? – Requirements
1. Projektübersicht

Projektname: WoHinDamit? – Digitale Recycling- und Weitergabehilfe für Wien

Schuljahr: 2026/27

Projektart: Webbasierter Prototyp

Ziel:
Die Anwendung unterstützt Nutzer*innen in Wien dabei, für alte, defekte oder nicht mehr benötigte Gegenstände eine sinnvolle Lösung zu finden.

Abhängig von Gegenstand und Zustand soll das System eine nachvollziehbare Empfehlung geben.

Die grundsätzliche Priorität lautet:

Reparieren

Verkaufen / Weitergeben / Spenden

Recyceln / fachgerecht Entsorgen

2. Zielgruppe

Die Anwendung richtet sich an Personen in Wien, die nicht wissen, was sie mit einem alten, defekten oder nicht mehr benötigten Gegenstand tun sollen.

Die Benutzerführung soll einfach und verständlich sein und ohne spezielle technische Kenntnisse funktionieren.

3. MUSS-Anforderungen
3.1 Benutzerführung und Entscheidungssystem

Die Benutzer*innen müssen:

einen Gegenstand über eine Textsuche auswählen können

einen Gegenstand über eine Kategorie auswählen können

den Zustand des Gegenstands über einfache Fragen beschreiben können

eine nachvollziehbare Empfehlung erhalten

eine Begründung für die Empfehlung erhalten

Das Entscheidungssystem verwendet eigene, in der Datenbank gepflegte Regeln.

Die Regeln berücksichtigen insbesondere:

Gegenstand

Kategorie

Zustand

Reparierbarkeit

weitere relevante Eigenschaften

3.2 Priorität der Empfehlungen

Das System soll Empfehlungen nach folgender Priorität prüfen:

Reparieren

Verkaufen / Weitergeben / Spenden

Recyceln / fachgerecht Entsorgen

Die konkrete Empfehlung hängt vom Gegenstand und seinen Eigenschaften ab.

3.3 Standortdaten

Die Anwendung muss geeignete Standorte anzeigen können.

Dazu gehören insbesondere:

Mistplätze

Sammelstellen

Reparaturwerkstätten

Repair-Cafés

Weitergabestellen

Gebrauchtwarenhändler

Für Fahrzeuge können geeignete Gebrauchtwagenhändler berücksichtigt werden.

Die Standortdaten sollen nach folgenden Kriterien verarbeitet werden:

Eignung für den jeweiligen Gegenstand

Entfernung zum Benutzer bzw. zur gewählten Position

Für einen Standort sollen, soweit verfügbar, folgende Informationen angezeigt werden:

Name

Adresse

Kontaktinformationen

Standort auf einer Karte

Standorttyp

Fehlende Informationen müssen als nicht verfügbar gekennzeichnet werden.

3.4 Webanwendung

Die Anwendung muss als responsive Webanwendung umgesetzt werden.

Das Frontend muss über dokumentierte Schnittstellen mit dem Backend kommunizieren.

Die Anwendung soll auf Desktop- und mobilen Geräten sinnvoll nutzbar sein.

3.5 Backend

Das Backend muss:

Anfragen des Frontends verarbeiten

Daten aus der Datenbank lesen und verarbeiten

die Entscheidungslogik ausführen

Empfehlungen erzeugen

Standortdaten bereitstellen

externe Standortdaten verarbeiten

Fehler behandeln

validierte Eingaben verarbeiten

3.6 Datenbank

Die Datenbank muss unter anderem folgende Informationen speichern können:

Kategorien

Gegenstände

Fragen

mögliche Antworten bzw. Zustände

Entscheidungsregeln

Empfehlungen

Standortdaten

Standorttypen

erforderliche Zusatzinformationen

Die Entscheidungsregeln müssen in der Datenbank gepflegt werden können.

3.7 Externe Daten

Geeignete offene Daten der Stadt Wien sollen eingebunden werden.

Insbesondere sollen Daten zu:

Mistplätzen

Sammelstellen

berücksichtigt werden.

Falls die Datenqualität ausreichend ist, können zusätzlich Daten zu:

Reparaturwerkstätten

Repair-Cafés

Weitergabestellen

Gebrauchtwarenhändlern

Gebrauchtwagenhändlern

eingebunden werden.

Externe Daten sollen:

abgerufen

aufbereitet

normalisiert

zwischengespeichert

werden.

3.8 Schnittstellen

Die Kommunikation zwischen Frontend und Backend erfolgt über dokumentierte Schnittstellen.

Die Schnittstellen müssen klar definierte:

Eingaben

Ausgaben

Fehlerfälle

besitzen.

3.9 Validierung und Fehlerbehandlung

Benutzereingaben müssen validiert werden.

Das System muss geeignete Fehlerfälle behandeln, zum Beispiel:

ungültige Eingaben

fehlende Daten

nicht gefundene Gegenstände

keine passenden Standorte

externe Datenquelle nicht erreichbar

interne Serverfehler

3.10 Tests

Die Kernfunktionen müssen getestet werden.

Dazu gehören insbesondere:

Suche

Kategorien

Zustandsfragen

Entscheidungslogik

Empfehlungen

Standortsuche

Datenverarbeitung

API-Schnittstellen

Fehlerfälle

4. ZUSATZ-Anforderungen

Die folgenden Funktionen sind optional und werden nur bei ausreichender Zeit umgesetzt:

4.1 Posting-Funktion

Eine einfache Posting-Funktion kann ermöglichen:

Foto hochladen

Beschreibung hinzufügen

Zustand bzw. Defekt angeben

Abholung oder Lieferung auswählen

Verschenken oder Reparieren lassen auswählen

Interesse an einem Posting zeigen

4.2 Automatische Bilderkennung

Ein Foto könnte automatisch analysiert werden, um eine passende Kategorie vorzuschlagen.

Diese Funktion ist nicht Bestandteil des Kernsystems.

4.3 Weitere optionale Funktionen

Routing

Favoriten

Prüfung der Öffnungszeiten

Administration

Bereich „Wertstoff-Wissen Wien / Gut zu wissen“

5. NICHT-ZIELE

Folgende Funktionen sind ausdrücklich nicht Bestandteil des Kernumfangs:

native Android-App

native iOS-App

vollständiger Marktplatz

Chat-System

Bezahlung

Versandabwicklung

Vermittlung von Verträgen

rechtsverbindliche Beratung

vollständige Abdeckung aller externen Standortdaten

automatische Bilderkennung als Kernfunktion

vollständiges Routing als Kernfunktion

6. Technische Rahmenbedingungen

Das Projekt wird als Client-Server-Anwendung umgesetzt.

Die grundlegende Architektur besteht aus:

Frontend
    |
    | HTTP / API
    v
Backend
    |
    +----------> Datenbank
    |
    +----------> Externe Datenquellen


Der Prototyp wird für Wien und ausgewählte Kategorien entwickelt.

Die Anwendung wird auf einer geeigneten Serverumgebung bereitgestellt.

7. Projektumfang

Der Kernumfang des Projekts besteht aus:

Responsive Webfrontend

Backend

Datenbank

Entscheidungslogik

dokumentierte Schnittstellen

externe Standortdaten

Kartenansicht

Standortsuche

Tests

Serverbetrieb

Deployment

technische Dokumentation

Projektdokumentation

Präsentation

Optionale Funktionen werden erst umgesetzt, wenn alle Kernanforderungen vollständig funktionieren.