/**
 * 当該処理のform送信先エンドポイントが「/list/enter」のため、
 * 当該jsファイルは「/list」配下のディレクトリに配置
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const updateDateForm = document.getElementById("updateDateForm");
	const updateDateInput = document.getElementById("updateDateInput");

	if (!updateDateForm || !updateDateInput) {
		console.error("updateDateForm または updateDateInput が見つかりません！");
		return;
	}

	console.log("updateDateForm と updateDateInput が検出されました。");

	// Enterキーのイベントリスナー
	// バリデーションありsubmitを実行する
	updateDateInput.addEventListener("keydown", function(event) {
		if (event.key === "Enter") {
			event.preventDefault();
			updateDateForm.requestSubmit();
		}
	});

	// フォーム送信時のイベントリスナーを追加
	// 発火されたバリデーションありsubmitを検知し入力日付のチェック処理を実行
	updateDateForm.addEventListener("submit", function(event) {
		event.preventDefault();
		validateAndSubmit(updateDateInput);
	});

	/**
	 * 入力日付のチェックおよび/list/enterエンドポイントへのform送信
	 * updateDateInputが当該関数のスコープ外でundefinedとなるのを防ぐため、引数に指定
	 */
	function validateAndSubmit(updateDateInput) {
		let inputDate = updateDateInput.value.trim();
		console.log("入力された日付: " + inputDate);

		// 入力日付の長さが10未満の場合
		if (inputDate.length < 10) {
			console.error("無効な日付なので処理を中断: " + inputDate);
			return;
		}

		// 入力日付の形式が「YYYY/MM/DD」でない場合
		if(!/^\d{4}\/\d{2}\/\d{2}$/.test(inputDate)){
			console.error("無効な日付なので処理を中断: " + inputDate);
			return;
		}
			
		// 入力日付の日にちが"00"または無効な日付の場合
		let parsedDate = dayjs(inputDate, "YYYY/MM/DD", true);
		if (parseInt(inputDate.substring(8, 10), 10) === 0 || !parsedDate.isValid()) {
			console.error("無効な日付なので処理を中断: " + inputDate);
			return;
		}

		console.log("有効な日付として処理: " + parsedDate.format("YYYY/MM/DD"));

		// 入力日付が未来日の場合
		let today = dayjs();
		if (parsedDate.isAfter(today, "day")) {
			console.error("未来の日付なので処理を中断: " + parsedDate.format("YYYY/MM/DD"));
			return;
		}

		console.log("フォーム送信: " + inputDate);
		updateDateForm.submit();
	}
});