import { toggleTheme, applySavedTheme } from "./utils.js";

window.toggleTheme = toggleTheme;

applySavedTheme();

const body = document.getElementById("notificationsBody");

function getToken() {
  const token = localStorage.getItem("token");

  if (!token || token.split(".").length !== 3) {
    window.location.href = "/login.html";
    return null;
  }

  return token.trim();
}

function formatDate(date) {
  if (!date) return "N/D";
  return new Date(date).toLocaleString("it-IT");
}

function render(tickets) {
  body.innerHTML = "";

  if (!tickets || tickets.length === 0) {
    body.innerHTML = `
      <tr>
        <td colspan="4">Nessuna notifica</td>
      </tr>
    `;
    return;
  }

  tickets.forEach(t => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${t.email || "N/D"}</td>
      <td>${t.eventName || "N/D"}</td>
      <td>${formatDate(t.eventDate)}</td>
      <td>${t.reminderSent ? "✔️ Inviato" : "❌ No"}</td>
    `;

    body.appendChild(tr);
  });
}

async function load() {
  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch("/tickets/notifications", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error();

    const data = await res.json();

    render(data);
  } catch {
    body.innerHTML = `
      <tr>
        <td colspan="4">Errore nel caricamento</td>
      </tr>
    `;
  }
}

load();