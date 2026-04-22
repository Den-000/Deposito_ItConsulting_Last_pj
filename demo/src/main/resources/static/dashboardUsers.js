const BASE_URL = "http://localhost:8081";

import { highlightSidebar } from "./ui.js";

async function loadUsers() {
  const res = await fetch(BASE_URL + "/admin/users", {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  const users = await res.json();

  const tbody = document.getElementById("usersBody");

  tbody.innerHTML = users.map(u => `
    <tr>
      <td>${u.email}</td>
      <td>${u.role}</td>

      <td>
        <button class="btn-primary" onclick="promote('${u.email}')">Promote</button>
        <button class="btn-danger" onclick="ban('${u.email}')">Ban</button>
      </td>
    </tr>
  `).join("");
}

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

async function ban(email) {
  await fetch(`${BASE_URL}/admin/ban?email=${email}`, {
    method: "POST",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  });

  loadUsers();
}

window.promote = promote;
window.ban = ban;

highlightSidebar();
loadUsers();