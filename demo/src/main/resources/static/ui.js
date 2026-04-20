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
      <div class="card-header" style="display: flex; flex-direction: row; gap: 0.5em; align-items: center; justify-content: space-between;">
        <small>${formatDate(e.date)}</small>
        <small>${e.location.name + ", " + e.location.city}</small>
      </div>
      <h3>${e.name}</h3>
      <div class="card-footer">
      <button class="badge small">${e.type}</button>
      <div class="badge medium">${e.status}</div>
      </div>
    `;

    container.appendChild(div);
  });
}

/**
 * PULIZIA INTERFACCIA
 */
export function clearUI() {
  document.getElementById("main").innerHTML = "";
}