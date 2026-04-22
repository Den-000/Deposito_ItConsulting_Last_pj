export function initProfileMenu() {
    const profileName = document.querySelector(".profile-name");
    const token = localStorage.getItem("token");

    if (profileName) {
        if (token) {
            try {
                const payload = JSON.parse(atob(token.split(".")[1]));
                profileName.textContent = payload.username || payload.sub || "";
            } catch (e) {
                profileName.textContent = "";
            }
        } else {
            profileName.textContent = "";
        }

        profileName.classList.remove("hidden-until-loaded");
    }

    window.toggleMenu = function () {
        const dropdownMenu = document.getElementById("dropdownMenu");
        if (dropdownMenu) {
            dropdownMenu.classList.toggle("show");
        }
    };

    window.logout = function () {
        localStorage.removeItem("token");
        window.location.href = "/login.html";
    };

    window.addEventListener("click", function (e) {
        const profileMenu = document.querySelector(".profile-menu");
        const dropdownMenu = document.getElementById("dropdownMenu");

        if (profileMenu && dropdownMenu && !profileMenu.contains(e.target)) {
            dropdownMenu.classList.remove("show");
        }
    });
}