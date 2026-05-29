/**
 * Módulo de API
 * Maneja todas las llamadas HTTP al backend
 */

class ApiClient {
  constructor() {
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  /**
   * Realizar una petición HTTP genérica
   * @param {string} endpoint - Endpoint a llamar
   * @param {string} method - Método HTTP (GET, POST, PUT, DELETE)
   * @param {object} data - Datos para enviar
   * @param {object} options - Opciones adicionales
   * @returns {Promise}
   */
  async request(endpoint, method = 'GET', data = null, options = {}) {
    const url = getApiUrl(endpoint);
    const headers = { ...this.defaultHeaders, ...options.headers };

    // Agregar token JWT si existe
    if (authManager.isAuthenticated()) {
      headers['Authorization'] = `Bearer ${authManager.getToken()}`;
    }

    const config = {
      method,
      headers,
      timeout: CONFIG.REQUEST_TIMEOUT,
      ...options,
    };

    if (data && (method === 'POST' || method === 'PUT')) {
      config.body = JSON.stringify(data);
    }

    try {
      debugLog(`Llamada API: ${method} ${url}`, data);

      const response = await fetch(url, config);

      // Si token expirado, intentar refrescar
      if (response.status === 401) {
        const refreshed = await authManager.refreshAccessToken();
        if (refreshed) {
          // Reintentar la llamada con el nuevo token
          return this.request(endpoint, method, data, options);
        } else {
          window.location.href = '/html/login.html'; // Redirigir a login
          throw new Error('No autorizado - sesión expirada');
        }
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
      }

      const result = await response.json().catch(() => ({}));
      debugLog(`Respuesta API: ${method} ${url}`, result);
      return result;
    } catch (error) {
      debugLog(`Error en API: ${method} ${url}`, error.message);
      throw error;
    }
  }

  /**
   * GET request
   * @param {string} endpoint
   * @param {object} options
   * @returns {Promise}
   */
  get(endpoint, options = {}) {
    return this.request(endpoint, 'GET', null, options);
  }

  /**
   * POST request
   * @param {string} endpoint
   * @param {object} data
   * @param {object} options
   * @returns {Promise}
   */
  post(endpoint, data, options = {}) {
    return this.request(endpoint, 'POST', data, options);
  }

  /**
   * PUT request
   * @param {string} endpoint
   * @param {object} data
   * @param {object} options
   * @returns {Promise}
   */
  put(endpoint, data, options = {}) {
    return this.request(endpoint, 'PUT', data, options);
  }

  /**
   * DELETE request
   * @param {string} endpoint
   * @param {object} options
   * @returns {Promise}
   */
  delete(endpoint, options = {}) {
    return this.request(endpoint, 'DELETE', null, options);
  }
}

/**
 * Servicio de Predicciones
 */
class PredictionService {
  /**
   * Obtener lista de equipos disponibles
   * @returns {Promise}
   */
  static async getTeams() {
    const api = new ApiClient();
    return api.get(CONFIG.ENDPOINTS.TEAMS.LIST);
  }

  /**
   * Buscar equipos
   * @param {string} query - Término de búsqueda
   * @returns {Promise}
   */
  static async searchTeams(query) {
    const api = new ApiClient();
    return api.get(`${CONFIG.ENDPOINTS.TEAMS.SEARCH}?q=${encodeURIComponent(query)}`);
  }

  /**
   * Realizar predicción
   * @param {string} teamHome - Equipo local
   * @param {string} teamAway - Equipo visitante
   * @returns {Promise}
   */
  static async predictMatch(teamHome, teamAway) {
    const api = new ApiClient();
    return api.post(CONFIG.ENDPOINTS.MATCHES.PREDICT, {
      homeTeam: teamHome,
      awayTeam: teamAway,
    });
  }

  /**
   * Obtener lista de predicciones del usuario
   * @returns {Promise}
   */
  static async getMatchHistory() {
    const api = new ApiClient();
    return api.get(CONFIG.ENDPOINTS.MATCHES.HISTORY);
  }

  /**
   * Obtener lista de partidos disponibles
   * @returns {Promise}
   */
  static async getMatches() {
    const api = new ApiClient();
    return api.get(CONFIG.ENDPOINTS.MATCHES.LIST);
  }
}

/**
 * Servicio de Usuario
 */
class UserService {
  /**
   * Obtener perfil del usuario
   * @returns {Promise}
   */
  static async getProfile() {
    const api = new ApiClient();
    return api.get(CONFIG.ENDPOINTS.USER.PROFILE);
  }

  /**
   * Actualizar perfil del usuario
   * @param {object} userData - Datos a actualizar
   * @returns {Promise}
   */
  static async updateProfile(userData) {
    const api = new ApiClient();
    return api.put(CONFIG.ENDPOINTS.USER.UPDATE, userData);
  }
}

// Crear instancia global de ApiClient
const apiClient = new ApiClient();

