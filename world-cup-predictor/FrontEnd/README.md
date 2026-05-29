# World Cup Predictor - Frontend

Interfaz web moderna y responsiva para predicción de resultados de partidos de fútbol.

## ⚡ Inicio Rápido

### 1. Configurar API URL
Editar `js/config.js` y cambiar:
```javascript
API_BASE_URL: 'http://localhost:8080/api'
```

### 2. Servir la aplicación
```bash
# Python
python -m http.server 8000

# Node.js
npm install -g http-server && http-server

# PHP
php -S localhost:8000
```

### 3. Abrir en navegador
```
http://localhost:8000
```

## 📁 Estructura

```
├── index.html              # Landing page
├── css/styles.css          # Estilos personalizados
├── js/
│   ├── config.js          # Configuración
│   ├── auth.js            # Autenticación JWT
│   └── api.js             # Cliente HTTP
├── html/
│   ├── login.html         # Inicio de sesión
│   ├── register.html      # Registro
│   └── dashboard.html     # Panel principal
└── ESTRUCTURA.md          # Documentación completa
```

## 🎨 Tecnologías

- **HTML5**: Estructura semántica
- **CSS3**: Flexbox, Grid, Responsive Design
- **Bootstrap 5**: Framework CSS
- **JavaScript (ES6)**: Lógica interactiva
- **JWT**: Autenticación segura

## ✨ Características

✅ Login y Registro  
✅ Predicción de partidos en tiempo real  
✅ Selección de equipos  
✅ Historial de predicciones  
✅ Autenticación JWT  
✅ Interfaz responsiva  
✅ Gestión de errores  

## 📱 Responsividad

Diseño optimizado para:
- 📲 Móvil (320px+)
- 📱 Tablet (768px+)
- 💻 Desktop (1200px+)

## 🔐 Seguridad

- JWT en localStorage
- Validación de formularios
- Headers de autorización
- Refresco automático de tokens
- Protección contra XSS

## 🚀 Próximas Mejoras

- [ ] Gráficas de estadísticas
- [ ] Sistema de rankings
- [ ] Notificaciones en tiempo real
- [ ] PWA (Offline support)
- [ ] Dark mode
- [ ] Integración con redes sociales

## 📚 Documentación Completa

Ver [ESTRUCTURA.md](./ESTRUCTURA.md) para documentación detallada.

## 🤝 Contribuciones

Para contribuir, contacta al equipo de desarrollo.

---

**Versión:** 1.0.0 | **Estado:** En desarrollo

