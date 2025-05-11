/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const masterUpdateOkButton = document.getElementById("masterUpdateOkButton");
	const userName = document.querySelector("input[name='userName']").value;
	const message = document.querySelector('.message');

	// masterUpdateOkButtonクリックのイベントリスナー
	masterUpdateOkButton.addEventListener("click", function(event) {
//		event.preventDefault();
//		
//		const selectedRadio = document.querySelector('input[name="selectedRadioData"]:checked');
//
//		if (!selectedRadio) {
//			message.innerText = "1つ選択してください。";
//			return;
//		}
//		
//		fetch("/api/update", {
//			method: "POST",
//			headers: {
//				"Content-Type": "application/json"
//			},
//			body: JSON.stringify({
//				userName: userName,
//				currentPoint: point,
//				pointData: [
//					{
//						pointId: pointId,
//						point: -addPoint
//					}
//				]
//			})
//		})
//			.then(response => {
//				if (!response.ok) {
//					message.innerText = "エラー。";
//					return;
//				}
//				return response.json();
//			})
//			.then(data => {
//				point = data;
//				message.innerText = "使いました。";
//			})
	});
});