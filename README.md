# Vacation Home Booking 
The Vacation Home Booking is a project based on a microservice architecture. It was developed as part of the course **"Programming Distributed Systems"**. The goal is to create a robust and scalable platform for booking vacation homes.

## System Architecture

### *1. Android App for Users*
- **Language:** Kotlin 
- **Platform:** Android Virtual Device (AVD) 
- **Features:**
- User-friendly interface 
- List, sort, and filter vacation homes 
- Detail view with placeholder images 
- Rating feature (create and read reviews) 
- "About" page with project and team information

### *2. CLI Tool for Administrators*
- **Language:** Python 
- **Purpose:** Management of users, listings, and reviews 
- **Features:**
- Moderation of users, listings, and reviews 
- Testing of backend APIs 
- Output in readable format 
- User-friendly commands and help function

### *3. Authentication Microservice*
- **Language:** Python 
- **Framework:** Django 
- **Deployment:** Docker 
- **Architecture:** RESTful API 
- **Database:** dbsqlite.3 
- **Features:**
- User login and authentication 
- Permission management for other microservices

### *4. Vacation Home Listings Microservice*
- **Language:** Python 
- **Framework:** FastAPI 
- **Deployment:** Docker 
- **Architecture:** RESTful API 
- **Database:** MongoDB 
- **Features:**
- CRUD operations for vacation home listings 
- Use of the AirBnB dataset for realistic listings 
- Data consistency through IP whitelisting for MongoDB access

## Starting the Application 

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
| Issue | Solution |
|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| Docker won't start or gives container errors | Delete existing containers with conflicting names or ports. |
| Android Studio can't find Gradle | Ensure only hotel_application is opened, not the full repo. If the error still persist, try re-opening Android Studio |
| Timeout error on data fetching | Computers in laboratories have restricted network environments; try running the project on a local machine. |

## Acknowledgment 
The contributions of the team members to the components are documented in the file [AUTHORS.md](./AUTHORS.md).

## License 
This project is open-source and available under the [BSD 3-Clause License](https://opensource.org/license/BSD-3-Clause).
