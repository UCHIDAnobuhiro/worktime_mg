document.addEventListener("DOMContentLoaded", () => {
    const startTimeInput = document.getElementById("start_time");
    const endTimeInput = document.getElementById("end_time");
    const dateInput = document.getElementById("date");

    // 現在の時間を取得し、"hh:mm" 形式にフォーマットする
    function getCurrentTime() {
        const now = new Date();
        const hours = now.getHours().toString().padStart(2, '0'); // 時間を取得し、フォーマットする
        const minutes = now.getMinutes().toString().padStart(2, '0'); // 分を取得し、フォーマットする
        return `${hours}:${minutes}`; // 現在の時間を "hh:mm" 形式で返す
    }

    // 今日の日付を取得し、"YYYY-MM-DD" 形式で返す
    function getTodayDate() {
        const today = new Date();
        const year = today.getFullYear();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 現在の月を取得し、ゼロ埋め
        const day = today.getDate().toString().padStart(2, '0'); // 現在の日付を取得し、ゼロ埋め
        return `${year}-${month}-${day}`;
    }
    
    const todayDate = getTodayDate();
    dateInput.max = todayDate;  // 最大日付を今日の日付に設定する

    // 日付が今日かどうかを確認し、もし今日なら時間制限を現在の時間に設定する
    function updateDateTimeRestrictions() {
        const selectedDate = new Date(dateInput.value); // 現在選択された日付を取得
        const currentDate = new Date();

        // 日付を "YYYY-MM-DD" 形式（時刻部分を除去）に設定する
        const formattedCurrentDate = currentDate.toISOString().split('T')[0]; // "YYYY-MM-DD" 形式で現在の日付を取得
        const formattedSelectedDate = selectedDate.toISOString().split('T')[0];

        // 選択された日付が今日の場合
        if (formattedSelectedDate === formattedCurrentDate) {
            // 現在の時間を取得
            const currentTime = getCurrentTime();
            // 開始時間と終了時間の最大値を現在の時間に設定する
            startTimeInput.max = currentTime;
            endTimeInput.max = currentTime;
        } else {
            // 今日ではない場合、最大時間制限を解除する
            startTimeInput.removeAttribute("max");
            endTimeInput.removeAttribute("max");
        }
    }

    // 開始時間と終了時間の最小/最大制限を設定する
    function updateTimeRestrictions() {
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;

        // 終了時間が開始時間よりも遅くないことを確認する
        if (startTime && endTime && endTime < startTime) {
            endTimeInput.setCustomValidity("開始打刻より遅い時間を入力してください。");
        } else {
            endTimeInput.setCustomValidity("");
        }

        // 時間制限もここで更新することができる
        updateDateTimeRestrictions();
    }

    // 開始時間と終了時間の入力を監視する
    startTimeInput.addEventListener("input", updateTimeRestrictions);
    endTimeInput.addEventListener("input", updateTimeRestrictions);

    // 初期化時に、もし日付が選択されていれば、時間制限を設定する
    const selectedDate = dateInput.value;
    if (selectedDate) {
        updateDateTimeRestrictions();
    }
});
