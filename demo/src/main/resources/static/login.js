import { getUserRole } from "./auth.js";

const API = "http://localhost:8081";
// endpoint backend

async function login() {
  try {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const res = await fetch(`${API}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    });

    const data = await res.json();

    if (res.ok && data.access) {
      localStorage.setItem("token", data.access);

      const role = getUserRole();

      if (role === "ADMIN") {
        window.location.href = "admin.html";
      } else {
        window.location.href = "home.html";
      }

    } else {
      alert("Login fallito");
    }

  } catch (err) {
    console.error(err);
    alert("Errore server");
  }
}

// collega il bottone
document.getElementById("loginBtn").addEventListener("click", login);

async function register() {

  // prende valori input
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const confirm = document.getElementById("confirm").value;

  // controllo base frontend
  if (password !== confirm) {
    alert("Le password non coincidono");
    return;
  }

  // TODO aggiungere ulteriori controlli (es. password troppo corta, username vuoto, ecc.)
  // !! ATTENZIONE !! questi controlli devono essere fatti anche lato backend, poiché un utente malintenzionato potrebbe bypassare quelli frontend inviando richieste direttamente al backend (es. con Postman)
  // !! IMPORTANTE !! controllare che l'username non sia già presente nel database

  // invio richiesta registrazione al backend
  await fetch(`${API}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ email, password })
  });

  // feedback utente
  alert("Registrazione completata");

  // ritorno al login
  window.location.href = "login.html";
}