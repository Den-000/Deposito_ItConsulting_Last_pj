export function getToken() {
  // recupera token JWT dal browser
  return localStorage.getItem("token");
}

/**
 * Genera header HTTP con autenticazione JWT.
 */
export function authHeaders() {
  const token = getToken();

  // se non c'è token → redirect login
  if (!token) {
    window.location.href = "login.html";
    return {};
  }

  // header standard per API protette
  return {
    "Authorization": "Bearer " + token,
    "Content-Type": "application/json"
  };
}

/**
 * Logout utente:
 * - elimina token
 * - torna alla pagina login
 */
export function logout() {
  localStorage.removeItem("token");
  window.location.href = "login.html";
}

export function getUserRole() {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.role;
  } catch {
    return null;
  }
}