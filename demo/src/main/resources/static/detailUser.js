const BASE_URL = "http://localhost:8081";
import { highlightSidebar } from "./ui.js";

const id = new URLSearchParams(window.location.search).get("id");

if (!id) {
  throw new Error("Missing user id in URL");
}

/* =========================
   LOAD USER
========================= */
async function loadUser() {
  const res = await fetch(`${BASE_URL}/admin/users/${id}`, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  if (!res.ok) throw new Error("User not found");

  const user = await res.json();

  document.getElementById("email").innerText = user.email;
  document.getElementById("role").innerText = user.role;
}

/* =========================
   LOAD TICKETS
========================= */
async function loadTickets() {
  const res = await fetch(`${BASE_URL}/admin/users/${id}/tickets`, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  const tickets = await res.json();
  console.log(tickets);

  const tbody = document.getElementById("ticketsBody");

  if (!Array.isArray(tickets)) {
    tbody.innerHTML = "<tr><td colspan='2'>No tickets</td></tr>";
    return;
  }

  tbody.innerHTML = tickets.map(t => `
    <tr class="record" data-id="${t.id}">
      <td>${t.id}</td>
      <td>${t.eventName}</td>
    </tr>
  `).join("");

  tbody.addEventListener("click", (e) => {
    const row = e.target.closest("tr");
    if (!row) return;

    const ticketId = row.dataset.id;
    goToTicket(ticketId);
  });
}

/* =========================
   ACTIONS
========================= */
async function promote() {
  await fetch(`${BASE_URL}/admin/users/${id}/promote`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadUser();
}

async function ban() {
  await fetch(`${BASE_URL}/admin/ban?id=${id}`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadUser();
}

async function deleteUser() {
  if (!confirm("Sei sicuro?")) return;

  await fetch(`${BASE_URL}/admin/users/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  window.location.href = "/dashboardUsers.html";
}

function goToTicket(id) {
  if (!id) {
    console.error("ID ticket mancante");
    return;
  }

  window.location.href = `/detailTicket.html?id=${encodeURIComponent(id)}`;
}

/* =========================
   INIT
========================= */
loadUser();
loadTickets();
loadPayments();
highlightSidebar();

/* expose */
window.promote = promote;
window.ban = ban;
window.deleteUser = deleteUser;