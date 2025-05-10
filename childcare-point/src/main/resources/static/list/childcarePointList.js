/**
 * 当該処理のform送信先エンドポイントが「/list/enter」のため、
 * 当該jsファイルは「/list」配下のディレクトリに配置
 */
document.addEventListener("DOMContentLoaded", function() {
	// テキストボックスの要素
	const updateDateInput = document.getElementById("updateDateInput");

	// 各種ボタン要素
	const yesterdayButton = document.getElementById("yesterdayButton");
	const tomorrowButton = document.getElementById("tomorrowButton");
	const deleteButton = document.getElementById("deleteButton");

	// ＜ボタンのイベントリスナー
	yesterdayButton.addEventListener('click', function(event) {
		event.preventDefault();
		if (checkUpdateDate(updateDateInput)) {
			fetchListData("/api/list/yesterday", "GET");
		}
	});

	// Enterキーのイベントリスナー
	updateDateInput.addEventListener("keydown", function(event) {
		if (event.key === "Enter") {
			event.preventDefault();

			if (checkUpdateDate(updateDateInput)) {
				fetchListData("/api/list/enter", "GET");
			}
		}
	});

	// ＞ボタンのイベントリスナー 
	tomorrowButton.addEventListener('click', function(event) {
		event.preventDefault();
		if (checkUpdateDate(updateDateInput)) {
			fetchListData("/api/list/tomorrow", "GET");
		}
	});

	// 削除ボタンのイベントリスナー
	deleteButton.addEventListener('click', function(event) {
		event.preventDefault();
		fetchListData("/api/list/delete", "POST");
	})

	/**
	 * 入力日付のチェックおよび/list/enterエンドポイントへのform送信
	 * updateDateInputが当該関数のスコープ外でundefinedとなるのを防ぐため、引数に指定
	 */
	function checkUpdateDate(updateDateInput) {
		let inputDate = updateDateInput.value.trim();
		console.log("入力された日付: " + inputDate);

		// 入力日付の長さが10未満の場合
		if (inputDate.length < 10) {
			console.error("無効な日付なので処理を中断: " + inputDate);
			return false;
		}

		// 入力日付の形式が「YYYY/MM/DD」でない場合
		if (!/^\d{4}\/\d{2}\/\d{2}$/.test(inputDate)) {
			console.error("無効な日付なので処理を中断: " + inputDate);
			return false;
		}

		// 入力日付の日にちが"00"または無効な日付の場合
		let parsedDate = dayjs(inputDate, "YYYY/MM/DD", true);
		if (parseInt(inputDate.substring(8, 10), 10) === 0 || !parsedDate.isValid()) {
			console.error("無効な日付なので処理を中断: " + inputDate);
			return false;
		}

		console.log("有効な日付として処理: " + parsedDate.format("YYYY/MM/DD"));

		// 入力日付が未来日の場合
		let today = dayjs();
		if (parsedDate.isAfter(today, "day")) {
			console.error("未来の日付なので処理を中断: " + parsedDate.format("YYYY/MM/DD"));
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	function fetchListData(endpoint, httpMethod) {
		const userName = document.getElementById("userName").value;
		const currentPoint = document.getElementById("currentPoint").value;
		let updateDateInput = document.getElementById("updateDateInput");
		let doTomorrowMoveFlg = document.getElementById("doTomorrowMoveFlg");
		let doDeleteListFlg = document.getElementById("doDeleteListFlg");
		const pointListData = document.getElementById("pointListBody");
		const recordId = document.querySelector("input[name=selectedRecordId]:checked");

		let params = new URLSearchParams({
			userName: userName,
			currentPoint: currentPoint,
			updateDate: updateDateInput.value
		});

		if (httpMethod === "POST") {
			params.append("recordId", recordId.value);
		}


		fetch(`${endpoint}?${params.toString()}`, {
			method: httpMethod,
			headers: {
				"Content-Type": "application/json"
			}
		})
			.then(responce => {
				if (!responce.ok) {

				}
				return responce.json();
			})
			.then(data => {
				updateDateInput.value = data.updateDate;

				// `doTomorrowMoveFlg` の値に基づいてボタンを表示・非表示
				doTomorrowMoveFlg.value = data.doTomorrowMoveFlg ? "true" : "false";
				console.log("＞："+doTomorrowMoveFlg.value);
				if (tomorrowButton) {
					tomorrowButton.style.display = data.doTomorrowMoveFlg ? "block" : "none";
				}

				// `doDeleteListFlg` の値に基づいてボタンを表示・非表示
				doDeleteListFlg.value = data.doDeleteListFlg ? "true" : "false";
				console.log("削除："+doDeleteListFlg.value);
				if (doDeleteListFlg) {
					doDeleteListFlg.style.display = data.doDeleteListFlg ? "block" : "none";
				}

				pointListData.innerHTML = "";

				data.selectPointList.forEach(item => {
					const row = document.createElement("tr");
					row.innerHTML =
						`
							<td>
								<input type="radio" name="selectedRecordId" value="${item.recordId}">
							</td>
			                <td>${item.pointName}</td>
			                <td>${item.point}</td>
		            	`;
					pointListData.appendChild(row);
				})
			})
	}
});