import { authHeaders } from "./auth.js";
import { state } from "./state.js";

/**
 * URL base del backend Spring Boot.
 * Tutte le chiamate API partono da questo indirizzo.
 */
export const API = "http://localhost:8081";

/**
 * Recupera gli eventi dal backend.
 *
 * Gestisce due modalità:
 * 1. "all" → paginazione classica
 * 2. "search" → ricerca per nome
 *
 * @returns JSON con lista eventi o pagina eventi
 */
export async function fetchEvents() {
  let url;

  // MODALITÀ RICERCA
  if (state.mode === "search") {
    url = `${API}/events/search?name=${encodeURIComponent(state.currentQuery)}`;
  } 
  // MODALITÀ LISTA COMPLETA (paginata)
  else {
    url = `${API}/events/all?page=${state.page}&size=${state.size}`;
  }

  console.log("FETCH:", url);

  // richiesta HTTP al backend
  const res = await fetch(url, {
    headers: authHeaders()
  });

  // gestione errori HTTP
  if (!res.ok) {
    const err = new Error("HTTP ERROR");
    err.status = res.status;
    throw err;
  }

  // conversione risposta in JSON
  return await res.json();
}