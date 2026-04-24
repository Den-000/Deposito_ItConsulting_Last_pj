import { toggleTheme, applySavedTheme } from "./utils.js";

window.toggleTheme = toggleTheme;

applySavedTheme();

const locationsBody = document.getElementById("locationsBody");
const searchForm = document.getElementById("searchForm");
const searchInput = document.getElementById("searchInput");

let allLocations = [];

function getToken() {
  const token = localStorage.getItem("token");

  if (!token || token.split(".").length !== 3) {
    window.location.href = "/login.html";
    return null;
  }

  return token.trim();
}

function renderLocations(locations) {
  locationsBody.innerHTML = "";

  if (!locations || locations.length === 0) {
    locationsBody.innerHTML = `
      <tr>
        <td colspan="4">Nessuna location trovata</td>
      </tr>
    `;
    return;
  }

  locations.forEach(location => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${location.name || "N/D"}</td>
      <td>${location.city || "N/D"}</td>
      <td>${location.address || "N/D"}</td>
      <td>
        <button class="btn-delete" onclick="deleteLocation(${location.id})">Elimina</button>
      </td>
    `;

    locationsBody.appendChild(tr);
  });
}

async function loadLocations() {
  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch("/locations", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error();

    allLocations = await res.json();
    renderLocations(allLocations);
  } catch {
    locationsBody.innerHTML = `
      <tr>
        <td colspan="4">Errore nel caricamento</td>
      </tr>
    `;
  }
}

searchForm.addEventListener("submit", e => {
  e.preventDefault();

  const query = searchInput.value.toLowerCase().trim();

  const filteredLocations = allLocations.filter(location =>
    (location.name || "").toLowerCase().includes(query) ||
    (location.city || "").toLowerCase().includes(query) ||
    (location.address || "").toLowerCase().includes(query)
  );

  renderLocations(filteredLocations);
});

window.deleteLocation = async function(id) {
  const confirmDelete = confirm("Vuoi eliminare questa location?");
  if (!confirmDelete) return;

  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch(`/locations/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error();

    loadLocations();
  } catch {
    alert("Errore eliminazione location");
  }
};

loadLocations();