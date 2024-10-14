@echo off
setlocal

set "host=postgres-animal-shelter"
set "port=5432"
set "db=Animal_Shelter"
set "user=postgres"
set "initSqlPath=init.sql"

rem Wait for PostgreSQL to be ready
:waitForPostgres
ping -n 2 %host% > nul
psql -h %host% -U %user% -d %db% -c "SELECT 1" > nul 2>&1
if errorlevel 1 (
    echo Waiting for PostgreSQL to be ready...
    goto waitForPostgres
)

rem Execute the SQL script
psql -h %host% -U %user% -d %db% -f %initSqlPath%

endlocal