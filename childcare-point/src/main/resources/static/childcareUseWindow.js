/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const useOkButton = document.getElementById("useOkButton");
	const userName = document.querySelector('input[name="userName"]').value;
	let point = document.getElementById("point").value;
	const message = document.querySelector('.message');

	// useOkButtonクリックのイベントリスナー
	useOkButton.addEventListener("click", function(event) {
		event.preventDefault();

		const selectedRadio = document.querySelector('input[name="selectedRadioData"]:checked');

		if (!selectedRadio) {
			message.innerText = "1つ選択してください。";
			return;
		}

		const pointId = selectedRadio.getAttribute("data-point-id");
		const addPoint = parseInt(selectedRadio.getAttribute("data-point"), 10);
		const settingPoint = point - addPoint;

		console.log("addPoint:" + addPoint);

		if (settingPoint < 0) {
			message.innerText = "ポイントが足りません。";
			return;
		}

		console.log("選択された値: " + pointId);
		console.log("選択された値: " + addPoint);


		fetch("/api/update", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				userName: userName,
				currentPoint: point,
				pointData: [
					{
						pointId: pointId,
						point: -addPoint
					}
				]
			})
		})
			.then(response => {
				if (!response.ok) {
					message.innerText = "エラー。";
					return;
				}
				return response.json();
			})
			.then(data => {
				point = data;
				message.innerText = "使いました。";
			})
	});
});