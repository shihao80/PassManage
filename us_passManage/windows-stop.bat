@echo off
setlocal enabledelayedexpansion
for /f "delims=  tokens=1" %%i in ('netstat -aon ^| findstr "18080"') do (
set a=%%i
goto js
)
:js
taskkill /f /pid "!a:~71,5!"
