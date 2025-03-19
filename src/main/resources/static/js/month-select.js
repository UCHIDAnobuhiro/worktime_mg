document.addEventListener("DOMContentLoaded", function() {
    const monthSelect = document.getElementById("monthSelect");
    monthSelect.addEventListener("change", function() {
        document.getElementById("monthForm").submit();
    });
});