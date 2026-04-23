export function getPageSize() {
  const width = window.innerWidth;

  if (width < 600) return 6;
  if (width < 1000) return 12;
  return 24;
}

export function formatDate(date) {
  return new Date(date).toLocaleString("it-IT", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
}

function getStatusBadge(status) {
  const value = (status || "").toString().toUpperCase();

  if (value === "ACTIVE") {
    return "medium";
  }

  if (value === "COMPLETED") {
    return "low";
  }

  return "medium";
}

function getStatusLabel(status) {
  const value = (status || "").toString().toUpperCase();

  if (value === "ACTIVE") return "ATTIVO";
  if (value === "COMPLETED") return "COMPLETATO";

  return value;
}

function getEventImage(eventName) {
  const name = (eventName || "").toLowerCase();

  if (name.includes("concerto") || name.includes("rock") || name.includes("live")) {
    return "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?q=80&w=1200&auto=format&fit=crop";
  }

  if (name.includes("teatro") || name.includes("spettacolo")) {
    return "https://images.unsplash.com/photo-1503095396549-807759245b35?q=80&w=1200&auto=format&fit=crop";
  }

  return "https://images.unsplash.com/photo-1511578314322-379afb476865?q=80&w=1200&auto=format&fit=crop";
}

export function render(events) {
  const container = document.getElementById("main");

  events.forEach(e => {
    const div = document.createElement("div");
    div.classList.add("card", "event-card");

    const imageUrl = getEventImage(e.name);
    const badgeClass = getStatusBadge(e.status);

    div.innerHTML = `
      <div class="event-card-body">
        <h3 class="event-title">${e.name}</h3>
      </div>

      <div class="event-card-image">
        <img src="${imageUrl}" alt="${e.name}">
      </div>

      <div class="event-card-meta under">
        <div class="event-meta-item">
          <small>${formatDate(e.date)}</small>
        </div>
        <div class="event-meta-item">
          <small>${e.location.name}, ${e.location.city}</small>
        </div>
      </div>

      <div class="card-footer event-card-footer">
        <button class="ticket-btn">BIGLIETTI</button>
        <div class="badge ${badgeClass}">${getStatusLabel(e.status)}</div>
      </div>
    `;

    container.appendChild(div);

    const btn = div.querySelector(".ticket-btn");

    btn.onclick = () => {
      window.goToEventPage(e.id);
    };
  });
}

export function clearUI() {
  document.getElementById("main").innerHTML = "";
}

/**
 * EVIDENZIA LA PAGINA CORRENTE NELLA SIDEBAR (per admin e user)
 */
export function highlightSidebar() {
  let path = window.location.pathname;

  // normalizza dettaglio -> dashboard
  if (path.includes("detailUser")) path = "dashboardUsers";
  else if (path.includes("detailTicket")) path = "dashboardUsers";
  else if (path.includes("detailPayment")) path = "dashboardUsers";
  else if (path.includes("detailEvent")) path = "dashboardEvents";
  else if (path.includes("detailTicketsType")) path = "dashboardEvents";
  else if (path.includes("detailLocation")) path = "dashboardLocations";

  document.querySelectorAll(".nav-item").forEach(el => {
    if (path.includes(el.dataset.page)) {
      el.classList.add("active");
    }
  });
}