# World Cup Predictor - Frontend

Interfaz web moderna para predicción de resultados de partidos de fútbol utilizando HTML5, CSS (Bootstrap) y JavaScript vanilla.

## 📁 Estructura del Proyecto

```
FrontEnd/
├── index.html                    # Landing page / Página principal
├── css/
│   └── styles.css               # Estilos personalizados (se complementan con Bootstrap)
├── js/
│   ├── config.js                # Configuración global (URLs de API, constantes)
│   ├── auth.js                  # Gestión de autenticación y JWT
│   ├── api.js                   # Cliente HTTP y servicios de API
│   └── main.js                  # Funciones globales (opcional)
├── html/
│   ├── login.html               # Página de inicio de sesión
│   ├── register.html            # Página de registro de usuario
│   └── dashboard.html           # Panel principal con predictor
└── assets/
    ├── images/                  # Imágenes (logos, iconos)
    └── fonts/                   # Fuentes personalizadas
```

## 🎯 Características Principales

### 1. **Autenticación JWT**
- Sistema robusto de autenticación con JWT
- Almacenamiento seguro de tokens en localStorage
- Refresco automático de tokens expirados
- Verificación de sesión en cada carga de página

### 2. **Gestión de Predicciones**
- Selección de equipos desde dropdowns
- Predicción en tiempo real mediante API
- Visualización de probabilidades y estadísticas
- Historial de predicciones guardadas

### 3. **Interfaz Responsiva**
- Diseño mobile-first
- Compatible con todos los navegadores modernos
- Temas personalizados con Bootstrap 5

### 4. **Manejo de Errores**
- Validación de formularios en cliente
- Mensajes de error claros y amigables
- Manejo de errores de conexión

## 🚀 Cómo Funciona

### Cliente HTTP (API)
El archivo `api.js` proporciona:
- `ApiClient`: Cliente genérico para peticiones HTTP
- `PredictionService`: Métodos para predicciones
- `UserService`: Métodos para perfil de usuario
- Manejo automático de JWT en headers
- Reintento de requests si el token expira

### Autenticación
El archivo `auth.js` proporciona:
- `AuthManager`: Gestión completa de autenticación
- `login()`: Autenticación de usuario
- `register()`: Registro de nuevo usuario
- `logout()`: Cierre de sesión
- `isTokenExpired()`: Verificar expiración
- `refreshAccessToken()`: Refrescar token JWT

### Configuración
El archivo `config.js` contiene:
- URLs base de la API
- Endpoints disponibles
- Claves de almacenamiento local
- Configuración de timeouts
- Modo debug

## 🔗 Endpoints Esperados del Backend

### Autenticación
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
```

### Equipos
```
GET /api/teams/list
GET /api/teams/search?q=query
```

### Predicciones
```
POST /api/matches/predict
GET /api/matches/list
GET /api/matches/history
```

### Usuario
```
GET /api/user/profile
PUT /api/user/update
```

## 📋 Estructura de Respuestas

### Login Success (200)
```json
{
  "accessToken": "jwt_token_here",
  "refreshToken": "refresh_token_here",
  "user": {
    "id": "user_id",
    "name": "Nombre Usuario",
    "username": "username"
  }
}
```

### Predicción (200)
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

## 🛠️ Instalación y Uso

### Requisitos
- Navegador moderno (Chrome, Firefox, Safari, Edge)
- Servidor web (Apache, Nginx, Node.js)
- Acceso a la API de backend

### Pasos

1. **Clonar o descargar el proyecto**
   ```bash
   cd FrontEnd
   ```

2. **Configurar la API**
   - Editar `js/config.js`
   - Cambiar `API_BASE_URL` a la URL de tu backend
   ```javascript
   const CONFIG = {
     API_BASE_URL: 'http://localhost:8080/api',  // Cambiar según necesario
     // ...
   };
   ```

3. **Servir los archivos**
   
   **Con Python:**
   ```bash
   python -m http.server 8000
   ```

   **Con Node.js:**
   ```bash
   npm install -g http-server
   http-server
   ```

   **Con PHP:**
   ```bash
   php -S localhost:8000
   ```

4. **Acceder en el navegador**
   ```
   http://localhost:8000
   ```

## 🔐 Seguridad

### Implementadas:
- ✅ Almacenamiento seguro de JWT
- ✅ Validación en cliente
- ✅ Refresco automático de tokens
- ✅ Headers de autorización en requests
- ✅ Verificación de sesión

### Recomendaciones:
- Usar HTTPS en producción
- Implementar CORS correctamente en backend
- Usar SameSite cookies si las usas
- Implementar rate limiting en backend
- Validar y sanitizar en backend también

## 📦 Dependencias

### Externas (via CDN):
- **Bootstrap 5.3.0**: Framework CSS
- **Popper.js**: (Incluido en Bootstrap)

### Internas:
- `config.js`: Configuración global
- `auth.js`: Autenticación
- `api.js`: Cliente HTTP

## 🎨 Personalización

### Cambiar Colores
Editar `:root` en `css/styles.css`:
```css
:root {
  --primary-color: #1e3c72;      /* Azul oscuro */
  --secondary-color: #2a5298;    /* Azul medio */
  --success-color: #28a745;      /* Verde */
  --danger-color: #dc3545;       /* Rojo */
  /* ... más colores ... */
}
```

### Cambiar Fuentes
En `css/styles.css` o directamente en HTML:
```html
<link href="https://fonts.googleapis.com/css2?family=TuFuente&display=swap" rel="stylesheet">
```

### Agregar Funcionalidades
1. Crear nuevos archivos en `js/`
2. Importarlos en el HTML correspondiente
3. Usar servicios existentes (`ApiClient`, `AuthManager`, etc.)

## 🧪 Testing

### Simular Respuestas
Para desarrollo sin backend, agregar en `js/`:
```javascript
// Mock data
const mockTeams = [
  { id: 1, name: 'Argentina' },
  { id: 2, name: 'Brasil' },
  // ...
];
```

## 📱 Responsive Design

Breakpoints configurados en Bootstrap:
- **xs**: < 576px (Mobile)
- **sm**: ≥ 576px (Tablet)
- **md**: ≥ 768px (Tablet grande)
- **lg**: ≥ 992px (Desktop)
- **xl**: ≥ 1200px (Desktop grande)

## 🔄 Flujo de Autenticación

```
1. Usuario abre index.html
   ↓
2. Verifica si tiene JWT válido
   ├─ Sí → Redirige a dashboard.html
   └─ No → Muestra login.html
   ↓
3. Usuario inicia sesión o se registra
   ↓
4. Backend retorna JWT tokens
   ↓
5. Frontend guarda tokens en localStorage
   ↓
6. Redirige a dashboard.html
   ↓
7. Dashboard carga datos autenticados
```

## 💡 Consejos para Mejorar

1. **Performance:**
   - Lazy loading de imágenes
   - Minificación de CSS/JS
   - Caché del navegador

2. **UX/UI:**
   - Agregar animaciones más fluidas
   - Tooltips informativos
   - Notificaciones push

3. **Escalabilidad:**
   - Usar módulos ES6 (si el backend lo permite)
   - Implementar PWA (Progressive Web App)
   - Service Workers para offline

4. **Funcionalidades:**
   - Comparativa de equipos
   - Gráficas históricas
   - Sistema de puntos/rankings
   - Compartir predicciones

## 🐛 Debugging

### Habilitar logs
En `config.js`:
```javascript
CONFIG.DEBUG = true;  // Muestra logs en consola
```

### Herramientas del Navegador
- F12 → Network: Ver requests HTTP
- F12 → Console: Ver errores y logs
- F12 → Application → Local Storage: Ver tokens

## 📞 Soporte

Para reportar bugs o sugerencias, contactar al equipo de desarrollo.

## 📄 Licencia

Proyecto de Universidad - Derecho Reservado

---

**Versión:** 1.0.0  
**Última actualización:** Mayo 2025

