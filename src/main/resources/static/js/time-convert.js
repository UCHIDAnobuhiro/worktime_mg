const timeInputs = document.querySelectorAll('.editTime');
const submitBtn = document.getElementById('submit_time');

submitBtn.addEventListener('click', function(event) {
	// 监听提交按钮的点击事件，处理时间格式为HH:mm:ss
	timeInputs.forEach(input => {
		let value = input.value;

		// 如果输入的时间格式为HH:mm且没有秒数，自动补充秒数
		if (value.length === 5) {
			input.value = value + ":00";  // 将HH:mm转换为HH:mm:ss
		}
	console.log(input.value);
	});
});
