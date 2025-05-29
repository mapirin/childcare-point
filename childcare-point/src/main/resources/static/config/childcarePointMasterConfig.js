/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const masterUpdateOkButton = document.getElementById("masterUpdateOkButton");
	//	const userName = document.querySelector("input[name='userName']").value;
	const message = document.querySelector('.message');

	const addButton = document.getElementById("addButton");

	addButton.addEventListener("click", function() {
		const tableBody = document.getElementById("addTable");
		const rowCount = tableBody.rows.length; // 現在の行数を取得

		// 新しい行を作成
		const newRow = tableBody.insertRow();
		newRow.innerHTML = `
	        <td><input type="text" name="pointConfigDsplDataDtoList[${rowCount}].pointMasterId"></td>
	        <td><input type="text" name="pointConfigDsplDataDtoList[${rowCount}].pointName"></td>
	        <td>
	            <select name="pointConfigDsplDataDtoList[${rowCount}].useMethod">
	                <option value="1">ためる</option>
	                <option value="2">つかう</option>
	            </select>
	        </td>
	        <td><input type="number" name="pointConfigDsplDataDtoList[${rowCount}].point"></td>
	        <td>
	            <select name="pointConfigDsplDataDtoList[${rowCount}].activeFlg">
	                <option value="1">表示</option>
	                <option value="0">非表示</option>
	            </select>
	        </td>
	        <span style="display: none;" data-value="1"></span>
	    `;
	});

	// masterUpdateOkButtonクリックのイベントリスナー
	masterUpdateOkButton.addEventListener("click", function(event) {
		event.preventDefault();

		let rows = document.querySelectorAll("#addTable tr");
		let upsertDataList = [];

		rows.forEach(row => {
			let pointMasterId = row.querySelector("td:nth-child(1)").innerText;
			let pointName = row.querySelector("td:nth-child(2) input").value;
			let useMethod = row.querySelector("td:nth-child(3) select").value;
			let point = row.querySelector("td:nth-child(4) input").value;
			let activeFlg = row.querySelector("td:nth-child(5) select").value;

			upsertDataList.push({
				pointMasterId: pointMasterId,
				pointName: pointName,
				useMethod: useMethod,
				point: point,
				activeFlgactiveFlg: activeFlg
			})
		});

		fetch("/api/config/update", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				upsertDataList: upsertDataList
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
				//TODO 取得データをhtmlにバインド
				point = data;
				message.innerText = "使いました。";
			})
	});
});