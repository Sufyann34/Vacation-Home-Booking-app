# Vacation Home Booking
Das Vacation Home Booking ist ein Projeckt auf Basis einer Microservice Architektur. Es wurde im Rahmen des des Kurses "Programming
Distributed Systems" entwickelt. Ziel ist es, eine robuste und skalierbare Plattform für die Buchung von Ferienhäusern zu erstellen.

## Systemarchitektur

### *1. Android App für Benutzer*
- **Sprache:** Kotlin
- **Plattform:** Android Virtual Device (AVD)
- **Funktionen:**
    - Benutzerfreundliche Oberfläche
    - Auflisten, Sortieren und Filtern von Ferienhäusern
    - Detailansicht mit Platzhalterbildern
    - Bewertungsfunktion (erstellen und lesen)
    - "Über" Seite mit Infos zum Projekt und Team

### *2. CLI-Tool für Administratoren*
- **Sprache:** Python
- **Zweck:** Verwaltung von Nutzern, Listen und Bewertungen
- **Funktionen:**
    - Moderation von Benutzern, Listen und Bewertungen
    - Testen der Backend-APIs
    - Ausgabe in lesbarem Format
    - Benutzerfreundliche Befehle und Hilfe-Funktion

### *3. Authentifizierungs-Microservice*
- **Sprache:** Python
- **Framework:** Django
- **Bereitstellung:** Docker
- **Architektur:** RESTful API
- **Datenbank:** dbsqlite.3
- **Funktionen:**
    - Benutzeranmeldung und Authentifizierung
    - Rechteverwaltung für andere Microservices

### *4. Microservice für Ferienhauslisten*
- **Sprache:** Python
- **Framework:** FastAPI
- **Bereitstellung:** Docker
- **Architektur:** RESTful API
- **Datenbank:** MongoDB
- **Funktionen:**
    - CRUD-Operationen für Ferienhauslisten
    - Nutzung des AirBnB-Datensatzes für realistische Listen
    - Datenkonsistenz durch IP-Whitelist für MongoDB zugriff

## Anwendung Starten
***Am Ende hinzuzufügen***

## Anerkennung
Jedes Teammitglied hat an einer oder mehreren Komponenten gearbeitet.

## Lizenze
Dieses Projekt ist Open-Source und unter der [BSD3-Clause](https://opensource.org/license/BSD-3-Clause) License verfügbar.