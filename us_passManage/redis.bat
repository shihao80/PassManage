@echo off

:again
set ip=127.0.0.1
set port=6379
netstat -ano|findstr %ip%:%port%|findstr -i ESTABLISHED
if ERRORLEVEL 1 (goto err) else (goto ok)

:err
tasklist|findstr -i "redis-server.exe"
if ERRORLEVEL 1 (start "" "%CD%\..\redis\redis_start.vbs") 

:ok
echo Redis Services is running %Date:~0,4%-%Date:~5,2%-%Date:~8,2% %Time:~0,2%:%Time:~3,2%
