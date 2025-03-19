const timeInputs = document.querySelectorAll('.editTime');
const submitBtn = document.getElementById('submit_time');

//変更ボタンが押下時に時間を整形
submitBtn.addEventListener('click', ()=> {
	
	timeInputs.forEach(input => {
		let value = input.value;

		//HH:mmの場合は秒数を追加
		if (value.length === 5) {
			input.value = value + ":00"; 
		}
	console.log(input.value);
	});
});
