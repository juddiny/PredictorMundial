# World Cup Predictor - Backend

Backend Spring Boot para el proyecto `world-cup-predictor`.

## Tecnologías
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring Security
- H2 Database
- JWT para autenticación

## Cómo ejecutar

1. Instala Maven si no está disponible localmente.
2. Desde el directorio `BackEnd` ejecuta:
   ```bash
   mvn clean package
   java -jar target/world-cup-predictor-backend-1.0.0.jar
   ```

## Endpoints principales

### Autenticación
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`

### Usuario
- `GET /api/user/profile`
- `PUT /api/user/update`

### Predicciones
- `GET /api/teams/list`
- `GET /api/teams/search?q=<texto>`
- `POST /api/matches/predict`
- `GET /api/matches/history`

## Base de datos
- `users`
- `user_roles`
- `predictions`

## H2 Console
- `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/worldcupdb`
- Usuario: `sa`
- Contraseña: ``
