import { getPageSize } from "./ui.js";

/**
 * STATO GLOBALE DELL'APP FRONTEND
 * contiene tutte le variabili condivise tra moduli
 */
export let state = {
  page: 0,                 // pagina corrente
  size: getPageSize(),     // numero elementi per pagina
  loading: false,          // evita chiamate duplicate
  finished: false,         // indica fine dati
  mode: "all",             // "all" | "search"
  currentQuery: ""         // query di ricerca
};

/**
 * RESET PARZIALE DELLO STATO
 */
export function resetState() {
  state.page = 0;
  state.size = getPageSize();
  state.loading = false;
  state.finished = false;
}