import { initProfileMenu } from "./profile-menu.js";
import { toggleTheme, applySavedTheme } from "./utils.js";

window.toggleTheme = toggleTheme;

const params = new URLSearchParams(window.location.search);
const ticketTypeId = params.get("ticketTypeId");
const eventId = params.get("eventId");

let selectedTicket = null;
let accountEmail = "";

function getToken() {
  const token = localStorage.getItem("token");
  if (!token || token.split(".").length !== 3) {
    window.location.href = "/login.html";
    return null;
  }
  return token.trim();
}

async function loadData() {
  try {
    const token = getToken();
    if (!token) return;

    const res = await fetch(`/events/${eventId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error("Errore caricamento evento");

    const event = await res.json();

    document.getElementById("eventName").textContent = event.name;

    selectedTicket = event.ticketTypes?.find(t => String(t.id) === String(ticketTypeId));

    if (!selectedTicket) throw new Error("Ticket non trovato");

    document.getElementById("ticketName").textContent = selectedTicket.name;
    document.getElementById("price").textContent = `€ ${selectedTicket.price}`;
  } catch (err) {
    document.getElementById("statusMsg").textContent = err.message;
  }
}

function showStep(stepNumber) {
  const step1 = document.getElementById("step1");
  const step2 = document.getElementById("step2");
  const stepBadge1 = document.getElementById("stepBadge1");
  const stepBadge2 = document.getElementById("stepBadge2");

  if (stepNumber === 1) {
    step1.classList.add("active");
    step2.classList.remove("active");
    stepBadge1.classList.add("active");
    stepBadge2.classList.remove("active");
  } else {
    step1.classList.remove("active");
    step2.classList.add("active");
    stepBadge1.classList.add("active");
    stepBadge2.classList.add("active");
  }
}

function validateStep1() {
  const firstName = document.getElementById("firstName").value.trim();
  const lastName = document.getElementById("lastName").value.trim();
  const email = document.getElementById("email").value.trim();
  const statusMsg = document.getElementById("statusMsg");

  statusMsg.textContent = "";

  if (!firstName) {
    statusMsg.textContent = "Nome obbligatorio";
    return false;
  }

  if (!lastName) {
    statusMsg.textContent = "Cognome obbligatorio";
    return false;
  }

  if (!email) {
    statusMsg.textContent = "Email obbligatoria";
    return false;
  }

  return true;
}

function validatePaymentStep() {
  const cardName = document.getElementById("cardName").value.trim();
  const cardNumber = document.getElementById("cardNumber").value.trim();
  const expiry = document.getElementById("expiry").value.trim();
  const cvv = document.getElementById("cvv").value.trim();
  const statusMsg = document.getElementById("statusMsg");

  statusMsg.textContent = "";

  if (!cardName) {
    statusMsg.textContent = "Intestatario carta obbligatorio";
    return false;
  }

  if (!cardNumber) {
    statusMsg.textContent = "Numero carta obbligatorio";
    return false;
  }

  if (!expiry) {
    statusMsg.textContent = "Scadenza obbligatoria";
    return false;
  }

  if (!cvv) {
    statusMsg.textContent = "CVV obbligatorio";
    return false;
  }

  return true;
}

async function buyTicket() {
  try {
    const token = getToken();
    if (!token) return;

    if (!validatePaymentStep()) return;

    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const email = document.getElementById("email").value.trim();

    const payload = {
      eventId: Number(eventId),
      ticketTypeId: Number(ticketTypeId),
      quantity: 1,
      email,
      firstName,
      lastName
    };

    const res = await fetch("/tickets", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    });

    const data = await res.json().catch(() => null);

    if (!res.ok) {
      throw new Error(data?.message || "Errore acquisto ticket");
    }

    document.getElementById("statusMsg").textContent = "Pagamento e acquisto completati con successo!";

    setTimeout(() => {
      window.location.href = "/biglietti.html";
    }, 1200);
  } catch (err) {
    document.getElementById("statusMsg").textContent = err.message;
  }
}

function loadAccountEmail() {
  const token = localStorage.getItem("token");
  if (!token) return;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    accountEmail = payload.email || payload.sub || "";
  } catch {}
}

function autofillEmail() {
  if (!accountEmail) return;
  document.getElementById("email").value = accountEmail;
}

function formatCardNumber(value) {
  return value
    .replace(/\D/g, "")
    .slice(0, 16)
    .replace(/(.{4})/g, "$1 ")
    .trim();
}

function formatExpiry(value) {
  const digits = value.replace(/\D/g, "").slice(0, 4);
  if (digits.length <= 2) return digits;
  return `${digits.slice(0, 2)}/${digits.slice(2)}`;
}

function initInputFormatting() {
  const cardNumber = document.getElementById("cardNumber");
  const expiry = document.getElementById("expiry");
  const cvv = document.getElementById("cvv");

  cardNumber.addEventListener("input", () => {
    cardNumber.value = formatCardNumber(cardNumber.value);
  });

  expiry.addEventListener("input", () => {
    expiry.value = formatExpiry(expiry.value);
  });

  cvv.addEventListener("input", () => {
    cvv.value = cvv.value.replace(/\D/g, "").slice(0, 4);
  });
}

function initStepButtons() {
  document.getElementById("nextBtn").addEventListener("click", () => {
    if (!validateStep1()) return;
    showStep(2);
  });

  document.getElementById("backBtn").addEventListener("click", () => {
    document.getElementById("statusMsg").textContent = "";
    showStep(1);
  });

  document.getElementById("buyBtn").addEventListener("click", buyTicket);
}

function initEmailActions() {
  const autofillBtn = document.getElementById("autofillEmailBtn");
  autofillBtn.addEventListener("click", autofillEmail);
}

function init() {
  applySavedTheme();
  initProfileMenu();
  loadAccountEmail();
  loadData();
  initInputFormatting();
  initStepButtons();
  initEmailActions();
}

window.addEventListener("DOMContentLoaded", init);