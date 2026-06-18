# Resumen de Cambios - Migración a MySQL y Base de Datos de Equipos

## 🎯 Cambios Realizados

### 1. ✅ Configuración de MySQL
- **Archivo modificado**: `src/main/resources/application.properties`
  - Cambió de H2 (base de datos en memoria) a MySQL
  - URL de conexión: `jdbc:mysql://localhost:3306/world_cup_predictor`
  - Usuario: `root` (configurable)
  - Contraseña: vacía (configurable en application.properties)
  - Parámetro: `createDatabaseIfNotExist=true` permite crear la BD automáticamente

### 2. ✅ Creación de la Entidad Team
- **Archivo nuevo**: `src/main/java/com/example/backendpredictor/entity/Team.java`
  - Campos: `id`, `name` (único), `region`
  - Tablespoons: teams

### 3. ✅ Repositorio para Teams
- **Archivo nuevo**: `src/main/java/com/example/backendpredictor/repository/TeamRepository.java`
  - Métodos: `findByName()`, `findByRegion()`, `existsByName()`

### 4. ✅ Controlador REST para Teams
- **Archivo nuevo**: `src/main/java/com/example/backendpredictor/controller/TeamController.java`
  - Endpoints implementados:
    - `GET /api/teams` - Todos los equipos
    - `GET /api/teams/region/{region}` - Equipos por región
    - `GET /api/teams/by-name/{name}` - Equipo específico
    - `GET /api/teams/count` - Contar equipos
    - `GET /api/teams/regions` - Listar regiones

### 5. ✅ Actualización de DataInitializer
- **Archivo modificado**: `src/main/java/com/example/backendpredictor/init/DataInitializer.java`
  - Agregado: inyección de `TeamRepository`
  - Agregado: método `loadTeams()` para cargar todos los equipos
  - Agregado: método `loadTeamsByRegion()` para cargar equipos por región
  - Se cargan automáticamente 32 equipos al iniciar la aplicación

### 6. ✅ Script SQL de Inicialización
- **Archivo nuevo**: `src/main/resources/schema.sql`
  - Define tablas para MySQL (roles, users, teams, predictions, user_roles)
  - Se ejecuta automáticamente al iniciar (spring.sql.init.mode=always)

## 📊 Equipos Cargados (32 Total)

### CONCACAF (América del Norte) - 3 equipos
- Canadá
- Estados Unidos
- México

### UEFA (Europa) - 16 equipos
- Alemania, Austria, Bélgica, Bosnia y Herzegovina, Croacia, Escocia
- España, Francia, Inglaterra, Noruega, Países Bajos, Portugal
- República Checa, Suecia, Suiza, Turquía

### CONCACAF-CARIBBEAN (Caribe) - 3 equipos
- Curazao
- Haití
- Panamá

### CAF (África) - 10 equipos
- Argelia, Cabo Verde, Costa de Marfil, Egipto, Ghana
- Marruecos, RD Congo, Senegal, Sudáfrica, Túnez

### AFC (Asia/Oceanía) - 10 equipos
- Arabia Saudí, Australia, Corea del Sur, Emiratos Árabes Unidos
- Irak, Irán, Japón, Jordania, Uzbekistán, Nueva Zelanda

## 🗃️ Estructura de Base de Datos MySQL

```
Tabla: roles
- id (INT, PK, Auto)
- name (VARCHAR, UNIQUE)

Tabla: users
- id (INT, PK, Auto)
- name (VARCHAR)
- username (VARCHAR, UNIQUE)
- password (VARCHAR)
- created_at (TIMESTAMP)

Tabla: teams
- id (INT, PK, Auto)
- name (VARCHAR, UNIQUE)
- region (VARCHAR)

Tabla: predictions
- id (INT, PK, Auto)
- user_id (INT, FK → users)
- home_team (VARCHAR)
- away_team (VARCHAR)
- predicted_home_score (INT)
- predicted_away_score (INT)
- ai_analysis (LONGTEXT)
- created_at (TIMESTAMP)

Tabla: user_roles
- user_id (INT, PK, FK → users)
- role_id (INT, PK, FK → roles)
```

## 🚀 Próximos Pasos

1. **Instalar/Configurar MySQL** (ver MYSQL_SETUP.md)
2. **Ejecutar la aplicación**:
   ```bash
   cd BackendPredictor
   java -jar target/BackendPredictor-0.0.1-SNAPSHOT.jar
   ```
3. **Verificar que los datos se cargaron**:
   ```bash
   curl http://localhost:8080/api/teams
   curl http://localhost:8080/api/teams/region/UEFA
   curl http://localhost:8080/api/teams/count
   ```

## ✨ Cambios en application.properties

Antes (H2):
```properties
spring.datasource.url=jdbc:h2:mem:backenddb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
```

Después (MySQL):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/world_cup_predictor?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
spring.sql.init.mode=always
```

## 📝 Usuarios Predefinidos

Con las mismas credenciales de antes:
- **usuario** / usuario123 (rol: ROLE_USER)
- **administrador** / admin123 (rol: ROLE_ADMIN, ROLE_USER)

## 🔄 Diferencias con H2

| Aspecto | H2 | MySQL |
|--------|-----|-------|
| Persistencia | En memoria (se pierden datos) | Disco (datos persistentes) |
| Base de datos | Creada automáticamente | Requiere creación previa |
| Ideal para | Desarrollo y pruebas | Desarrollo, testing y producción |
| Configuración | Mínima | Más pasos iniciales |

## 📦 Dependencias Usadas

- MySQL Connector/J (driver MySQL para Java)
- Spring Data JPA
- Hibernate (ORM)
- Spring Security (con BCrypt)

---

Todos los cambios están listos. Solo falta **instalar/configurar MySQL** y los datos se cargarán automáticamente. 🎉

