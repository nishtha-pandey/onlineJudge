Online Judge Platform

A complete, plug-and-play coding contest system — Spring Boot backend + React/Next.js frontend, fully Dockerized so you can run everything with one command.

 Project Overview
onlinejudge/
├── backend/         
├── frontend/        
├── docker-compose.yml
└── README.md

Backend (Spring Boot)

REST APIs for contests, submissions & leaderboard

Code execution using Docker with time/memory limits

Async judging with live status

H2 in-memory DB with sample data

JPA/Hibernate for persistence

Frontend (Next.js + React)

Contest join page

Live contest UI with problems, editor & scoreboard

Monaco editor for Java / Python / C++

Auto refresh for submissions & leaderboard

Tailwind CSS responsive UI
 Getting Started
Prerequisites

Docker + Docker Compose

Git

Run with Docker (Recommended)
git clone <repository-url>
cd onlinejudge
docker-compose up --build


Now visit:

Frontend → http://localhost:3000

Backend API → http://localhost:8080/api

H2 Console → http://localhost:8080/h2-console

 How to Use

Open http://localhost:3000

Join contest — use Contest ID = 1 and any name

Pick a problem and write code

Submit — watch status change to “Accepted/Wrong Answer/…”

Leaderboard refreshes every 15s automatically

Included Sample Problems

Hello World

Sum of Two Numbers

Even or Odd

 Important API Endpoints

GET /api/contests/{id}

POST /api/submissions

GET /api/submissions/{id}

GET /api/contests/{id}/leaderboard

 Architecture Snapshot

Backend: Contest, Problem, TestCase, Submission models → JPA Repositories → Services → REST Controllers → Docker based judge
Frontend: Join page → Contest UI → Monaco editor → Polling for results

 Docker Setup (3 containers)
Container	Purpose
Backend	Spring Boot API + H2 DB
Frontend	Next.js UI
Judge	Secure sandbox for code execution

Judge engine runs code with:

CPU & memory caps

No network

Auto kill on timeout

Runs Java, Python & C++

 Security Highlights

Code executes in isolated container

Resource limits to prevent abuse

Validated inputs & CORS configured

 Common Problems & Fixes

Docker not starting? Open Docker Desktop first
Port already in use? Stop anything on 3000/8080
Judge not working? Rebuild without cache:

docker-compose up --build --no-cache


More quick commands:

docker-compose logs -f backend
docker-compose restart frontend
docker-compose down -v

 Planned Improvements

User login & profiles

WebSockets for live push updates

Support more languages

Admin UI to create contests

Code sharing & discussion       this is the updated humanized version of this project 
