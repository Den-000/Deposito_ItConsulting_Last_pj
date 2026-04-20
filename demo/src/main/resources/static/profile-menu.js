document.addEventListener("DOMContentLoaded", function () {
    const profileBtn = document.getElementById("profileBtn");
    const dropdownMenu = document.getElementById("dropdownMenu");
    const logoutLink = document.getElementById("logoutLink");

    if (profileBtn && dropdownMenu) {
        profileBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle("show");
        });

        document.addEventListener("click", function (e) {
            if (!dropdownMenu.contains(e.target) && !profileBtn.contains(e.target)) {
                dropdownMenu.classList.remove("show");
            }
        });
    }

    if (logoutLink) {
        logoutLink.addEventListener("click", function (e) {
            e.preventDefault();
            localStorage.removeItem("token");
            window.location.href = "/login.html";
        });
    }
});