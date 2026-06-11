@echo off
REM Script para levantar el frontend en Windows
cd /d "%~dp0"

echo Iniciando servidor frontend en http://localhost:8000

if exist node_modules\.bin\http-server (
  npx http-server -a 0.0.0.0 -p 8000
) else (
  echo Instalando dependencias...
  npm install
  npx http-server -a 0.0.0.0 -p 8000
)
