document.addEventListener("DOMContentLoaded", () => {
	const form = document.querySelector("form");
	const password = document.getElementById("passwd");
	const confirmPassword = document.getElementById("confirm-passwd");
	
	form.addEventListener("submit", (event) => {
		if (password.value !== confirmPassword.value) {
			event.preventDefault(); // フォームの送信を防ぐ
			alert("パスワードが一致しません。もう一度確認してください。");
			confirmPassword.focus();
		}
	});
});