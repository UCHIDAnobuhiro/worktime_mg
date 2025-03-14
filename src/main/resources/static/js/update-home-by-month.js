document.addEventListener("DOMContentLoaded", () => {
	const monthSelect = document.getElementById('monthSelect');
	
	const updateStampByMonth = () => {
		const selectedMonth = parseInt(monthSelect.value);
		const tableContainer = document.getElementById('tableContainer');
		//リセットテーブル
		tableContainer.innerHTML = '';

		//2025年に暫定
		const daysInMonth = new Date(2025, selectedMonth, 0).getDate();

		//テーブルのヘッダー
		const table = document.createElement('table');
		table.className = "table-auto w-full text-left whitespace-no-wrap";
		table.innerHTML = `
        <thead>
            <tr>
                <th class="px-4 py-3 title-font tracking-wider font-medium text-gray-900 text-sm bg-gray-100 rounded-tl rounded-bl">日付</th>
                <th class="px-4 py-3 title-font tracking-wider font-medium text-gray-900 text-sm bg-gray-100">出勤時刻</th>
                <th class="px-4 py-3 title-font tracking-wider font-medium text-gray-900 text-sm bg-gray-100">退勤時刻</th>
                <th class="px-4 py-3 title-font tracking-wider font-medium text-gray-900 text-sm bg-gray-100">勤務時間</th>
                <th class="w-10 title-font tracking-wider font-medium text-gray-900 text-sm bg-gray-100 rounded-tr rounded-br"></th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    `;
		//テーブルボディー
		const tbody = table.querySelector('tbody');

		// 月に対するstampを請求
		fetch(`/updateStampsByMonth/${selectedMonth}`)
			.then(response => response.json())
			.then(data => {

				// その月の日数に対するデカさの配列容器を作成
				let stampsByDay = new Array(daysInMonth).fill(null);

				// その月のstamp日付をチェックし、日付が一致するstampを配列に
				data.forEach(stamp => {
					const dayOfMonth = new Date(stamp.day).getDate() - 1;
					stampsByDay[dayOfMonth] = stamp;
				});

				// 配列の中のデータを整形し、テーブルボディに表示
				for (let i = 1; i <= daysInMonth; i++) {

					//ボディに挿入する行を定義
					const row = tbody.insertRow();
					const stamp = stampsByDay[i - 1];

					//各elementsの整形と保存
					const stampDate = stamp ? new Date(stamp.day) : null;
					const stampDayOfMonth = stampDate ? stampDate.getDate() : null;
					const stampMonth = stampDate ? stampDate.getMonth() : null;
					const stampYear = stampDate ? stampDate.getFullYear() : null;
					const stampStartTime = stamp?.start_time ?? '00:00:00';
					const stampEndTime = stamp?.end_time ?? '00:00:00';
					const stampHoursWorked = stamp?.hoursWorked ?? '00:00:00';

					// 年と月が一致してるかをチェック
					const isCurrentMonth = stampMonth === (selectedMonth - 1);
					const isCurrentDay = stampDayOfMonth === i;

					// 曜日の設定
					const currentDay =new Date(2025,selectedMonth - 1,i);
					const currentWeekday = currentDay? currentDay.toLocaleDateString('ja-JP', { weekday: 'short' }) : '';
					
					//挿入された行の内容、データがない場合は赤でハイライト
					row.innerHTML = `
					<td class="px-4 py-3">${`2025年${selectedMonth}月${i}日(${currentWeekday})`}</td>
					<td class="px-4 py-3 ${!(isCurrentMonth && isCurrentDay) ? 'text-red-500' : ''}">
						${isCurrentMonth && isCurrentDay ? stampStartTime : '00:00:00'}
					</td>
					<td class="px-4 py-3 ${!(isCurrentMonth && isCurrentDay) ? 'text-red-500' : ''}">
						${isCurrentMonth && isCurrentDay ? stampEndTime : '00:00:00'}
					</td>
					<td class="px-4 py-3 ${!(isCurrentMonth && isCurrentDay) ? 'text-red-500' : ''}">
						${isCurrentMonth && isCurrentDay ? stampHoursWorked : '00:00:00'}
					</td>
					`;
				}
			})
			.catch(error => {
				console.error('stamp取得失敗', error);
			});
		tableContainer.appendChild(table);
	}
	    monthSelect.addEventListener("change", updateStampByMonth);
		monthSelect.addEventListener("change", updateWorkTimeByMonth);
		updateStampByMonth();
		updateWorkTimeByMonth();
	});

	//月のselectorを選択するときの総勤務時間と出勤日数の更新
	async function updateWorkTimeByMonth() {
		const selectedMonth = parseInt(document.getElementById("monthSelect").value, 10);
		try {
			const response = await fetch(`/updateWorkTimeByMonth/${selectedMonth}`, {
				method: "PATCH",
				headers: {
					"Content-Type": "application/json"
				},
			});

			if (response.ok) {
				const data = await response.json();
				document.getElementById("workTimeMonth").textContent = `総勤務時間: ${data.workTimeMonth}`;
				document.getElementById("workDaysMonth").textContent = `出勤日数: ${data.workDaysMonth}日`;
			} else {
				console.error("ステータス更新失敗", response.status);
			}
		} catch (error) {
			console.error("ステータス更新エラー:", error);
		}
	}
