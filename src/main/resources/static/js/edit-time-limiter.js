//開始時刻が終了時刻より遅い場合はエラーメッセージの表示とデータ変更の抑制
//どちらのみ入力された場合は、controllerで抑制
document.addEventListener("DOMContentLoaded",  ()=> {
    const startTimeInput = document.getElementById("start_time");
    const endTimeInput = document.getElementById("end_time");

    startTimeInput.addEventListener("input",  ()=> {
        endTimeInput.min = startTimeInput.value;
    });
	endTimeInput.addEventListener("input",  ()=> {
	    startTimeInput.max = endTimeInput.value;
	});
});

