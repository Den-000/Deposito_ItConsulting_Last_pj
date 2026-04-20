import { loadEvents, reset, loadAllEvents } from "./events.js";
import { handleSearch } from "./search.js";
import { initScroll } from "./scroll.js";
import { initResize } from "./resize.js";
import { toggleTheme } from "./utils.js";
import { state } from "./state.js";
import { logout } from "./auth.js";

/**
 * FUNZIONE PRINCIPALE DI INIZIALIZZAZIONE
 * 
 * Viene eseguita quando il DOM è pronto.
 * Serve a collegare tutti gli eventi della UI.
 */
function initApp() {
  console.log("DOM pronto");

  // form ricerca eventi
  document
    .getElementById("searchForm")
    .addEventListener("submit", handleSearch);

  // bottone "Eventi"
  document
    .getElementById("navEventsBtn")
    .addEventListener("click", loadAllEvents);

  // logout
  document
    .getElementById("topBarBtn")
    .addEventListener("click", logout);

  // inizializzazione stato UI
  reset();
  loadEvents();

  // infinite scroll
  initScroll();

  // gestione responsive delle dimensioni della UI
  initResize();
}

// esposizione globale per HTML
window.initApp = initApp;
window.toggleTheme = toggleTheme;

// esegue init quando il DOM è pronto
window.addEventListener("DOMContentLoaded", initApp);