/**
 * Módulo de Autenticación con JWT
 * Maneja login, registro y tokens
 */

class AuthManager {
  constructor() {
    this.token = this.getStoredToken();
    this.refreshToken = this.getStoredRefreshToken();
    this.user = this.getStoredUser();
  }

  /**
   * Realizar login
   * @param {string} username - Nombre de usuario
   * @param {string} password - Contraseña
   * @returns {Promise}
   */
  async login(username, password) {
    try {
      const response = await fetch(getApiUrl(CONFIG.ENDPOINTS.AUTH.LOGIN), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
        timeout: CONFIG.REQUEST_TIMEOUT,
      });

      if (!response.ok) {
        throw new Error(`Error de login: ${response.statusText}`);
      }

      const data = await response.json();

      // Guardar tokens y datos de usuario
      this.setTokens(data.accessToken, data.refreshToken);
      this.setUser(data.user);

      debugLog('Login exitoso', data.user);
      return { success: true, user: data.user };
    } catch (error) {
      debugLog('Error en login', error);
      throw error;
    }
  }

  /**
   * Realizar registro
   * @param {string} name - Nombre completo
   * @param {string} username - Nombre de usuario
   * @param {string} password - Contraseña
   * @returns {Promise}
   */
  async register(name, username, password) {
    try {
      const response = await fetch(getApiUrl(CONFIG.ENDPOINTS.AUTH.REGISTER), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, username, password }),
        timeout: CONFIG.REQUEST_TIMEOUT,
      });

      if (!response.ok) {
        throw new Error(`Error de registro: ${response.statusText}`);
      }

      const data = await response.json();
      debugLog('Registro exitoso', data);
      return { success: true, message: data.message };
    } catch (error) {
      debugLog('Error en registro', error);
      throw error;
    }
  }

  /**
   * Logout - Limpiar datos de sesión
   */
  logout() {
    localStorage.removeItem(CONFIG.STORAGE.TOKEN_KEY);
    localStorage.removeItem(CONFIG.STORAGE.REFRESH_TOKEN_KEY);
    localStorage.removeItem(CONFIG.STORAGE.USER_KEY);
    this.token = null;
    this.refreshToken = null;
    this.user = null;
    debugLog('Logout realizado');
  }

  /**
   * Verificar si el usuario está autenticado
   * @returns {boolean}
   */
  isAuthenticated() {
    return !!this.token;
  }

  /**
   * Obtener el token actual
   * @returns {string|null}
   */
  getToken() {
    return this.token;
  }

  /**
   * Guardar tokens en localStorage
   * @param {string} accessToken - Token de acceso
   * @param {string} refreshToken - Token de refresco
   */
  setTokens(accessToken, refreshToken) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    localStorage.setItem(CONFIG.STORAGE.TOKEN_KEY, accessToken);
    if (refreshToken) {
      localStorage.setItem(CONFIG.STORAGE.REFRESH_TOKEN_KEY, refreshToken);
    }
  }

  /**
   * Obtener token desde localStorage
   * @returns {string|null}
   */
  getStoredToken() {
    return localStorage.getItem(CONFIG.STORAGE.TOKEN_KEY);
  }

  /**
   * Obtener refresh token desde localStorage
   * @returns {string|null}
   */
  getStoredRefreshToken() {
    return localStorage.getItem(CONFIG.STORAGE.REFRESH_TOKEN_KEY);
  }

  /**
   * Guardar datos del usuario
   * @param {object} userData - Datos del usuario
   */
  setUser(userData) {
    this.user = userData;
    localStorage.setItem(CONFIG.STORAGE.USER_KEY, JSON.stringify(userData));
  }

  /**
   * Obtener datos del usuario
   * @returns {object|null}
   */
  getUser() {
    return this.user;
  }

  /**
   * Obtener datos del usuario desde localStorage
   * @returns {object|null}
   */
  getStoredUser() {
    const userData = localStorage.getItem(CONFIG.STORAGE.USER_KEY);
    return userData ? JSON.parse(userData) : null;
  }

  /**
   * Decodificar JWT para obtener información
   * @param {string} token - JWT token
   * @returns {object|null}
   */
  decodeToken(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64).split('').map((c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      debugLog('Error al decodificar token', error);
      return null;
    }
  }

  /**
   * Verificar si el token está expirado
   * @returns {boolean}
   */
  isTokenExpired() {
    const decoded = this.decodeToken(this.token);
    if (!decoded || !decoded.exp) return true;
    return decoded.exp * 1000 < Date.now();
  }

  /**
   * Refrescar el token
   * @returns {Promise}
   */
  async refreshAccessToken() {
    try {
      if (!this.refreshToken) {
        throw new Error('No refresh token disponible');
      }

      const response = await fetch(getApiUrl(CONFIG.ENDPOINTS.AUTH.REFRESH), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.refreshToken}`,
        },
        timeout: CONFIG.REQUEST_TIMEOUT,
      });

      if (!response.ok) {
        throw new Error('Error al refrescar token');
      }

      const data = await response.json();
      this.setTokens(data.accessToken, data.refreshToken);
      debugLog('Token refrescado exitosamente');
      return true;
    } catch (error) {
      debugLog('Error al refrescar token', error);
      this.logout(); // Logout si no se puede refrescar
      return false;
    }
  }
}

// Crear instancia global de AuthManager
const authManager = new AuthManager();

