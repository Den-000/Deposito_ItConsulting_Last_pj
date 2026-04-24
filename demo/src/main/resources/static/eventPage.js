import { formatDate, getEventImage } from "./ui.js";
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

  document.getElementById("location").textContent = [
    event.location?.address,
    event.location?.name,
    event.location?.city
  ].filter(Boolean).join(", ");

  const imageUrl = event.imageUrl && event.imageUrl.trim() !== ""
    ? event.imageUrl
    : getEventImage(event.name);

  document.getElementById("eventImage").src = imageUrl;

  const seatsEl = document.getElementById("seats");
  seatsEl.textContent = `Posti occupati: ${event.bookedSeats}/${event.maxSeats}`;

  const statusEl = document.getElementById("status");

  function getStatusLabel(status) {
    const value = (status || "").toUpperCase();

    if (value === "ACTIVE") return "ATTIVO";
    if (value === "COMING_SOON") return "NON DISPONIBILE";
    if (value === "COMPLETED") return "COMPLETATO";
    return value;
  }

  statusEl.textContent = getStatusLabel(event.status);

  renderTicketTypes(event.ticketTypes || [], event.status);
}

function getTicketDescription(ticketName) {
  const value = (ticketName || "").toLowerCase();

  if (value.includes("curva")) {
    return "Settore più caldo dello stadio, ideale per vivere la partita con il tifo più intenso.";
  }

  if (value.includes("distinti")) {
    return "Posti con buona visuale sul campo e atmosfera equilibrata tra tifo e comodità.";
  }

  if (value.includes("tribuna laterale")) {
    return "Settore laterale con visuale ampia sul campo e maggiore comfort durante l’evento.";
  }

  if (value.includes("tribuna centrale")) {
    return "Posti centrali con visuale privilegiata, pensati per seguire ogni azione al meglio.";
  }

  if (value.includes("hospitality")) {
    return "Esperienza premium con area dedicata, servizi esclusivi e massimo comfort.";
  }

  if (value.includes("vip")) {
    return "Accesso prioritario, posti migliori e area dedicata.";
  }

  if (value.includes("ridotto")) {
    return "Accesso a prezzo agevolato per bambini e anziani.";
  }

  if (value.includes("standard")) {
    return "Accesso completo all’evento con posto standard.";
  }

  return "Accesso all’evento con biglietto dedicato.";
}

function renderTicketTypes(ticketTypes, eventStatus) {
  const container = document.getElementById("ticketsList");
  container.innerHTML = "";

  ticketTypes.forEach(t => {
    const div = document.createElement("div");
    div.className = "ticket-card";

    if (eventStatus !== "ACTIVE") {
      div.classList.add("large");
    }

    const icon = t.name?.toLowerCase().includes("vip") ? "👑" : "⭐";

    div.innerHTML = `
      <div class="ticket-icon">${icon}</div>
      <h3>${t.name}</h3>
      <div class="ticket-price">€${Number(t.price).toFixed(2)}</div>
      <div class="ticket-divider"></div>
      <p class="ticket-description">${getTicketDescription(t.name)}</p>
      <div class="ticket-availability">Disponibili: <span>${t.availableSeats}</span></div>
      <button class="ticket-button">
        ${(eventStatus === "ACTIVE") ? "PRENOTA" : "NON DISPONIBILE"}
      </button>
    `;

    const btn = div.querySelector(".ticket-button");

    if (eventStatus === "ACTIVE") {
      btn.onclick = () => {
        window.location.href = `/bookTicket.html?eventId=${eventId}&ticketTypeId=${t.id}`;
      };
    } else {
      btn.disabled = true;
    }

    container.appendChild(div);
  });
}

applySavedTheme();
initProfileMenu();
loadEvent();