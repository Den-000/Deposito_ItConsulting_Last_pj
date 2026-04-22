import { toggleTheme, applySavedTheme } from "./utils.js"

window.toggleTheme = toggleTheme

function initProfileMenu() {
    window.toggleMenu = function () {
        const dropdownMenu = document.getElementById("dropdownMenu")
        if (dropdownMenu) {
            dropdownMenu.classList.toggle("show")
        }
    }

    window.logout = function () {
        localStorage.removeItem("token")
        window.location.href = "/login.html"
    }

    window.addEventListener("click", function (e) {
        const profileMenu = document.querySelector(".profile-menu")
        const dropdownMenu = document.getElementById("dropdownMenu")

        if (profileMenu && dropdownMenu && !profileMenu.contains(e.target)) {
            dropdownMenu.classList.remove("show")
        }
    })
}

function initPasswordToggle() {
    const toggleButton = document.getElementById("togglePassword")
    const passwordInput = document.getElementById("p_password")

    if (!toggleButton || !passwordInput) {
        return
    }

    toggleButton.addEventListener("click", function () {
        if (passwordInput.type === "password") {
            passwordInput.type = "text"
        } else {
            passwordInput.type = "password"
        }
    })
}

async function loadUserProfile() {
    const token = localStorage.getItem("token")

    if (!token) {
        window.location.href = "/login.html"
        return
    }

    try {
        const response = await fetch("/users/me", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        })

        if (!response.ok) {
            throw new Error("Errore nel caricamento profilo")
        }

        const user = await response.json()

        const usernameTopbar = document.querySelector(".profile-name")
        const username = document.getElementById("p_username")
        const name = document.getElementById("p_name")
        const surname = document.getElementById("p_surname")
        const email = document.getElementById("p_email")
        const phone = document.getElementById("p_phone")
        const password = document.getElementById("p_password")

        if (usernameTopbar) {
            usernameTopbar.textContent = user.username ?? ""
            usernameTopbar.classList.remove("hidden-until-loaded")
        }
        if (username) username.textContent = user.username ?? ""
        if (name) name.textContent = user.name ?? ""
        if (surname) surname.textContent = user.surname ?? ""
        if (email) email.textContent = user.email ?? ""
        if (phone) phone.textContent = user.phone ?? ""
        if (password) password.value = user.password ?? "********"
    } catch (error) {
        console.error(error)
        alert("Impossibile caricare il profilo utente")
    }
}

document.addEventListener("DOMContentLoaded", function () {
    applySavedTheme()
    initProfileMenu()
    initPasswordToggle()
    loadUserProfile()
})