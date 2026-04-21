export function initProfileMenu() {
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