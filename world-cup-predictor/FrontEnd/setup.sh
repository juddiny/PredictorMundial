#!/bin/bash
# Script para levantar el frontend en desarrollo

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}World Cup Predictor - Frontend Setup${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Detectar qué está disponible
if command -v python3 &> /dev/null; then
    echo -e "${GREEN}✓ Python3 detectado${NC}"
    PYTHON_CMD="python3 -m http.server 8000"
    PYTHON_AVAILABLE=true
fi

if command -v python &> /dev/null; then
    echo -e "${GREEN}✓ Python detectado${NC}"
    PYTHON_CMD="python -m http.server 8000"
    PYTHON_AVAILABLE=true
fi

if command -v php &> /dev/null; then
    echo -e "${GREEN}✓ PHP detectado${NC}"
    PHP_AVAILABLE=true
fi

if command -v node &> /dev/null; then
    echo -e "${GREEN}✓ Node.js detectado${NC}"
    NODE_AVAILABLE=true
fi

echo -e "\n${BLUE}Opciones disponibles:${NC}\n"

if [ "$PYTHON_AVAILABLE" = true ]; then
    echo "1) Python HTTP Server (recomendado)"
    echo "   $PYTHON_CMD"
fi

if [ "$PHP_AVAILABLE" = true ]; then
    echo "2) PHP Development Server"
    echo "   php -S localhost:8000"
fi

if [ "$NODE_AVAILABLE" = true ]; then
    echo "3) Node.js HTTP Server"
    echo "   npx http-server"
fi

echo -e "\nEligiendo opción 1 (Python)...\n"

if [ "$PYTHON_AVAILABLE" = true ]; then
    echo -e "${GREEN}Iniciando servidor en http://localhost:8000${NC}\n"
    eval $PYTHON_CMD
else
    echo -e "${RED}No se encontró Python. Instálalo o ejecuta manualmente.${NC}"
fi

