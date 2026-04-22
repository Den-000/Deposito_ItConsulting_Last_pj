import { loadAllEvents } from "./events.js";
import { handleSearch } from "./search.js";
import { logout, getUserRole } from "./auth.js";
import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";

/**
 * INIT ADMIN APP
 */
function initApp() {
  applySavedTheme();
  console.log("ADMIN DOM pronto");

  // =========================
  // CONTROLLO RUOLO
  // =========================
  const role = getUserRole();

  if (!role) {
    window.location.href = "login.html";
    return;
  }

  if (role !== "ADMIN") {
    alert("Accesso non autorizzato");
    window.location.href = "home.html";
    return;
  }

  // =========================
  // UI EVENTS
  // =========================
  const searchForm = document.getElementById("searchForm");
  if (searchForm) {
    searchForm.addEventListener("submit", handleSearch);
  }

  const navBtn = document.getElementById("navEventsBtn");
  if (navBtn) navBtn.addEventListener("click", loadAllEvents);

  const logoutBtn = document.getElementById("topBarBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);

  // =========================
  // INIT UI
  // =========================
  initProfileMenu();

  loadAdminPanel();
}

/**
 * ADMIN PANEL
 */
function loadAdminPanel() {
  const panel = document.getElementById("adminPanel");

  if (!panel) {
    console.error("adminPanel non trovato nel DOM");
    return;
  }

  panel.style.display = "flex";

  panel.innerHTML = `
    <h3>📊 ADMIN PANEL</h3>

    <p>👥 Utenti totali: <strong>120</strong></p>
    <p>✅ Attivi: <strong>98</strong></p>
    <p>🛡️ Admin: <strong>5</strong></p>

    <h4>Gestione utenti</h4>

    <input id="userInput" placeholder="Username">

    <div style="margin-top:10px;">
      <button id="btnPromote">Promuovi Admin</button>
      <button id="btnBan">Banna Utente</button>
    </div>
  `;

  // eventi
  document.getElementById("btnPromote").addEventListener("click", promoteUser);
  document.getElementById("btnBan").addEventListener("click", banUser);
}

/**
 * AZIONI ADMIN
 */
function promoteUser() {
  const user = document.getElementById("userInput").value;
  if (!user) return alert("Inserisci username");

  alert("Promosso a ADMIN: " + user);
}

function banUser() {
  const user = document.getElementById("userInput").value;
  if (!user) return alert("Inserisci username");

  alert("Utente bannato: " + user);
}

/**
 * GLOBAL EXPORT
 */
window.initApp = initApp;
window.toggleTheme = toggleTheme;

/**
 * INIT
 */
window.addEventListener("DOMContentLoaded", initApp);