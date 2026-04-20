const API = "http://localhost:8081";
// endpoint backend

async function login() {
  try {

    // prende valori input HTML
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // chiamata HTTP POST al backend
    const res = await fetch(`${API}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },

      // body inviato al backend in formato JSON
      body: JSON.stringify({ username, password })
    });

    // converte risposta backend in JSON
    const data = await res.json();

    console.log("LOGIN RESPONSE:", data);
    // per debug

    // se login OK ed access token presente
    if (res.ok && data.access) {

      // salva JWT nel browser
      localStorage.setItem("token", data.access);

      // redirect alla home
      window.location.href = "home.html";
    } else {
      // login fallito
      alert("Login fallito");
    }

  } catch (err) {
    // errore rete/server
    console.error(err);
    alert("Errore di rete e/o server");
  }
}

async function register() {

  // prende valori input
  const username = document.getElementById("username").value;
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
    body: JSON.stringify({ username, password })
  });

  // feedback utente
  alert("Registrazione completata");

  // ritorno al login
  window.location.href = "login.html";
}