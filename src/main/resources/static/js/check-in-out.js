document.addEventListener("DOMContentLoaded", function () {
    const clockInButton = document.getElementById("clockInBtn");
    const clockOutButton = document.getElementById("clockOutBtn");
    const isClockInInput = document.getElementById("isClockIn");

	const setClockIn=(value)=>{
		isClockInInput.value = value;
		console.log("isClockIn 设置为:", value); // 用于调试
	}
	
    // 监听按钮点击事件
    clockInButton.addEventListener("click", function () {
        setClockIn(true);
    });

    clockOutButton.addEventListener("click", function () {
        setClockIn(false);
    });
});
