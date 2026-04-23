const API = "http://localhost:8081";

async function register() {
  try {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirm").value;

    if (!email || !password || !confirm) {
      alert("Compila tutti i campi");
      return;
    }

    if (password !== confirm) {
      alert("Le password non coincidono");
      return;
    }

    const res = await fetch(`${API}/auth/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    });

    if (res.ok) {
      alert("Registrazione completata");
      window.location.href = "login.html";
    } else {
      alert("Registrazione fallita");
    }
  } catch (err) {
    console.error(err);
    alert("Errore server");
  }
}

document.getElementById("registerBtn").addEventListener("click", register);