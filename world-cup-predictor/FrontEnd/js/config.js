/**
 * Archivo de configuración global
 * Ajusta estas variables según tu ambiente (desarrollo, producción)
 */

const CONFIG = {
  // URL base de la API (Spring Boot Backend)
  API_BASE_URL: 'http://localhost:8080/api',

  // Endpoints disponibles
  ENDPOINTS: {
    AUTH: {
      LOGIN: '/auth/login',
      REGISTER: '/auth/register',
      REFRESH: '/auth/refresh',
    },
    MATCHES: {
      PREDICT: '/matches/predict',
      LIST: '/matches/list',
      HISTORY: '/matches/history',
    },
    TEAMS: {
      LIST: '/teams/list',
      SEARCH: '/teams/search',
    },
    USER: {
      PROFILE: '/user/profile',
      UPDATE: '/user/update',
    }
  },

  // Configuración de almacenamiento
  STORAGE: {
    TOKEN_KEY: 'jwt_token',
    REFRESH_TOKEN_KEY: 'refresh_token',
    USER_KEY: 'user_data',
  },

  // Timeouts
  REQUEST_TIMEOUT: 5000,

  // Configuración de desarrollo
  DEBUG: true,
};

// Función auxiliar para obtener URL completa de endpoint
function getApiUrl(endpoint) {
  return CONFIG.API_BASE_URL + endpoint;
}

// Función para registrar logs en modo debug
function debugLog(message, data = null) {
  if (CONFIG.DEBUG) {
    console.log(`[DEBUG] ${message}`, data || '');
  }
}

