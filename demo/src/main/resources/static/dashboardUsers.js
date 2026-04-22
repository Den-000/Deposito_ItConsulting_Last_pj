const BASE_URL = "http://localhost:8081";

import { highlightSidebar } from "./ui.js";

/* PAGE SETUP */
async function loadUsers() {
  const res = await fetch(BASE_URL + "/admin/users", {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  const users = await res.json();

  const tbody = document.getElementById("usersBody");

  tbody.innerHTML = users.map(u => `
    <tr data-id="${u.id}" class="record">
      <td>${u.email}</td>
      <td>${u.role}</td>
  
      <td>
        <button class="btn-primary" data-action="promote" data-email="${u.email}">
          Promuovi
        </button>
  
        <button class="btn-danger" data-action="deleteUser" data-id="${u.id}">
          Elimina
        </button>
      </td>
    </tr>
  `).join("");
}

/* EVENTS LISTENER */
const tbody = document.getElementById("usersBody");

tbody.addEventListener("click", (e) => {
  const row = e.target.closest("tr");
  if (!row) return;

  // se clicco un bottone → non navigo
  if (e.target.dataset.action === "promote") {
    e.stopPropagation();
    promote(e.target.dataset.email);
    return;
  }

  if (e.target.dataset.action === "deleteUser") {
    e.stopPropagation();
    deleteUser(e.target.dataset.id);
    return;
  }

  // click sulla riga → dettaglio user
  goToUser(row.dataset.id);
});

/* ACTIONS */
async function promote(email) {
  await fetch(`${BASE_URL}/admin/promote?email=${email}`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadUsers();
}

async function deleteUser(id) {
  if (!confirm("Sei sicuro?")) return;

  await fetch(`${BASE_URL}/admin/users/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadUsers();
}

function goToUser(id) {
  window.location.href = `/detailUser.html?id=${encodeURIComponent(id)}`;
}

/* EXPORTS */
window.promote = promote;
window.ban = deleteUser;

/* MAIN */
highlightSidebar();
loadUsers();