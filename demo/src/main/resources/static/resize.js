import { state } from "./state.js";
import { getPageSize } from "./ui.js";
import { reset, loadEvents } from "./events.js";

/**
 * GESTIONE RESPONSIVE:
 * cambia numero di elementi per pagina in base alla larghezza schermo
 */
export function initResize() {
  window.addEventListener("resize", () => {
    const newSize = getPageSize();

    if (newSize !== state.size) {
      console.log("Resize → aggiorno size:", newSize);

      state.size = newSize;

      // se siamo in modalità "all" ricarichiamo dati
      if (state.mode === "all") {
        reset();
        loadEvents();
      }
    }
  });
}