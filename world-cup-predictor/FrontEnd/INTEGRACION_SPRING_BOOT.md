# Integración Frontend + Spring Boot + JWT

Esta guía explica cómo integrar este frontend con un backend Spring Boot autenticado con JWT.

## 📋 Requisitos

- Spring Boot 2.7+ (o 3.x)
- JWT (JSON Web Tokens) - usando library como jjwt
- CORS habilitado
- Base de datos (MySQL, PostgreSQL, H2, etc)

## 🔧 Configuración en Spring Boot

### 1. Agregar Dependencias (pom.xml)

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. Configurar CORS (CorsConfig.java)

```java
package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:8000", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

### 3. Configurar Properties (application.yml)

```yaml
spring:
  application:
    name: world-cup-predictor
  
  datasource:
    url: jdbc:mysql://localhost:3306/world_cup_db
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt:
  secret: tu-clave-secreta-muy-larga-y-segura
  expiration: 86400000  # 24 horas en millisegundos
  refresh-expiration: 604800000  # 7 días en millisegundos

server:
  port: 8080
  servlet:
    context-path: /
```

### 4. Estructura de Controladores

```java
// AuthController.java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Lógica de registro
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Lógica de login
        // Retornar: { accessToken, refreshToken, user }
        return ResponseEntity.ok(new LoginResponse(...));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String token) {
        // Refrescar token JWT
    }
}

// PredictionController.java
@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:8000")
public class PredictionController {
    
    @PostMapping("/predict")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> predict(@RequestBody PredictionRequest request) {
        // Llamar al agente IA para generar predicción
        // Retornar análisis con probabilidades
        return ResponseEntity.ok(new PredictionResponse(...));
    }
    
    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getHistory() {
        // Obtener historial de predicciones del usuario
    }
}

// TeamsController.java
@RestController
@RequestMapping("/api/teams")
public class TeamsController {
    
    @GetMapping("/list")
    public ResponseEntity<?> listTeams() {
        // Retornar lista de equipos disponibles
    }
}
```

## 🔐 Flujo de Autenticación

```
Frontend                           Backend
========                           ======

1. Usuario ingresa credenciales
   │
   ├─> POST /api/auth/login
   │   {username, password}
   │                              └─> Validar credenciales
   │                              └─> Generar JWT tokens
   │                              └─> Guardar refresh token (BD o Redis)
   │
   ←─────────────────────────────── 200 OK
       {
         accessToken: "eyJhbGc...",
         refreshToken: "eyJhbGc...",
         user: {id, name, username}
       }
   
2. Frontend guarda tokens
   - accessToken → localStorage
   - refreshToken → localStorage
   
3. Próximas peticiones
   │
   ├─> GET /api/matches/history
   │   Headers: { Authorization: "Bearer eyJhbGc..." }
   │                              └─> Validar JWT
   │                              └─> Procesar request
   │
   ←─────────────────────────────── 200 OK [datos]
   
4. Si token expira (401)
   │
   ├─> POST /api/auth/refresh
   │   { refreshToken: "eyJhbGc..." }
   │                              └─> Validar refresh token
   │                              └─> Generar nuevo accessToken
   │
   ←─────────────────────────────── 200 OK
       { accessToken: "nuevo_token" }
   
5. Llevar con nuevo token
   │
   ├─> GET /api/matches/history (reintentado)
   │   Headers: { Authorization: "Bearer nuevo_token" }
```

## 📊 Estructura de Respuestas

### Login Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "Juan García",
    "username": "juangarcia",
    "email": "juan@example.com"
  }
}
```

### Prediction Response
```json
{
  "homeScore": 2,
  "awayScore": 1,
  "homeWinProbability": 0.65,
  "drawProbability": 0.20,
  "awayWinProbability": 0.15,
  "confidence": 0.87,
  "timestamp": "2025-05-29T10:30:00Z"
}
```

### Teams Response
```json
[
  {
    "id": 1,
    "name": "Argentina",
    "code": "ARG",
    "ranking": 3
  },
  {
    "id": 2,
    "name": "Brasil",
    "code": "BRA",
    "ranking": 1
  }
]
```

## 🎯 Endpoints Mínimos Requeridos

### Autenticación
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Refrescar token

### Equipos
- `GET /api/teams/list` - Listar equipos
- `GET /api/teams/search?q=query` - Buscar equipos

### Predicciones
- `POST /api/matches/predict` - Realizar predicción
- `GET /api/matches/history` - Obtener historial

### Usuario
- `GET /api/user/profile` - Obtener perfil
- `PUT /api/user/update` - Actualizar perfil

## 🚀 Llamar al Agente IA

Desde Spring Boot, llamar al agente IA del ADK:

```java
@Service
public class PredictionService {
    
    public PredictionResult predict(String homeTeam, String awayTeam) {
        // Llamar al agente usando REST, gRPC, o librería ADK
        // El agente devuelve análisis y probabilidades
        
        // Ejemplo con RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String agentUrl = "http://localhost:5000/predict";
        
        PredictionRequest request = new PredictionRequest(homeTeam, awayTeam);
        PredictionResult result = restTemplate.postForObject(agentUrl, request, 
            PredictionResult.class);
        
        return result;
    }
}
```

## 🔍 Testing de API

### Con cURL
```bash
# Registro
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan","username":"juan","password":"123456"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"juan","password":"123456"}'

# Predicción (con token)
curl -X POST http://localhost:8080/api/matches/predict \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"homeTeam":"1","awayTeam":"2"}'
```

### Con Postman
1. Importar colección
2. Configurar variable {{baseUrl}} = http://localhost:8080
3. Ejecutar requests en orden
4. Postman guarda automáticamente tokens en variables

## ⚠️ Consideraciones de Seguridad

- ✅ Usar HTTPS en producción
- ✅ Validar JWT signature en backend
- ✅ Configurar CORS correctamente
- ✅ Implementar rate limiting
- ✅ Sanitizar inputs
- ✅ Usar contraseñas hasheadas (bcrypt)
- ✅ Implementar CSRF si usa cookies
- ✅ Logs de seguridad

## 🧪 Debugging

### Frontend
```javascript
// En config.js
CONFIG.DEBUG = true;  // Muestra llamadas API en consola
```

### Spring Boot
```properties
# En application.yml
logging:
  level:
    org.springframework.web: DEBUG
    com.example: DEBUG
```

## 📦 Deployment

### Frontend
- Servir archivos estáticos con nginx/apache
- O usar Spring Boot para servir estáticos

### Backend + Frontend Insieme
```xml
<!-- En pom.xml -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <configuration>
        <resources>
            <resource>
                <directory>../FrontEnd</directory>
                <targetPath>static</targetPath>
            </resource>
        </resources>
    </configuration>
</plugin>
```

## 📚 Referencias

- [JWT.io](https://jwt.io)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring CORS](https://spring.io/guides/gs/handling-form-submission/)
- [Mozilla MDN - JWT](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)

---

**Versión:** 1.0 | **Última actualización:** Mayo 2025

