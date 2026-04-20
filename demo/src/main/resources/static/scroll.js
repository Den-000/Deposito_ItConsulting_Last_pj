import { state } from "./state.js";
import { loadEvents } from "./events.js";

/**
 * INFINITE SCROLL
 * carica automaticamente nuovi eventi quando si arriva in fondo alla pagina
 */
export function initScroll() {
  window.addEventListener("scroll", () => {
    if (state.loading || state.finished) return;
    if (state.mode !== "all") return;

    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const fullHeight = document.documentElement.scrollHeight;

    // quando siamo vicini al fondo pagina
    if (scrollTop + windowHeight >= fullHeight - 200) {
      loadEvents();
    }
  });
}