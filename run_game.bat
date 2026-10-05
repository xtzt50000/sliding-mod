@echo off
title Minecraft Fabric Test Client
cd /d "%~dp0"
echo ==============================================
echo  Starting Minecraft with Rivals Slide Mod...
echo ==============================================
call gradlew.bat runClient
pause
