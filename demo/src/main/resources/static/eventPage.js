import { formatDate } from "./ui.js";

// prendi ID dall'URL
const params = new URLSearchParams(window.location.search);
const eventId = params.get("id");

// endpoint backend (ATTENZIONE: devi averlo nel controller)
const url = `/events/${eventId}`;

/**
 * CARICA EVENTO
 */
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
    homeBtn.onclick = () => {
      window.location.href = "/home.html";
    };

  } catch (err) {
    console.error("Errore caricamento evento:", err);
  }
}

/**
 * RENDER EVENTO
 */
function renderEvent(event) {
  document.getElementById("name").textContent = event.name;
  document.getElementById("description").textContent = event.description || "";

  document.getElementById("date").textContent = formatDate(event.date);

  document.getElementById("location").textContent =
    `${event.location?.address || ""}, ${event.location?.name || ""}, ${event.location?.city || ""}`;

  document.getElementById("status").textContent = `${event.bookedSeats}/${event.maxSeats}`;

  document.getElementById("seats").textContent =
  event.status || "";

  renderTicketTypes(event.ticketTypes || []);
}

/**
 * RENDER BIGLIETTI
 */
function renderTicketTypes(ticketTypes) {
  const container = document.getElementById("ticketsList");
  container.innerHTML = "";

  ticketTypes.forEach(t => {
    const div = document.createElement("div");
    div.className = "event";

    div.innerHTML = `
      <div>
        <strong>${t.name}</strong><br/>
        €${t.price}
      </div>

      <div>
        Disponibili: ${t.availableSeats}
      </div>

      <button class="book-btn">PRENOTA</button>
    `;

    div.querySelector(".book-btn").onclick = () => {
      console.log("Prenota:", t.type);
    };

    container.appendChild(div);
  });
}

// INIT
loadEvent();