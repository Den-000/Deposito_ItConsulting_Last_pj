import { highlightSidebar, formatDate } from "./ui.js";

const BASE_URL = "http://localhost:8081";

const id = new URLSearchParams(window.location.search).get("id");

if (!id) {
  throw new Error("Missing ticket id in URL");
}

/* =========================
   LOAD TICKET
========================= */
async function loadTicket() {
  const res = await fetch(`${BASE_URL}/tickets/${id}`, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  if (!res.ok) {
    console.error("Errore caricamento ticket");
    return;
  }

  const t = await res.json();

  document.getElementById("ticketId").innerText = t.id;
  document.getElementById("eventName").innerText = t.eventName;
  document.getElementById("email").innerText = t.email;
  document.getElementById("purchaseDate").innerText = formatDate(t.purchaseDate);
  document.getElementById("valid").innerText = t.valid ? "✅" : "❌";
  document.getElementById("checkedIn").innerText = t.checkedIn ? "✅" : "❌";
  document.getElementById("ticketType").innerText = t.ticketTypeName;
    document.getElementById("price").innerText = t.price ?? "-";

    document.getElementById("paymentAmount").innerText = t.paymentAmount ?? "-";
    document.getElementById("paymentMethod").innerText = t.paymentMethod ?? "-";
    document.getElementById("paymentStatus").innerText = t.paymentStatus ?? "-";
}

/* =========================
   ACTIONS
========================= */

async function checkIn() {
  await fetch(`${BASE_URL}/tickets/${id}/checkin`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadTicket();
}

async function invalidate() {
  await fetch(`${BASE_URL}/tickets/${id}/invalidate`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadTicket();
}

async function deleteTicket() {
  if (!confirm("Sei sicuro di eliminare questo ticket?")) return;

  await fetch(`${BASE_URL}/tickets/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  window.history.back();
}

/* =========================
   INIT
========================= */
loadTicket();
highlightSidebar();

/* expose */
window.checkIn = checkIn;
window.invalidate = invalidate;
window.deleteTicket = deleteTicket;