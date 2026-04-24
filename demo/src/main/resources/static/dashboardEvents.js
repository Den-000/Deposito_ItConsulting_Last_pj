import { toggleTheme, applySavedTheme } from "./utils.js";

window.toggleTheme = toggleTheme;

applySavedTheme();

const eventsBody = document.getElementById("eventsBody");
const searchForm = document.getElementById("searchForm");
const searchInput = document.getElementById("searchInput");

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

function formatLocation(location) {
  if (!location) return "N/D";

  const name = location.name || "";
  const city = location.city || "";

  if (name && city) return `${name}, ${city}`;
  if (name) return name;
  if (city) return city;

  return "N/D";
}

function renderEvents(events) {
  eventsBody.innerHTML = "";

  if (!events || events.length === 0) {
    eventsBody.innerHTML = `
      <tr>
        <td colspan="5">Nessun evento trovato</td>
      </tr>
    `;
    return;
  }

  events.forEach(event => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${event.name || "N/D"}</td>
      <td>${formatDate(event.date)}</td>
      <td>${formatLocation(event.location)}</td>
      <td>${event.status || "N/D"}</td>
      <td>
        <button class="btn-promote" onclick="editEvent(${event.id})">Modifica</button>
        <button class="btn-delete" onclick="deleteEvent(${event.id})">Elimina</button>
      </td>
    `;

    eventsBody.appendChild(tr);
  });
}

async function loadEvents(query = "") {
  try {
    const token = getToken();
    if (!token) return;

    let url = "/events/all";

    if (query) {
      url += `/search?name=${encodeURIComponent(query)}`;
    }

    const res = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error("Errore nel caricamento eventi");

    const data = await res.json();

    renderEvents(data.content);
  } catch (err) {
    eventsBody.innerHTML = `
      <tr>
        <td colspan="5">Errore nel caricamento</td>
      </tr>
    `;
  }
}

searchForm.addEventListener("submit", e => {
  e.preventDefault();
  loadEvents(searchInput.value);
});

window.editEvent = function(id) {
  window.location.href = `/event.html?id=${id}`;
};

window.deleteEvent = async function(id) {
  const confirmDelete = confirm("Vuoi eliminare questo evento?");
  if (!confirmDelete) return;

  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch(`/events/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error();

    loadEvents();
  } catch {
    alert("Errore eliminazione evento");
  }
};

loadEvents();