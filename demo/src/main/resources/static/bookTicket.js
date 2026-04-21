import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";

window.toggleTheme = toggleTheme;

const params = new URLSearchParams(window.location.search);
const ticketTypeId = params.get("ticketTypeId");
const eventId = params.get("eventId");

let selectedTicket = null;

function getToken() {
  const token = localStorage.getItem("token");
  if (!token || token.split(".").length !== 3) {
    window.location.href = "/login.html";
    return null;
  }
  return token.trim();
}

async function loadData() {
  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch(`/events/${eventId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error("Errore caricamento evento");

    const event = await res.json();

    document.getElementById("eventName").textContent = event.name;

    selectedTicket = event.ticketTypes?.find(t => String(t.id) === String(ticketTypeId));

    if (!selectedTicket) throw new Error("Ticket non trovato");

    document.getElementById("ticketName").textContent = selectedTicket.name;
    document.getElementById("price").textContent = `€ ${selectedTicket.price}`;

  } catch (err) {
    document.getElementById("statusMsg").textContent = err.message;
  }
}

async function buyTicket() {
  try {
    const token = getToken();
    if (!token) return;

    const email = document.getElementById("email").value.trim();
    const quantity = Number(document.getElementById("quantity").value);

    if (!email) throw new Error("Email obbligatoria");
    if (quantity < 1) throw new Error("Quantità non valida");

    const payload = {
      eventId: Number(eventId),
      ticketTypeId: Number(ticketTypeId),
      quantity,
      email
    };

    const res = await fetch("/tickets", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    });

    const data = await res.json().catch(() => null);

    if (!res.ok) {
      throw new Error(data?.message || "Errore acquisto ticket");
    }

    document.getElementById("statusMsg").textContent =
      "Pagamento e acquisto completati con successo!";

    setTimeout(() => {
      window.location.href = "/biglietti.html";
    }, 1200);

  } catch (err) {
    document.getElementById("statusMsg").textContent = err.message;
  }
}

function preloadEmail() {
  const token = localStorage.getItem("token");
  if (!token) return;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    document.getElementById("email").value = payload.sub || "";
  } catch {}
}

function init() {
  document.getElementById("buyBtn").addEventListener("click", buyTicket);

  applySavedTheme();
  initProfileMenu();
  preloadEmail();
  loadData();
}

window.addEventListener("DOMContentLoaded", init);