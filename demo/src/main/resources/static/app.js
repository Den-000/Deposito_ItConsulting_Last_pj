import { loadEvents, reset, loadAllEvents } from "./events.js";
import { handleSearch } from "./search.js";
import { initScroll } from "./scroll.js";
import { initResize } from "./resize.js";
import { state } from "./state.js";
import { logout } from "./auth.js";
import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";

/**
 * FUNZIONE PRINCIPALE DI INIZIALIZZAZIONE
 * 
 * Viene eseguita quando il DOM è pronto.
 * Serve a collegare tutti gli eventi della UI.
 */
function initApp() {
  applySavedTheme();
  console.log("DOM pronto");

  // form ricerca eventi
  const searchForm = document.getElementById("searchForm");
  if (searchForm) {
    searchForm.addEventListener("submit", handleSearch);
  }
  // bottone "Eventi"
  const navBtn = document.getElementById("navEventsBtn");
  if (navBtn) navBtn.addEventListener("click", loadAllEvents);

  // logout
  const logoutBtn = document.getElementById("topBarBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);

  // inizializzazione stato UI
  reset();
  loadEvents();

  // infinite scroll
  initScroll();

  // gestione responsive delle dimensioni della UI
  initResize();

  // menu profilo
  initProfileMenu();
}

// esposizione globale per HTML
window.initApp = initApp;
window.toggleTheme = toggleTheme;
window.goToEventPage = function(eventId) {
  if (!eventId) return;
  window.location.href = `/event.html?id=${encodeURIComponent(eventId)}`;
};

// esegue init quando il DOM è pronto
window.addEventListener("DOMContentLoaded", initApp);