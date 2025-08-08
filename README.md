TLDR:
in cmd: git clone https://github.com/Jeremy304/Wordle-2.git
        cd Wordle-2

WINDOWS: gradlew.bat bootRun
MAC/LINUX: ./gradlew bootRun

Open browser: try "http://localhost:8080" or "http://localhost:5500"

DONE 

if gradlew doesn't work: 

cd "C:\VSC projects\demo"                 
        .\gradlew.bat bootRun



A simple backend word game built with Spring Boot. Players attempt to guess a 6-letter target word, similar to Wordle. The game returns feedback on each guess.

Tech Stack

Java 17

Spring Boot 3.5.3

Gradle

REST API

VS Code

**STRUCTURE**

.
├── src
│   └── main
│       └── java
│           └── com.example.demo
│               ├── DemoApplication.java
│               ├── GameController.java
│               ├── WordGameService.java
│               └── WordGameResult.java
│       └── resources
│           ├── static
│           │   └── index.html
│           └── application.properties
├── build.gradle
└── README.md

Running the App Locally

Prerequisites

JDK 17+

Git

Gradle (or use the included gradlew)

1. Clone the Repo -
git clone https://github.com/Jeremy304/Wordle-2.git
cd Wordle-2


2. Run the App
./gradlew bootRun    # or use gradlew.bat bootRun for Windows

open browser to http://localhost:8080 or gttps://localhost:5500 

Interaction via: Postman, cURL, Web frontend 

Word Files

The game uses two files:

words.txt – full dictionary of valid guesses

wordbank.txt – target words the game can select from

EX: 

POST /api/guess
Content-Type: application/json

{
  "guess": "planet"
}

Project is licensed under the MIT License
