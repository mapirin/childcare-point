/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const useOkForm = document.getElementById("useOkForm");
	const useOkButton = document.getElementById("useOkButton");
	const message = document.querySelector('.message');

	// useOkButtonクリックのイベントリスナー
	useOkButton.addEventListener("click", function(event) {
		const selectedRadio = document.querySelector('input[name="selectedRadioData"]:checked');
		
		if(!selectedRadio){
			event.preventDefault();
			message.innerText = "1つ選択してください。";
			return;
		}
		
		console.log("選択された値: " + selectedRadio.value);
		useOkForm.submit();
	});
});