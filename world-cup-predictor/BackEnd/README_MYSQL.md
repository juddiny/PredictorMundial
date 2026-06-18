# 🏆 World Cup Predictor - Estado del Proyecto

## ✅ Cambios Completados

### Backend - Migración a MySQL ✨
Tu backend ya está completamente configurado para usar MySQL. Los cambios incluyen:

1. **Entidad Team** - Nueva entidad para almacenar los 32 equipos con su región
2. **Controlador REST** - Endpoints para obtener equipos por región, nombre, etc.
3. **DataInitializer** - Carga automáticamente todos los equipos al iniciar
4. **Schema SQL** - Define la estructura de todas las tablas
5. **Application.properties** - Cambiado de H2 a MySQL

### Equipos Precargados (32 Total) ⚽
```
CONCACAF (América del Norte):        Canadá, Estados Unidos, México
UEFA (Europa):                        16 equipos europeos
CONCACAF-CARIBBEAN (Caribe):         Curazao, Haití, Panamá
CAF (África):                         10 equipos africanos
AFC (Asia/Oceanía):                  10 equipos asiáticos/oceanía
```

## 🚀 Instrucciones para Alcanzar el Éxito

### Paso 1: Instalar MySQL
Si no tienes MySQL instalado:

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

**macOS (con Homebrew):**
```bash
brew install mysql
mysql_secure_installation
```

**Windows:**
- Descarga desde: https://dev.mysql.com/downloads/mysql/

### Paso 2: Configurar MySQL (Opción A - Automática)
```bash
cd /home/usuario/Documentos/Web_I/proyecto/world-cup-predictor/BackEnd
bash setup_mysql.sh
# Sigue las instrucciones interactivas
```

### Paso 3: Configurar MySQL (Opción B - Manual)
Si el script automatizado no funciona:

```bash
mysql -u root -p
# Ingresa tu contraseña de MySQL

# En la consola de MySQL, ejecuta:
CREATE DATABASE IF NOT EXISTS world_cup_predictor 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

EXIT;
```

Luego actualiza **application.properties**:
```properties
spring.datasource.username=root
spring.datasource.password=tuContraseña
```

### Paso 4: Compilar el Backend
```bash
cd /home/usuario/Documentos/Web_I/proyecto/world-cup-predictor/BackEnd/BackendPredictor
./mvnw clean package -DskipTests
```

### Paso 5: Ejecutar la Aplicación
```bash
java -jar target/BackendPredictor-0.0.1-SNAPSHOT.jar
```

Deberías ver en la consola:
```
[DataInitializer] Usuarios creados: 
 - usuario roles=[ROLE_USER]
 - administrador roles=[ROLE_USER, ROLE_ADMIN]

[DataInitializer] Equipos cargados: 32 equipos en total

Tomcat started on port 8080
```

## 🧪 Verificar que Todo Funciona

### Prueba 1: Obtener todos los equipos
```bash
curl http://localhost:8080/api/teams | python -m json.tool
```

Esperado: Una lista de 32 equipos

### Prueba 2: Obtener equipos por región
```bash
curl http://localhost:8080/api/teams/region/UEFA | python -m json.tool
```

Esperado: 16 equipos europeos

### Prueba 3: Contar equipos
```bash
curl http://localhost:8080/api/teams/count
```

Esperado: `{"total":32}`

### Prueba 4: Obtener equipos disponibles
```bash
curl http://localhost:8080/api/teams/regions
```

### Prueba 5: Registrar nuevo usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "username": "juanperez",
    "password": "micontraseña123"
  }'
```

## 📊 Base de Datos MySQL

### Estructura de Tablas

**roles**
- id, name

**users**
- id, name, username, password, created_at

**teams** 
- id, name, region

**predictions**
- id, user_id, home_team, away_team, predicted_home_score, predicted_away_score, ai_analysis, created_at

**user_roles**
- user_id, role_id

### Verificar desde MySQL
```bash
mysql -u root -p world_cup_predictor
# En la consola MySQL:
SHOW TABLES;
SELECT * FROM teams LIMIT 5;
SELECT COUNT(*) FROM teams;
SELECT DISTINCT region FROM teams;
```

## 📁 Archivos Creados/Modificados

### Nuevos Archivos:
- ✅ `Team.java` - Entidad Team
- ✅ `TeamRepository.java` - Repositorio
- ✅ `TeamController.java` - Controlador REST
- ✅ `schema.sql` - Definición de tablas
- ✅ `MYSQL_SETUP.md` - Guía detallada MySQL
- ✅ `CAMBIOS_REALIZADOS.md` - Resumen de cambios
- ✅ `setup_mysql.sh` - Script de configuración automática

### Archivos Modificados:
- ✅ `application.properties` - Configuración MySQL
- ✅ `DataInitializer.java` - Carga de equipos
- ✅ `pom.xml` - Dependencias (MySQL Connector/J)

## 🔐 Usuarios de Prueba Predefinidos

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| usuario | usuario123 | ROLE_USER |
| administrador | admin123 | ROLE_ADMIN, ROLE_USER |

## 🛠️ Troubleshooting

**Problema**: "Access denied for user 'root'@'localhost'"
- Solución: Verifica que MySQL está en ejecución (`sudo service mysql start`)

**Problema**: "Unknown database 'world_cup_predictor'"
- Solución: Crea la BD manualmente (ver paso 3)

**Problema**: El servidor no inicia
- Solución: Verifica que el puerto 8080 está disponible

**Problema**: Los equipos no aparecen
- Solución: Verifica que MySQL está corriendo y la conexión funciona

## 📝 Notas Importantes

1. **Datos Persistentes**: A diferencia de H2, MySQL almacena datos permanentemente
2. **Credenciales Configurables**: Puedes cambiar usuario/contraseña en `application.properties`
3. **Creación Automática**: Las tablas se crean automáticamente la primera vez
4. **Encoding**: Se usa utf8mb4 para soportar caracteres especiales y emojis

## 🎯 Próximos Pasos (Cuando Todo Funcione)

1. Integrar frontend con backend (cambiar auth.js para llamar a endpoints reales)
2. Implementar `/api/auth/login` para retornar JWT
3. Agregar más endpoints para predicciones
4. Implementar seguridad JWT en backend
5. Conectar con API de Gemini para análisis IA

---

## 📞 Resumen Rápido

```bash
# 1. Instalar MySQL (si no está instalado)
sudo apt install mysql-server  # Ubuntu
# o
brew install mysql             # macOS

# 2. Configurar BD automáticamente
cd /home/usuario/Documentos/Web_I/proyecto/world-cup-predictor/BackEnd
bash setup_mysql.sh

# 3. Compilar
cd BackendPredictor && ./mvnw clean package -DskipTests

# 4. Ejecutar
java -jar target/BackendPredictor-0.0.1-SNAPSHOT.jar

# 5. Probar
curl http://localhost:8080/api/teams
```

¡Todo listo para usar! 🚀

