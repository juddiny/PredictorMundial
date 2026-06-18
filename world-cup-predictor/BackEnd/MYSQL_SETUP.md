# Configuración de MySQL para World Cup Predictor

## Requisitos Previos
- MySQL versión 5.7 o superior instalado
- MySQL Server ejecutándose localmente en puerto 3306

## Instalación de MySQL (si no está instalado)

### En Ubuntu/Debian:
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### En macOS (con Homebrew):
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

### En Windows:
- Descargar desde https://dev.mysql.com/downloads/mysql/
- Ejecutar el instalador

## Configuración Inicial

### 1. Acceder a MySQL como root:
```bash
mysql -u root -p
# Ingrese la contraseña configurada durante la instalación
```

### 2. Crear la base de datos:
```sql
CREATE DATABASE IF NOT EXISTS world_cup_predictor 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Crear usuario (opcional, pero recomendado para producción):
CREATE USER 'worldcup'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON world_cup_predictor.* TO 'worldcup'@'localhost';
FLUSH PRIVILEGES;

EXIT;
```

## Configuración en application.properties

El archivo `/src/main/resources/application.properties` ya está configurado con:

```properties
# Datasource MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/world_cup_predictor?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
```

### Si usaste un usuario diferente:
Modifica el archivo `application.properties`:
```properties
spring.datasource.username=worldcup
spring.datasource.password=tu_contraseña_segura
```

## Validar Conexión

Puedes verificar que la base de datos se creó correctamente:

```bash
mysql -u root -e "USE world_cup_predictor; SHOW TABLES;"
```

Deberías ver:
```
+----------------------------------+
| Tables_in_world_cup_predictor   |
+----------------------------------+
| predictions                      |
| roles                            |
| teams                            |
| user_roles                       |
| users                            |
+----------------------------------+
```

## Ejecutar la Aplicación

Una vez MySQL esté configurado:

```bash
cd BackendPredictor
java -jar target/BackendPredictor-0.0.1-SNAPSHOT.jar
```

La aplicación:
1. Se conectará a MySQL
2. Creará las tablas automáticamente (si no existen)
3. Cargará:
   - Roles: ROLE_USER, ROLE_ADMIN
   - Usuarios de prueba:
     - usuario / usuario123 (rol: ROLE_USER)
     - administrador / admin123 (rol: ROLE_USER, ROLE_ADMIN)
   - 32 equipos de fútbol organizados por región:
     - CONCACAF (América del Norte): 3 equipos
     - UEFA (Europa): 16 equipos
     - CONCACAF-CARIBBEAN (Caribe): 3 equipos
     - CAF (África): 10 equipos
     - AFC (Asia/Oceanía): 10 equipos

## Endpoints Disponibles

### Teams (Equipos)
- `GET /api/teams` - Obtener todos los equipos
- `GET /api/teams/region/{region}` - Obtener equipos por región
- `GET /api/teams/by-name/{name}` - Obtener equipo por nombre
- `GET /api/teams/count` - Contar total de equipos
- `GET /api/teams/regions` - Listar regiones disponibles

### Authentication (Autenticación)
- `POST /api/auth/register` - Registrar nuevo usuario
  - Ejemplo:
  ```json
  {
    "name": "Mi Nombre",
    "username": "miusuario",
    "password": "micontraseña"
  }
  ```

## Verificar que funciona

```bash
# Obtener todos los equipos
curl http://localhost:8080/api/teams

# Obtener equipos de UEFA
curl http://localhost:8080/api/teams/region/UEFA

# Contar equipos
curl http://localhost:8080/api/teams/count
```

## Troubleshooting

### Error: "Access denied for user 'root'@'localhost'"
- Verifica que MySQL está en ejecución: `mysql --version`
- Inicia MySQL: `sudo service mysql start` (Linux) o `brew services start mysql` (macOS)
- Verifica la contraseña en application.properties

### Error: "The server time zone value 'UTC' is unrecognized"
- Este error ya está manejado en la URL de conexión con `&serverTimezone=UTC`
- Si persiste, asegúrate de tener MySQL actualizado

### Error: "Unknown database 'world_cup_predictor'"
- Crea la base de datos manualmente (ver sección "Crear la base de datos")
- O asegúrate que MySQL está en ejecución y accesible

## Datos Cargados Automáticamente

### Equipos por Región:
- **CONCACAF**: Canadá, Estados Unidos, México
- **UEFA**: Alemania, Austria, Bélgica, Bosnia y Herzegovina, Croacia, Escocia, España, Francia, Inglaterra, Noruega, Países Bajos, Portugal, República Checa, Suecia, Suiza, Turquía
- **CONCACAF-CARIBBEAN**: Curazao, Haití, Panamá
- **CAF**: Argelia, Cabo Verde, Costa de Marfil, Egipto, Ghana, Marruecos, RD Congo, Senegal, Sudáfrica, Túnez
- **AFC**: Arabia Saudí, Australia, Corea del Sur, Emiratos Árabes Unidos, Irak, Irán, Japón, Jordania, Uzbekistán, Nueva Zelanda

Total: 32 equipos

---

Última actualización: 2026-06-09

