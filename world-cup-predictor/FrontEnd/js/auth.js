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
  /**
   * Realizar login (MOCK TEMPORAL PARA FASE 1 CON JWT VÁLIDO)
   */
  async login(username, password) {
    try {
      // GENERAMOS UN PAYLOAD DE JWT FALSO PERO CON EXPIRACIÓN EN EL FUTURO (Año 2030)
      // "exp": 1924905600 es el equivalente al 1 de Enero de 2030
      const mockPayload = {
        sub: username,
        name: "Usuario Invitado",
        roles: ["ROLE_USER"],
        iat: Math.floor(Date.now() / 1000),
        exp: 1924905600 
      };

      // Codificamos el payload en Base64 para que de verdad parezca un JWT real
      const base64Payload = btoa(JSON.stringify(mockPayload));
      
      // Construimos el Token Falso Completo (Header.Payload.Signature)
      const fakeAccessToken = `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.${base64Payload}.fakesignature123`;
      const fakeRefreshToken = "refresh.token.falso.456";
      const userSession = { name: "Usuario Invitado", username: username };

      // Guardar tokens y datos de usuario en el LocalStorage
      this.setTokens(fakeAccessToken, fakeRefreshToken);
      this.setUser(userSession);

      debugLog('Login exitoso simulado con JWT del futuro', userSession);
      return { success: true, user: userSession };
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
  /**
   * Realizar registro (MOCK TEMPORAL PARA FASE 1)
   */
  async register(name, username, password) {
    try {
      /* COMENTAMOS EL LLAMADO REAL PARA QUE NO SE ROMPA LA RED
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
      */

      // SIMULACIÓN: Guardamos el usuario inventado de forma local para usarlo en el login
      const mockUser = { id: 1, name: name, username: username };
      localStorage.setItem('mock_registered_user', JSON.stringify({ ...mockUser, password }));

      debugLog('Registro exitoso simulado', mockUser);
      return { success: true, message: "Usuario registrado con éxito en modo simulación." };
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

