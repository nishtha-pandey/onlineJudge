# Online Judge Platform

A complete online coding contest platform built with Spring Boot backend and React/Next.js frontend, fully dockerized for easy deployment.

## Project Structure

```
onlinejudge/
├── backend/                 # Spring Boot Backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/example/onlinejudge/
│   │       │       ├── config/         # Configuration classes
│   │       │       ├── controller/     # REST controllers
│   │       │       ├── dto/           # Data Transfer Objects
│   │       │       ├── model/         # JPA entities
│   │       │       ├── repository/    # JPA repositories
│   │       │       └── service/      # Business logic
│   │       └── resources/
│   │           └── application.properties
│   ├── Dockerfile           # Backend container
│   ├── Dockerfile.judge     # Judge engine container
│   └── pom.xml             # Maven configuration
├── frontend/               # React/Next.js Frontend
│   ├── src/
│   │   ├── app/           # Next.js app directory
│   │   └── lib/           # API utilities
│   ├── Dockerfile         # Frontend container
│   ├── package.json       # Node.js dependencies
│   └── next.config.ts     # Next.js configuration
├── docker-compose.yml      # Multi-container orchestration
└── README.md              # This file
```

## Features

### Backend (Spring Boot)
- **REST API** for contest management, submissions, and leaderboard
- **Docker-based Code Execution** with resource limits and security
- **Real-time Submission Processing** with asynchronous judging
- **H2 Database** with sample data pre-populated
- **JPA/Hibernate** for data persistence

### Frontend (React/Next.js)
- **Join Page** with contest ID and username input
- **Live Contest Interface** with problem view, code editor, and leaderboard
- **Monaco Editor** with syntax highlighting for Java, Python, and C++
- **Real-time Updates** with polling for submission status and leaderboard
- **Responsive Design** with Tailwind CSS

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Git

### Option 1: Docker Compose (Recommended)

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd onlinejudge
   ```

2. **Start all services:**
   ```bash
   docker-compose up --build
   ```

3. **Access the application:**
   - Frontend: `http://localhost:3000`
   - Backend API: `http://localhost:8080/api`
   - H2 Console: `http://localhost:8080/h2-console`

### Option 2: Manual Setup

#### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build Docker image for code execution:**
   ```bash
   docker build -f Dockerfile.judge -t onlinejudge-judge:latest .
   ```

3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

#### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

## Usage

1. **Open the application** at `http://localhost:3000`
2. **Join Contest** using Contest ID: `1` and any username
3. **Select a problem** from the problems list
4. **Write your code** in the Monaco editor (Java, Python, or C++)
5. **Submit your solution** and watch real-time status updates
6. **View the leaderboard** that updates every 15 seconds

## Sample Problems

The platform comes with 3 beginner-friendly problems:

1. **Hello World** - Print "Hello World"
2. **Sum of Two Numbers** - Calculate sum of two integers
3. **Even or Odd** - Determine if a number is even or odd

## API Endpoints

- `GET /api/contests/{contestId}` - Get contest details
- `POST /api/submissions` - Submit code for judging
- `GET /api/submissions/{submissionId}` - Get submission status
- `GET /api/contests/{contestId}/leaderboard` - Get live leaderboard
- `GET /api/contests/{contestId}/submissions` - Get all submissions
- `GET /api/contests/{contestId}/submissions/{username}` - Get user submissions

## Architecture

### Backend Components
- **Models**: Contest, Problem, TestCase, Submission
- **Repositories**: JPA repositories for data access
- **Services**: ContestService, SubmissionService, JudgeService
- **Controllers**: REST API endpoints
- **Docker Integration**: Secure code execution environment

### Frontend Components
- **Join Page**: Contest entry form
- **Contest Page**: Main interface with problem view, editor, and leaderboard
- **API Service**: Axios-based HTTP client
- **Real-time Updates**: Polling for live status updates

## Docker Architecture

The platform is fully containerized with the following services:

### Services Overview
- **Backend**: Spring Boot API server with H2 database
- **Frontend**: Next.js React application
- **Judge Engine**: Isolated code execution environment

### Docker Judge Engine

The platform uses Docker containers to safely execute user code with:
- **Resource Limits**: Memory and CPU constraints
- **Network Isolation**: No network access during execution
- **Timeout Protection**: Automatic termination of long-running code
- **Multi-language Support**: Java, Python, and C++

### Container Details

#### Backend Container (`backend/Dockerfile`)
- Base: OpenJDK 17
- Maven for dependency management
- Exposes port 8080
- Includes sample data initialization

#### Frontend Container (`frontend/Dockerfile`)
- Base: Node.js 18 Alpine
- Next.js production build
- Exposes port 3000
- Optimized for production

#### Judge Engine Container (`backend/Dockerfile.judge`)
- Base: OpenJDK 17
- Includes Python 3 and C++ compiler
- Isolated execution environment
- Used for secure code evaluation

## Database Schema

- **Contest**: Contest information and timing
- **Problem**: Problem details, constraints, and test cases
- **TestCase**: Input/output pairs for validation
- **Submission**: User code submissions and results

## Security Features

- **Container Isolation**: User code runs in isolated Docker containers
- **Resource Limits**: Memory and time limits prevent resource abuse
- **Input Validation**: Server-side validation of all inputs
- **CORS Configuration**: Proper cross-origin resource sharing

## Development Notes

- **H2 Console**: Available at `http://localhost:8080/h2-console` for database inspection
- **Hot Reload**: Frontend supports hot reloading during development
- **Logging**: Comprehensive logging for debugging and monitoring
- **Error Handling**: Graceful error handling with user-friendly messages

## Future Enhancements

- User authentication and registration
- More programming languages support
- Real-time notifications with WebSockets
- Advanced problem types (interactive, output-only)
- Contest creation and management interface
- Code sharing and discussion features

## Troubleshooting

### Docker Issues
- **Docker not running**: Ensure Docker Desktop is running
- **Permission denied**: Run `docker-compose up` with appropriate permissions
- **Port conflicts**: Check if ports 3000 and 8080 are available
- **Build failures**: Run `docker-compose up --build --no-cache` to rebuild images
- **Judge engine issues**: Verify Docker-in-Docker is enabled in Docker Desktop settings

### Backend Issues
- **Maven build fails**: Check Java version (17+ required)
- **Database connection**: Verify H2 console is accessible at `/h2-console`
- **API not responding**: Check backend logs with `docker-compose logs backend`
- **Judge service errors**: Ensure judge engine container is running

### Frontend Issues
- **Build failures**: Check Node.js version compatibility
- **API connection errors**: Verify backend is running on port 8080
- **Hot reload not working**: Restart the frontend container
- **Monaco editor issues**: Clear browser cache and restart

### Docker Compose Commands
```bash
# View logs
docker-compose logs -f [service-name]

# Restart specific service
docker-compose restart [service-name]

# Stop all services
docker-compose down

# Remove all containers and volumes
docker-compose down -v

# Rebuild and start
docker-compose up --build
```

## License

This project is created for educational purposes and interview demonstrations.
