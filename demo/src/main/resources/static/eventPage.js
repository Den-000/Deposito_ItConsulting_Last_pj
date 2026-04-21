import { formatDate } from "./ui.js";
import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";

const params = new URLSearchParams(window.location.search);
const eventId = params.get("id");

const url = `/events/${eventId}`;

window.toggleTheme = toggleTheme;

async function loadEvent() {
  if (!eventId) {
    console.error("ID evento mancante");
    return;
  }

  try {
    const token = localStorage.getItem("token");

    const res = await fetch(url, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`);
    }

    const event = await res.json();
    renderEvent(event);

    const homeBtn = document.getElementById("goHome");
    if (homeBtn) {
      homeBtn.onclick = () => {
        window.location.href = "/home.html";
      };
    }

  } catch (err) {
    console.error("Errore caricamento evento:", err);
  }
}

function renderEvent(event) {
  document.getElementById("name").textContent = event.name;
  document.getElementById("description").textContent = event.description || "";

  document.getElementById("date").textContent = formatDate(event.date);

  document.getElementById("location").textContent =
    `${event.location?.address || ""}, ${event.location?.name || ""}, ${event.location?.city || ""}`;

  document.getElementById("seats").textContent = `${event.bookedSeats}/${event.maxSeats}`;

  document.getElementById("status").textContent = event.status || "";

  renderTicketTypes(event.ticketTypes || []);
}

function renderTicketTypes(ticketTypes) {
  const container = document.getElementById("ticketsList");
  container.innerHTML = "";

  ticketTypes.forEach(t => {
    const div = document.createElement("div");
    div.className = "card";

    div.innerHTML = `
      <div>
        <strong>${t.name}</strong>
        €${t.price}
      </div>
      <p>Aggiungere descrizione del pacchetto</p>
      <div>
        Disponibili: ${t.availableSeats}
      </div>
      <button class="book-btn">PRENOTA</button>
    `;

    div.querySelector(".book-btn").onclick = () => {
      console.log("Prenota:", t.name);
    };

    container.appendChild(div);
  });
}

applySavedTheme();
initProfileMenu();
loadEvent();