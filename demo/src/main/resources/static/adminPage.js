import { loadAllEvents } from "./events.js";
import { handleSearch } from "./search.js";
import { logout, getUserRole } from "./auth.js";
import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";
import { highlightSidebar } from "./ui.js";

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
async function loadAdminPanel() {
  const panel = document.getElementById("adminPanel");

  if (!panel) {
    console.error("adminPanel non trovato nel DOM");
    return;
  }

  panel.style.display = "flex";

  // E' stato rimosso "Utenti attivi" vista la scadenza a breve termine del progetto
  // Volendo si potrebbe aggiungere Node.js + Socket.io backend, o far affidamento a Firebase
  // In alternativa si potrebbe creare un metodo che, ad intervallo di tot secondi, pinghi se l'utente è attivo o meno, ed aggiorna (ma non sarebbe realtime)
  try {
    // chiamata al backend (endpoint)
    const res = await fetch("/admin/stats", {
      headers: {
        "Authorization": "Bearer " + localStorage.getItem("token")
      }
    });
    const data = await res.json();

    const { totalUsers, totalAdmins } = data;

    panel.innerHTML = `
      <h3>📊 ADMIN PANEL</h3>

      <p>👥 Utenti totali: <strong>${totalUsers}</strong></p>
      <p>🛡️ Admin: <strong>${totalAdmins}</strong></p>

      <h4>Gestione utenti</h4>

      <input id="userInput" placeholder="Email">

      <div style="margin-top:10px;">
        <button id="btnPromote">Promuovi Admin</button>
      </div>
    `;

    // eventi
    document.getElementById("btnPromote").addEventListener("click", promoteUser);

  } catch (err) {
    console.error("Errore nel caricamento stats admin:", err);

    panel.innerHTML = `
      <h3>📊 ADMIN PANEL</h3>
      <p style="color:red;">Errore nel caricamento dati</p>
    `;
  }
}

/**
 * AZIONI ADMIN
 */
export async function promoteUser() {
  const user = document.getElementById("userInput").value;
  if (!user) return alert("Inserisci email");

  const res = await fetch(`/admin/promote?email=${user}`, {
    method: "POST",
    headers: {
      "Authorization": "Bearer " + localStorage.getItem("token")
    }
  });

  if (res.ok) {
    alert("Promosso ad ADMIN: " + user);
  } else {
    alert("Errore promozione");
  }
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
highlightSidebar();