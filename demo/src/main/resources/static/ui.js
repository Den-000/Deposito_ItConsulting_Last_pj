export function getPageSize() {
  const width = window.innerWidth;

  // adattamento responsive
  if (width < 600) return 6;
  if (width < 1000) return 12;
  return 24;
}

/**
 * FORMATTAZIONE DATA leggibile
 */
export function formatDate(date) {
  return new Date(date).toLocaleString();
}

/**
 * RENDER EVENTI NEL DOM
 */
export function render(events) {
  const container = document.getElementById("main");

  events.forEach(e => {
    const div = document.createElement("div");
    div.classList.add("card");

    div.innerHTML = `
      <div class="card-header">
        <small>${formatDate(e.date)}</small>
        <small>${e.location.name + ", " + e.location.city}</small>
      </div>
      <h3>${e.name}</h3>
      <div class="card-footer">
      <div><button class="ticket-btn">BIGLIETTI</button></div>
      
      
      <div class="badge medium">${e.status}</div>
      </div>
    `;
// TODO Cambiare da e.status a rapporto fra posti occupati e posti totali, con badge verde se < 50%, giallo se tra 50% e 80%, rosso se > 80%

    container.appendChild(div);
    
    const btn = div.querySelector(".ticket-btn");

    btn.onclick = () => {
      window.goToEventPage(e.id);
    };
  });
}

/**
 * PULIZIA INTERFACCIA
 */
export function clearUI() {
  document.getElementById("main").innerHTML = "";
}