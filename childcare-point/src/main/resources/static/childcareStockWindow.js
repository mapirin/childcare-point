/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const stockOkButton = document.getElementById("stockOkButton");
	const userName = document.querySelector("input[name='userName']").value;
	let point = document.getElementById("point").value;
	const message = document.querySelector('.message');

	// useOkButtonクリックのイベントリスナー
	stockOkButton.addEventListener("click", function(event) {
		event.preventDefault();
		
		const selectedCheckboxDataList = Array.from(document.querySelectorAll('input[name="selectedCheckboxData"]:checked'))
			.map(item => (
				{
					pointId: item.getAttribute("data-point-id"),
					point: parseInt(item.getAttribute("data-point"), 10)
				}
			));

		console.log(parseInt(point, 10));
		console.log(selectedCheckboxDataList);

		if (selectedCheckboxDataList.length === 0) {
			message.innerText = "1つ以上選択してください。";
			return;
		}

		console.log("選択された値: " + selectedCheckboxDataList.value);

		fetch("/api/update", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				userName: userName,
				currentPoint: point,
				pointData: selectedCheckboxDataList
			})
		})
			.then(response => {
				if (!response) {
					message.innerText = "エラー。";
					return;
				}
				return response.json();
			})
			.then(data => {
				point = data;
				document.querySelectorAll('input[name="selectedCheckboxData"]:checked').forEach(item => {
					item.checked = false;
				})
				message.innerText = "貯めました。";
			})
	});
});