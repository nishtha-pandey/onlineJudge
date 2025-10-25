@echo off
echo Starting Online Judge Platform...
echo.

echo Starting Backend (Spring Boot)...
start "Backend" cmd /k "cd backend && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 15 /nobreak > nul

echo Starting Frontend (Next.js)...
start "Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo ========================================
echo Online Judge Platform Started!
echo ========================================
echo Frontend: http://localhost:3000
echo Backend API: http://localhost:8080/api
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Sample Contest ID: 1
echo Username: Any name you like
echo.
echo Press any key to exit...
pause > nul
