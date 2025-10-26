ONLINE JUDGE PLATFORM

This is a full-stack online coding contest platform built with Spring Boot (backend) and React/Next.js (frontend).
It’s fully Dockerized, so you can spin up the whole thing with a single command and start running coding contests locally.

It’s simple, fast to set up, and built mainly for learning, experimenting, or interview demos.


WHAT IT DOES?

Lets users join a contest with just a contest ID and username

Lets you solve coding problems in real time

Runs code safely inside Docker containers

Shows live submission results and an updating leaderboard

Comes with sample problems and data so you can test it right away


PROJECT LAYOUT

onlinejudge/
├── backend/               # Spring Boot backend
│   ├── config/            # App config
│   ├── controller/        # REST APIs
│   ├── dto/               # Data Transfer Objects
│   ├── model/             # Database entities
│   ├── repository/        # JPA repositories
│   └── service/           # Business logic
├── frontend/              # Next.js frontend
│   ├── app/               # Pages and UI
│   └── lib/               # API utilities
├── docker-compose.yml
└── README.md


.


KEY FEATURES

Backend (Spring Boot)

REST APIs for contests, submissions, and leaderboards

Secure code execution inside Docker containers

Asynchronous judging (so it doesn’t block)

H2 in-memory database with some preloaded data

Built with JPA and Hibernate

Frontend (React/Next.js)

Simple join page (enter contest ID and username)

Live contest interface with problems, code editor, and leaderboard

Monaco editor (with syntax highlighting for Java, Python, and C++)

Auto-refresh for submission status and leaderboard

Fully responsive with Tailwind CSS

GETTING STARTED

Docker and Docker Compose & Git

Quick Setup (Recommended)
git clone <repository-url>
cd onlinejudge
docker-compose up --build


EVERYTHING IS RUNNING ON

Frontend → http://localhost:3000

Backend API → http://localhost:8080/api

H2 Console → http://localhost:8080/h2-console



Manual Setup (if you prefer running things separately)
Backend
cd backend
docker build -f Dockerfile.judge -t onlinejudge-judge:latest .
mvn spring-boot:run


Runs on: http://localhost:8080

Frontend
cd frontend
npm install
npm run dev


Runs on: http://localhost:3000


HOW TO USE IT?

Go to http://localhost:3000

Join using Contest ID 1 and any username

Pick a problem

Write your code (Java, Python, or C++)

Hit submit — the backend spins up a container and runs your code

Watch the results and leaderboard update automatically


INCLUDED SAMPLE PROBLEMS

Hello World – Print “Hello World”

Sum of Two Numbers – Add two integers

Even or Odd – Check if a number is even or odd

Just enough to see everything working end to end.


API 

Endpoint	Description
GET /api/contests/{id}	Contest details
POST /api/submissions	Submit code
GET /api/submissions/{id}	Get submission result
GET /api/contests/{id}/leaderboard	Live leaderboard
GET /api/contests/{id}/submissions	All submissions
GET /api/contests/{id}/submissions/{username}	Submissions by user


UNDER THE HOOD

Backend

Models: Contest, Problem, TestCase, Submission

Services handle logic and judging

Uses Docker to safely run user code

Async processing for submissions

Frontend

Join page and contest page (problem view, editor, leaderboard)

Axios for API calls

Polling for real-time updates


DOCKER SETUP

Everything runs in containers:

Service	What it does
Backend	Spring Boot API with H2 database
Frontend	Next.js web app
Judge Engine	Executes user code safely


JUDGE ENGINE HIGHLIGHTS

CPU and memory limits

Network disabled

Timeout for long-running code

Supports Java, Python, and C++


DATABASE OVERVIEW

Contest – Contest info

Problem – Details and test cases

TestCase – Input/output for checking solutions

Submission – Stores code, result, and metadata


SECURITY

User code runs in isolated containers

Memory/time limits to prevent abuse

Server-side input validation

Safe CORS setup


DEVELOPMENT NOTES

H2 console at /h2-console (user: sa, password: blank)

Hot reload enabled for frontend

Logs and error handling for easy debugging


FUTURE IDEAS

Add user login/registration

More language support

WebSocket-based live updates

Admin tools for creating contests/problems

Code sharing and discussion section


COMMON ISSUES

1)Docker

Docker not running? Start Docker Desktop.

Permission denied? Try sudo or adjust permissions.

Port conflicts? Make sure 3000 and 8080 are free.

To rebuild cleanly:

docker-compose up --build --no-cache

2)Backend

Needs Java 17 or newer

Check logs: docker-compose logs backend

H2 console not working? Make sure backend is up

3)Frontend

Needs Node.js 18+

If build fails, clear cache and reinstall

Hot reload sometimes needs a restart


HANDY DOCKER COMMANDS

docker-compose logs -f [service]
docker-compose restart [service]
docker-compose down
docker-compose down -v
docker-compose up --build

LICENSE

This project was built for learning and demo purposes — feel free to explore, modify, and use it for your own experiments or interviews.
