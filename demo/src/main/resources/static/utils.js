export function toggleTheme() {
  const current = document.documentElement.getAttribute("data-theme");

  // switch tra light e dark mode
  document.documentElement.setAttribute(
    "data-theme",
    current === "light" ? "dark" : "light"
  );
}