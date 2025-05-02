/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const stockOkForm = document.getElementById("stockOkForm");
	const stockOkButton = document.getElementById("stockOkButton");
	const message = document.querySelector('.message');

	// useOkButtonクリックのイベントリスナー
	stockOkButton.addEventListener("click", function(event) {
		const selectedRadio = document.querySelector('input[name="selectedRadioData"]:checked');
		
		if(!selectedRadio){
			event.preventDefault();
			message.innerText = "1つ選択してください。";
			return;
		}
		
		console.log("選択された値: " + selectedRadio.value);
		stockOkForm.submit();
	});
});