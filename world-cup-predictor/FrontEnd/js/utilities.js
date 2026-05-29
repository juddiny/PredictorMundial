/**
 * Funciones Utilitarias
 * Helpers generales para el frontend
 */

/**
 * Formatear fecha a formato legible
 * @param {string|Date} date - Fecha a formatear
 * @param {string} locale - Idioma (por defecto 'es-ES')
 * @returns {string}
 */
function formatDate(date, locale = 'es-ES') {
  const d = new Date(date);
  return d.toLocaleDateString(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

/**
 * Formatear hora
 * @param {string|Date} date - Fecha a formatear
 * @param {string} locale - Idioma
 * @returns {string}
 */
function formatTime(date, locale = 'es-ES') {
  const d = new Date(date);
  return d.toLocaleTimeString(locale, {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
}

/**
 * Validar email
 * @param {string} email
 * @returns {boolean}
 */
function validateEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Validar contraseña (mínimo 6 caracteres)
 * @param {string} password
 * @returns {boolean}
 */
function validatePassword(password) {
  return password && password.length >= 6;
}

/**
 * Validar username (3+ caracteres, sin espacios)
 * @param {string} username
 * @returns {boolean}
 */
function validateUsername(username) {
  const usernameRegex = /^[a-zA-Z0-9_-]{3,}$/;
  return usernameRegex.test(username);
}

/**
 * Truncar texto
 * @param {string} text - Texto a truncar
 * @param {number} maxLength - Longitud máxima
 * @returns {string}
 */
function truncateText(text, maxLength = 50) {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}

/**
 * Capitalizar primera letra
 * @param {string} text
 * @returns {string}
 */
function capitalize(text) {
  return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
}

/**
 * Debounce - Retrasar ejecución de función
 * @param {function} func
 * @param {number} wait - Millisegundos
 * @returns {function}
 */
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

/**
 * Throttle - Limitar ejecución de función
 * @param {function} func
 * @param {number} limit - Millisegundos
 * @returns {function}
 */
function throttle(func, limit) {
  let inThrottle;
  return function(...args) {
    if (!inThrottle) {
      func.apply(this, args);
      inThrottle = true;
      setTimeout(() => inThrottle = false, limit);
    }
  };
}

/**
 * Copiar texto al portapapeles
 * @param {string} text
 * @returns {Promise}
 */
function copyToClipboard(text) {
  return navigator.clipboard.writeText(text);
}

/**
 * Descargar archivo
 * @param {string} content - Contenido del archivo
 * @param {string} filename - Nombre del archivo
 * @param {string} type - Tipo MIME (por defecto 'text/plain')
 */
function downloadFile(content, filename, type = 'text/plain') {
  const element = document.createElement('a');
  element.setAttribute('href', `data:${type};charset=utf-8,${encodeURIComponent(content)}`);
  element.setAttribute('download', filename);
  element.style.display = 'none';
  document.body.appendChild(element);
  element.click();
  document.body.removeChild(element);
}

/**
 * Obtener parámetro de URL
 * @param {string} paramName - Nombre del parámetro
 * @returns {string|null}
 */
function getURLParam(paramName) {
  const url = new URLSearchParams(window.location.search);
  return url.get(paramName);
}

/**
 * Esperar (sleep)
 * @param {number} ms - Millisegundos
 * @returns {Promise}
 */
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Detener propagación de evento
 * @param {Event} event
 */
function stopEvent(event) {
  event.preventDefault();
  event.stopPropagation();
}

/**
 * Verificar si es dispositivo móvil
 * @returns {boolean}
 */
function isMobile() {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

/**
 * Generar ID único
 * @returns {string}
 */
function generateId() {
  return `_${Math.random().toString(36).substr(2, 9)}`;
}

/**
 * Convertir objeto a parámetros URL
 * @param {object} obj - Objeto con parámetros
 * @returns {string}
 */
function objectToParams(obj) {
  return Object.keys(obj)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(obj[key])}`)
    .join('&');
}

/**
 * Hacer scroll a elemento
 * @param {Element|string} element - Elemento o selector
 * @param {object} options - Opciones de scroll
 */
function scrollTo(element, options = { behavior: 'smooth', block: 'start' }) {
  if (typeof element === 'string') {
    element = document.querySelector(element);
  }
  if (element) {
    element.scrollIntoView(options);
  }
}

/**
 * Mostrar notificación del navegador
 * @param {string} title - Título
 * @param {object} options - Opciones
 */
function showNotification(title, options = {}) {
  if ('Notification' in window && Notification.permission === 'granted') {
    new Notification(title, options);
  }
}

/**
 * Pedir permiso para notificaciones
 * @returns {Promise}
 */
function requestNotificationPermission() {
  if ('Notification' in window && Notification.permission === 'default') {
    return Notification.requestPermission();
  }
  return Promise.resolve();
}

/**
 * Fromat número como moneda
 * @param {number} value - Valor a formatear
 * @param {string} currency - Código de moneda (USD, EUR, etc)
 * @param {string} locale - Idioma
 * @returns {string}
 */
function formatCurrency(value, currency = 'USD', locale = 'es-ES') {
  return new Intl.NumberFormat(locale, {
    style: 'currency',
    currency: currency
  }).format(value);
}

/**
 * Parsear JSON de forma segura
 * @param {string} jsonString
 * @param {*} defaultValue - Valor por defecto
 * @returns {*}
 */
function safeJsonParse(jsonString, defaultValue = null) {
  try {
    return JSON.parse(jsonString);
  } catch (error) {
    return defaultValue;
  }
}

export {
  formatDate,
  formatTime,
  validateEmail,
  validatePassword,
  validateUsername,
  truncateText,
  capitalize,
  debounce,
  throttle,
  copyToClipboard,
  downloadFile,
  getURLParam,
  sleep,
  stopEvent,
  isMobile,
  generateId,
  objectToParams,
  scrollTo,
  showNotification,
  requestNotificationPermission,
  formatCurrency,
  safeJsonParse
};

