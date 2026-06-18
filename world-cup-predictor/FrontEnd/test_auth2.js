const fs = require('fs');
const vm = require('vm');
global.CONFIG = { STORAGE: { TOKEN_KEY: 'jwt_token', REFRESH_TOKEN_KEY: 'refresh_token', USER_KEY: 'user_data' }, DEBUG: true };
global.debugLog = (m,d='') => { if (global.CONFIG.DEBUG) console.log('[DEBUG]', m, d); };
global.btoa = (s) => Buffer.from(s, 'utf8').toString('base64');
global.atob = (b) => Buffer.from(b, 'base64').toString('utf8');
class LocalStorageMock { constructor(){ this.store = {}; } getItem(k){ return this.store.hasOwnProperty(k)?this.store[k]:null } setItem(k,v){ this.store[k]=String(v);} removeItem(k){delete this.store[k];} }
global.localStorage = new LocalStorageMock();
const code = fs.readFileSync(__dirname + '/js/auth.js', 'utf8');
vm.runInThisContext(code, { filename: 'auth.js' });
(async () => {
  console.log('\n-- INTENTO LOGIN INCORRECTO --');
  try{ await authManager.login('usuario','wrongpass'); console.log('ERROR: deberia fallar'); } catch(e){ console.log('Falló como esperado:', e.message); }
  console.log('\n-- LOGIN USUARIO CORRECTO --');
  try{ const r = await authManager.login('usuario','usuario123'); console.log('OK:', r.user); } catch(e){ console.log('Unexpected error', e); }
  console.log('\n-- LOGIN ADMIN CORRECTO --');
  try{ const r = await authManager.login('administrador','admin123'); console.log('OK:', r.user); } catch(e){ console.log('Unexpected error', e); }
})();
