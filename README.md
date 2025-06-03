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
**Hinweis: Der abschnitt 'Anwendung Starten' ist auf Englisch verfasst, da dies eine Vorgabe des Kurses ist und der Kurs in Englischer Sprache unterrichtet wird.**
### *Prerequisites*
Before you begin, ensure the following tools are installed on your system:
- Git
- Python 3.8+
- Docker
- Visual Studio Code
- Android Studio with an Android Virtual Device

### Cloning the project
- Open your terminal and navigate to your desired project directory:
```bash
cd desktop # or any directory of your choice
```

- Clone the project:
```bash
git clone https://gitlab.hsrw.eu/34188/vacation-home-booking.git
```

- Open the project folder in Visual Studio Code

### Setting Up Authentication Microservice
- In VS Code, open the integrated terminal

- Navigate to authMicroservice directory:
```bash
cd authMicroservice
```

- Create and activate a virtual environment:
```bash
python3 -m venv venv
source venv/bin/activate # for Linux & MacOS
# OR
venv\Scripts\activate # for Windows
```

- Install the required dependencies:
```bash
pip3 install -r requirements.txt
```

- Apply database migrations:
```bash
python3 manage.py migrate
```

- Deactivate the virtual environment and return to root directory:
```bash
deactivate
cd ..
```

### Setting Up Environment Variables
- Create a file named .env in the root of the project, next to docker-compose.yml, and paste the MongoDB URL provided by our team via Sciebo.

### Running the Application with Docker:
- From the terminal of VS Code, run the following commands to start docker:
```bash
docker compose up -d listing_service authMicroservice
docker compose run cli_model
```

### Using CLI Tool
Once the CLI tool is running:
- Type **pds** in the terminal and it'll ask you to login
- You must first sign up via our Android application (see next section)
- After signing up via our application, log in via CLI
- Type **help** in the CLI to see the full list of available commands

### Running the Android Application
- Open Android Studio
- **ONLY** open the **hotel_application** subfolder — do **not** open the entire project root, or Android Studio may fail to load Gradle correctly
- Run the project on your Android Emulator
- Our application **FeinBleiben** will launch
- Sign up a new user in the app, then you may use the same credentials to log in through the CLI tool

### Comman Issues & Troubleshooting
| Issue                                        | Solution                                                                                                   |
|----------------------------------------------|------------------------------------------------------------------------------------------------------------|
| Docker won't start or gives container errors | Delete existing containers with conflicting names or ports                                                 |
| Android Studio can't find Gradle             | Ensure only hotel_application is opened, not the full repo                                                 |
| Timeout error on data fetching               | Computers in laboratories have restricted network environments; try running the project on a local machine |
| CLI login fail                               | Make sure you have successfully signed up in the Android application before attempting to log in           |


## Anerkennung
Die Arbeit der Teammitglieder an der Komponenten ist in der Datei [AUTHORS.md](./AUTHORS.md) dokumentiert.

## Lizenze
Dieses Projekt ist Open-Source und unter der [BSD3-Clause license](https://opensource.org/license/BSD-3-Clause) verfügbar.