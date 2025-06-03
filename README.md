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
*Hinweis: Der abschnitt 'Anwendung Starten' ist auf Englisch verfasst, da dies eine Vorgabe des Kurses ist und der Kurs in Englischer Sprache unterrichtet wird.*
### i. Prerequisites
Before you begin, ensure the following tools are installed on your system:
- [Docker](https://www.docker.com/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [Android Studio](https://developer.android.com/studio) with an Android Virtual Device

### ii. Cloning the project
- Open your terminal and navigate to your desired project directory:
```bash
cd ~/desktop # or any directory of your choice
```

- Clone the project:
```bash
git clone https://gitlab.hsrw.eu/34188/vacation-home-booking.git
```

- Open the project folder in Visual Studio Code:
```bash
open -a "Visual Studio Code" vacation-home-booking
```

### iii. Setting Up Environment Variables
Create a file named **.env** in the root of the project, next to docker-compose.yml, and paste the MongoDB URL provided by our team via Sciebo.

Alternatively, you can simply download the **.env** file from Sciebo and copy-paste it directly into the root directory.

### iv. Running the Application with Docker:
- From the terminal of VS Code, run the following commands to start docker:
```bash
docker compose build
```
```bash
docker compose up -d listing_service authMicroservice
```
```bash
docker compose run cli_model
```
Make sure you have docker running in the backgroud or else these commands won't be executed.

### v. Using CLI Tool
Once all the docker containers are set up:
- Type **pds** in the same terminal to start the CLI model.
- To register as an admin, your email must end with **@group08.pds**.
- Type **help** in the CLI to see the full list of available commands.

### vi. Running the Android Application
- Open Android Studio on your system.
- **ONLY** open the **hotel_application** subfolder — do **not** open the entire project root, or Android Studio may fail to load Gradle correctly.
- Run the project on your selected Android Emulator.
- Our application **FeinBleiben** will launch.

### vii. Comman Issues & Troubleshooting
| Issue                                        | Solution                                                                                                              |
|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| Docker won't start or gives container errors | Delete existing containers with conflicting names or ports.                                                           |
| Android Studio can't find Gradle             | Ensure only hotel_application is opened, not the full repo. If the error still persist, try re-opening Android Studio |
| Timeout error on data fetching               | Computers in laboratories have restricted network environments; try running the project on a local machine.           |

## Anerkennung
Die Arbeit der Teammitglieder an der Komponenten ist in der Datei [AUTHORS.md](./AUTHORS.md) dokumentiert.

## Lizenze
Dieses Projekt ist Open-Source und unter der [BSD3-Clause license](https://opensource.org/license/BSD-3-Clause) verfügbar.