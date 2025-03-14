//編集画面のデータ処理。　HH:mmからHH:mm:ssに変更
const timeInputs = document.querySelectorAll('.editTime');

timeInputs.forEach(input => {
	//inputだけではデータがうまく修正されない可能性があるため、changeの時も修正する。
	input.addEventListener('input', function() {
		let value = input.value;

		if (value.length === 5) {
			input.value = value + ":00";
		}
	});

	input.addEventListener('change', function() {
		let value = input.value;
		if (value.length === 5) {
			input.value = value + ":00";
		}
	});
});