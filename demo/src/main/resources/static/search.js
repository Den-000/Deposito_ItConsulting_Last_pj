import { state } from "./state.js";
import { fetchEvents } from "./api.js";
import { render } from "./ui.js";
import { reset } from "./events.js";

/**
 * GESTIONE RICERCA EVENTI
 */
export async function handleSearch(event) {
  event.preventDefault();

  // valore input ricerca
  const value = document.getElementById("searchInput").value;

  // attiva modalità search
  state.mode = "search";
  state.currentQuery = value;

  // reset UI
  reset();

  // chiamata backend
  const data = await fetchEvents();

  const events = Array.isArray(data) ? data : [];

  // mostra risultati
  render(events);

  // in search non si usa paginazione
  state.finished = true;
}