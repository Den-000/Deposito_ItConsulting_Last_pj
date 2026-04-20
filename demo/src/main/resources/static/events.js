import { state, resetState } from "./state.js";
import { fetchEvents } from "./api.js";
import { render, clearUI } from "./ui.js";
import { logout } from "./auth.js";

/**
 * CARICA EVENTI DAL BACKEND
 * (gestisce paginazione e ricerca)
 */
export async function loadEvents() {
  if (state.loading || state.finished) return;

  state.loading = true;

  try {
    const data = await fetchEvents();

    // backend può restituire:
    // - array diretto
    // - pagina Spring (content)
    const events = Array.isArray(data)
      ? data
      : (data.content || []);

    if (events.length === 0) {
      state.finished = true;
      return;
    }

    // render UI
    render(events);

    // gestione paginazione
    if (state.mode === "all") {
      state.page++;

      if (data.last === true) {
        state.finished = true;
      }
    } else {
      state.finished = true;
    }

  } catch (err) {
    // se token non valido → logout automatico
    if (err.status === 403) logout();
    console.error(err);
  } finally {
    state.loading = false;
  }
}

/**
 * PASSA ALLA MODALITÀ "TUTTI GLI EVENTI"
 */
export function loadAllEvents() {
  state.mode = "all";
  state.currentQuery = "";

  reset();
  loadEvents();
}

/**
 * RESET UI + STATO
 */
export function reset() {
  resetState();
  clearUI();
}