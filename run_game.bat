@echo off
title Minecraft Fabric Test Client
cd /d "%~dp0"
echo ==============================================
echo  Starting Minecraft with sliding-mod...
echo ==============================================
call gradlew.bat runClient
pause
