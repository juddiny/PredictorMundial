#!/bin/bash

# Script para configurar MySQL para World Cup Predictor
# Uso: bash setup_mysql.sh

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  Configurador de MySQL para World Cup Predictor               ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Verificar si MySQL está instalado
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL no está instalado."
    echo ""
    echo "Por favor instala MySQL:
    - Ubuntu/Debian: sudo apt install mysql-server
    - macOS: brew install mysql
    - Windows: Descarga desde https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

echo "✅ MySQL detectado"
echo ""

# Preguntar por credenciales
read -p "¿Cuál es tu usuario MySQL? (default: root): " MYSQL_USER
MYSQL_USER=${MYSQL_USER:-root}

read -sp "¿Cuál es tu contraseña MySQL? (deja en blanco si no hay): " MYSQL_PASSWORD
echo ""

MYSQL_HOST="localhost"
MYSQL_DB="world_cup_predictor"

# Construcción del comando mysql
MYSQL_CMD="mysql -h $MYSQL_HOST -u $MYSQL_USER"
if [ ! -z "$MYSQL_PASSWORD" ]; then
    MYSQL_CMD="$MYSQL_CMD -p$MYSQL_PASSWORD"
fi

# Verificar conexión
echo "🔍 Verificando conexión a MySQL..."
if ! $MYSQL_CMD -e "SELECT 1" &>/dev/null; then
    echo "❌ Error: No se puede conectar a MySQL."
    echo "   Verifica:"
    echo "   1. MySQL está en ejecución (sudo service mysql start)"
    echo "   2. Las credenciales son correctas"
    echo "   3. MySQL está en localhost:3306"
    exit 1
fi

echo "✅ Conexión exitosa"
echo ""
echo "📦 Creando base de datos '$MYSQL_DB'..."

# SQL script para crear la base de datos y el usuario
SQL_SCRIPT="
CREATE DATABASE IF NOT EXISTS $MYSQL_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
"

# Ejecutar el script
if $MYSQL_CMD -e "$SQL_SCRIPT"; then
    echo "✅ Base de datos '$MYSQL_DB' creada/verificada"
else
    echo "❌ Error al crear la base de datos"
    exit 1
fi

# Actualizar application.properties
APP_PROPERTIES="../BackendPredictor/src/main/resources/application.properties"

if [ -f "$APP_PROPERTIES" ]; then
    echo ""
    echo "📝 Actualizando application.properties..."

    # Crear backup
    cp "$APP_PROPERTIES" "$APP_PROPERTIES.bak"
    echo "✅ Backup creado: $APP_PROPERTIES.bak"

    # Actualizar credenciales
    sed -i.tmp "s/spring.datasource.username=.*/spring.datasource.username=$MYSQL_USER/" "$APP_PROPERTIES"
    sed -i.tmp "s/spring.datasource.password=.*/spring.datasource.password=$MYSQL_PASSWORD/" "$APP_PROPERTIES"
    rm -f "$APP_PROPERTIES.tmp"

    echo "✅ Credenciales actualizadas en application.properties"
fi

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  ✅ Configuración completada                                   ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "🚀 Próximos pasos:"
echo ""
echo "1. Compila el proyecto:"
echo "   cd BackendPredictor && ./mvnw clean package -DskipTests"
echo ""
echo "2. Ejecuta la aplicación:"
echo "   java -jar target/BackendPredictor-0.0.1-SNAPSHOT.jar"
echo ""
echo "3. Verifica los teams:"
echo "   curl http://localhost:8080/api/teams"
echo ""
echo "Datos precargados:"
echo "  - 32 equipos de fútbol"
echo "  - 2 usuarios de prueba (usuario/usuario123, administrador/admin123)"
echo "  - 2 roles (ROLE_USER, ROLE_ADMIN)"
echo ""

