import { initProfileMenu } from "/profile-menu.js";
import { toggleTheme, applySavedTheme } from "/utils.js";

window.toggleTheme = toggleTheme;

applySavedTheme();
initProfileMenu();

const token = localStorage.getItem("token");

let passwordVisible = false;
let realPassword = "********";

async function loadProfile() {
    try {
        const res = await fetch("/users/me", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!res.ok) throw new Error("Errore profilo");

        const user = await res.json();

        document.getElementById("username").textContent = user.username;
        document.getElementById("p_username").textContent = user.username;
        document.getElementById("p_name").textContent = user.name ?? "Non disponibile";
        document.getElementById("p_surname").textContent = user.surname ?? "Non disponibile";
        document.getElementById("p_email").textContent = user.email;
        document.getElementById("p_phone").textContent = user.phone ?? "Non disponibile";

        realPassword = user.password;
        document.getElementById("p_password").textContent = "********";

    } catch (e) {
        console.error(e);
    }
}

document.getElementById("togglePassword").onclick = () => {
    passwordVisible = !passwordVisible;

    document.getElementById("p_password").textContent =
        passwordVisible ? realPassword : "********";
};

loadProfile();