document.addEventListener("DOMContentLoaded", () => {
	const startTimeInput = document.getElementById("start_time");
	const endTimeInput = document.getElementById("end_time");
	const dateInput = document.getElementById("date");

	// 現在の時間を "hh:mm" 形式で取得
	const getCurrentTime = () => {
		const now = new Date();
		return now.toTimeString().slice(0, 5); // "hh:mm"
	};

	// 今日の日付を "YYYY-MM-DD" 形式で取得
	const getTodayDate = () => {
		const today = new Date();
		return today.toISOString().split('T')[0]; // "YYYY-MM-DD"
	};

	dateInput.max = getTodayDate(); // 最大日付を今日に設定

	// 時間入力の最大値を更新
	const updateDateTimeRestrictions = () => {
		if (dateInput.value === getTodayDate()) {
			// 今日の日付が選択された場合、最大時間を現在の時間に設定
			const currentTime = getCurrentTime();
			startTimeInput.max = currentTime;
			endTimeInput.max = currentTime;
		} else {
			// 今日以外の日付が選択された場合、最大時間の制限を解除
			startTimeInput.removeAttribute("max");
			endTimeInput.removeAttribute("max");
		}
	};

	// 時間入力のバリデーション
	const updateTimeRestrictions = () => {
		const startTime = startTimeInput.value;
		const endTime = endTimeInput.value;

		// まず日付の時間制限を更新
		updateDateTimeRestrictions();

		// 終了時間が開始時間より後であることを確認
		if (startTime && endTime && endTime < startTime) {
			endTimeInput.setCustomValidity("終了時間は開始時間より後に設定してください！");
		} else {
			endTimeInput.setCustomValidity("");
		}

		// 今日の日付が選択されている場合、未来の時間を防ぐ
		if (dateInput.value === getTodayDate()) {
			const currentTime = getCurrentTime();
			if (startTime && startTime > currentTime) {
				startTimeInput.setCustomValidity("未来の時間を選択することはできません！");
			} else {
				startTimeInput.setCustomValidity("");
			}

			if (endTime && endTime > currentTime) {
				endTimeInput.setCustomValidity("未来の時間を選択することはできません！");
			} else {
				endTimeInput.setCustomValidity("");
			}
		}
	};

	// 入力イベントを監視
	startTimeInput.addEventListener("input", updateTimeRestrictions);
	endTimeInput.addEventListener("input", updateTimeRestrictions);
	dateInput.addEventListener("change", updateTimeRestrictions); // 日付変更時、時間を再チェック

	// ページ読み込み時、日付が既に選択されている場合は制限を更新
	if (dateInput.value) {
		updateDateTimeRestrictions();
	}
});