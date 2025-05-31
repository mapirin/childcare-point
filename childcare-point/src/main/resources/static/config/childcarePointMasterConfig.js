/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	// HTMLの読み込み完了後に2要素を取得
	const masterUpdateOkButton = document.getElementById("masterUpdateOkButton");
	const userName = document.querySelector("input[name='userName']").value;
	const message = document.querySelector('.message');

	const initDataList = document.querySelectorAll("#addTable tr");
	let apiDataList = [];
	const addButton = document.getElementById("addButton");

	addButton.addEventListener("click", function() {
		const tableBody = document.getElementById("addTable");
		const rowCount = tableBody.rows.length; // 現在の行数を取得

		// 新しい行を作成
		const newRow = tableBody.insertRow();
		//table内のth:attrのdata-addedにtrueをセット
		newRow.setAttribute("data-added", "true");

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
	    `;
	});

	// masterUpdateOkButtonクリックのイベントリスナー
	masterUpdateOkButton.addEventListener("click", function(event) {
		event.preventDefault();

		let rows = document.querySelectorAll("#addTable tr");
		let compareDataList = [];
		let upsertDataList = [];

		if (apiDataList.length === 0) {
			initDataList.forEach(item => {
				let pointMasterId = item.querySelector("td:nth-child(1)").innerText;
				let pointName = item.querySelector("td:nth-child(2) input").value;
				let useMethod = item.querySelector("td:nth-child(3) select").getAttribute("data-value");
				let point = item.querySelector("td:nth-child(4) input").value;
				let activeFlg = item.querySelector("td:nth-child(5) select").getAttribute("data-value");
				let isInsertable = '0'

				compareDataList.push({
					pointMasterId: pointMasterId,
					pointName: pointName,
					useMethod: useMethod,
					point: point,
					activeFlg: activeFlg,
					userName: userName,
					isInsertable: isInsertable
				});
			});
		} else if (apiDataList.length > 0) {
			compareDataList = apiDataList;
		}

		rows.forEach(row => {
			let pointMasterId = row.getAttribute("data-added") === "true" ? row.querySelector("td:nth-child(1) input").value : row.querySelector("td:nth-child(1)").innerText;
			let pointName = row.querySelector("td:nth-child(2) input").value;
			let useMethod = row.querySelector("td:nth-child(3) select").value;
			let point = row.querySelector("td:nth-child(4) input").value;
			let activeFlg = row.querySelector("td:nth-child(5) select").value;
			let isInsertable = row.getAttribute("data-added");

			upsertDataList.push({
				pointMasterId: pointMasterId,
				pointName: pointName,
				useMethod: useMethod,
				point: point,
				activeFlg: activeFlg,
				userName: userName,
				isInsertable: isInsertable
			})
		});

		console.log(compareDataList);
		console.log(upsertDataList);
		console.log("start")

		fetch("/api/config/update", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				userName: userName,
				initDataList: compareDataList,
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
				console.log(data.pointConfigDsplDataDtoList);

				//ここでリストを渡すことでレンダリング処理に使用するデータのみを処理対象とする
				renderTable(data.pointConfigDsplDataDtoList);
				message.innerText = data.message;

				console.log("end")
			})
	});

	function renderTable(data) {
		const tableBody = document.getElementById("addTable");
		tableBody.innerHTML = "";
		apiDataList = [];

		//第一引数：処理中の要素
		//第二引数：処理中のインデックス(0->n)
		//thymeleafのフォームに含めたい場合はname属性を使用
		//単純な埋め込みテキストとして使用する場合は$でそのままレンダリング
		data.forEach((item, index) => {
			const row =
				`
				<tr data-added="false">
					<td>${item.pointMasterId}</td>
			        <td><input type="text" name="pointConfigDsplDataDtoList[${index}].pointName" value="${item.pointName}"></td>
			        <td>
			            <select name="pointConfigDsplDataDtoList[${index}].useMethod" data-value="${item.useMethod}">
			                <option value="1">ためる</option>
			                <option value="2">つかう</option>
			            </select>
			        </td>
			        <td><input type="number" name="pointConfigDsplDataDtoList[${index}].point" value="${item.point}"></td>
			        <td>
			            <select name="pointConfigDsplDataDtoList[${index}].activeFlg" data-value="${item.activeFlg}">
			                <option value="1">表示</option>
			                <option value="0">非表示</option>
			            </select>
			        </td>
				</tr>
		    `;
			tableBody.innerHTML += row;

			apiDataList.push({
				pointMasterId: item.pointMasterId,
				pointName: item.pointName,
				useMethod: item.useMethod,
				point: item.point,
				activeFlg: item.activeFlg,
				userName: item.userName,
				isInsertable: '0'
			});
		})


	}
});