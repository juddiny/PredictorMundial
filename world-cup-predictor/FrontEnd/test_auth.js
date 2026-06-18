// Test script para auth.js en Node (simulación de entorno navegador)
const fs = require('fs');
const vm = require('vm');
// Minimal CONFIG y utilidades que auth.js espera
global.CONFIG = {
  STORAGE: {
    TOKEN_KEY: 'jwt_token',
    REFRESH_TOKEN_KEY: 'refresh_token',
    USER_KEY: 'user_data'
  },
  DEBUG: true
};
global.debugLog = function(message, data = '') {
  if (global.CONFIG.DEBUG) console.log('[DEBUG]', message, data);
};
// Implementar btoa/atob para Node
global.btoa = function(str) { return Buffer.from(str, 'utf8').toString('base64'); };
global.atob = function(b64) { return Buffer.from(b64, 'base64').toString('utf8'); };
after = console.log;
// Simular localStorage simple
class LocalStorageMock {
  constructor() { this.store = {}; }
  getItem(key) { return this.store.hasOwnProperty(key) ? this.store[key] : null; }
  setItem(key, value) { this.store[key] = String(value); }
  removeItem(key) { delete this.store[key]; }
}
global.localStorage = new LocalStorageMock();
// Cargar y ejecutar auth.js
const code = fs.readFileSync(__dirname + '/js/auth.js', 'utf8');
vm.runInThisContext(code, { filename: 'auth.js' });
(async () => {
  console.log('\n--- PROBANDO LOGIN INCORRECTO ---');
  let res = await authManager.login('usuario', 'wrongpass');
  console.log(res);
  console.log('\n--- PROBANDO LOGIN CORRECTO USUARIO ---');
  res = await authManager.login('usuario', 'usuario123');
  console.log(res);
  console.log('\n--- PROBANDO LOGIN CORRECTO ADMIN ---');
  res = await authManager.login('administrador', 'admin123');
  console.log(res);
  console.log('\n--- PROBANDO LOGIN CON USUARIO REGISTRADO (registro simulado) ---');
  await authManager.register('Pepe', 'pepe', 'pepe123');
  res = await authManager.login('pepe', 'pepe123');
  console.log(res);
  console.log('\n--- TOKEN GUARDADO EN STORAGE ---');
  console.log('jwt_token:', localStorage.getItem('jwt_token'));
  console.log('user_data:', localStorage.getItem('user_data'));
})();
