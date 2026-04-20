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
    div.classList.add("column");

    div.innerHTML = `
      <h3>${e.name}</h3>
      <small>${formatDate(e.date)}</small>
      <div class="badge medium">${e.status}</div>
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