package com.childcare.point.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.PointConfigDsplDataDto;
import com.childcare.point.dto.PointConfigDto;
import com.childcare.point.dto.UpdateConfigOkDetailDto;
import com.childcare.point.entity.PointMaster;
import com.childcare.point.entity.PointNameMaster;
import com.childcare.point.repository.PointMasterRepository;
import com.childcare.point.repository.PointNameMasterRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

@Service
public class PointConfigServiceImpl {

	@Autowired
	private PointMasterRepository pointMasterRepository;

	@Autowired
	private PointNameMasterRepository pointNameMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * ポイント設定画面表示データ取得
	 * 
	 * @param userName
	 * @return
	 */
	public PointConfigDto selectPointConfigData(String userName) {
		List<PointConfigDsplDataDto> pointConfigDsplDataDtoList = pointMasterRepository.find();

		PointConfigDto pointConfigDto = new PointConfigDto();
		pointConfigDto.setUserName(userName);
		pointConfigDto.setPointConfigDsplDataDtoList(pointConfigDsplDataDtoList);

		return pointConfigDto;
	}

	/**
	 * ポイント設定画面表示データ登録更新
	 * 
	 * isInsertable=trueの場合、2TBLに登録
	 * isInsertable=falseの場合、差分をPOINT_MASTER TBLに更新
	 * 
	 * @param userName
	 * @param pointConfigData
	 * @return
	 */
	@Transactional
	public String upsertPointConfigData(String userName,
			List<UpdateConfigOkDetailDto> initDataList,
			List<UpdateConfigOkDetailDto> upsertDataList) {

		String message = "";

		if (upsertDataList != null) {

			//登録処理
			//isInsertable=trueのレコードが対象データ
			//画面側で行追加した行のみが条件を満たす
			//POINT_MASTER TBL登録
			if (upsertDataList.size() > initDataList.size()) {
				//			redisService.getData("config-data:" + userName).size()) {

				System.out.println("000");

				for (int i = 0; i < upsertDataList.size(); i++) {
					if (Boolean.parseBoolean(upsertDataList.get(i).getIsInsertable())) {

						//エラーチェック
						if (upsertDataList.get(i).getPointMasterId().isEmpty()) {
							System.err.println("pointMasterId is empty");
							message = "ポイントIDが入力されていません。";
							return message;
						}
						if (upsertDataList.get(i).getPointName().isEmpty()) {
							System.err.println("pointName is empty");
							message = "ポイント名が入力されていません。";
							return message;
						}
						if (upsertDataList.get(i).getPoint() == 0) {
							System.err.println("point is 0");
							message = "ポイントが入力されていません。";
							return message;
						}

						//登録処理実行
						try {
							//POINT_MASTER TBL登録
							PointMaster pointMaster = new PointMaster();
							pointMaster.setPointMasterId(upsertDataList.get(i).getPointMasterId());
							pointMaster.setPoint(upsertDataList.get(i).getPoint());
							pointMaster.setUseMethod(upsertDataList.get(i).getUseMethod());
							pointMaster.setActiveFlg(upsertDataList.get(i).getActiveFlg());
							pointMaster.setUpdateUserId(upsertDataList.get(i).getUserName());

							pointMasterRepository.save(pointMaster);

							//POINT_NAME_MASTER TBL登録
							PointNameMaster pointNameMaster = new PointNameMaster();
							pointNameMaster.setPointNameMasterId(upsertDataList.get(i).getPointMasterId());
							pointNameMaster.setPointName(upsertDataList.get(i).getPointName());

							pointNameMasterRepository.save(pointNameMaster);
						} catch (Exception e) {
							System.err.println(e.getMessage());
							message = "	登録できませんでした。";
							return message;
						}
					}
				}
			}

			//更新処理
			//redisTemplateからpointConfigDataを取得
			//取得データとリクエストデータの差分を確認
			//差分があった場合、対象データを更新
			List<UpdateConfigOkDetailDto> retrievedUpdateDataList = retreivingUpdateData(userName, initDataList,
					upsertDataList);

			if (retrievedUpdateDataList.size() > 0) {
				System.out.println("r");

				//更新処理を実行
				//retrievedUpdateDataListは、要素ごとに更新対象のデータのみnull以外の値が格納されているため、null以外のフィールドをset句に追加
				//1つでもset句にフィールドが設定された場合、更新対象の変数はtrueとなり更新処理を実行する
				for (int i = 0; i < retrievedUpdateDataList.size(); i++) {
					boolean isUpdatable = false;
					boolean isUpdatableName = false;
					int execCount = 0;

					//POINT_MASTER SQL準備
					CriteriaBuilder cbPointMaster = entityManager.getCriteriaBuilder();
					CriteriaUpdate<PointMaster> updatePointMaster = cbPointMaster
							.createCriteriaUpdate(PointMaster.class);
					Root<PointMaster> rootPointMaster = updatePointMaster.from(PointMaster.class);

					if (retrievedUpdateDataList.get(i).getPoint() != 0) {
						updatePointMaster.set(rootPointMaster.get("point"), retrievedUpdateDataList.get(i).getPoint());
						isUpdatable = true;
					}
					if (retrievedUpdateDataList.get(i).getUseMethod() != null) {
						updatePointMaster.set(rootPointMaster.get("useMethod"),
								retrievedUpdateDataList.get(i).getUseMethod());
						isUpdatable = true;
					}
					if (retrievedUpdateDataList.get(i).getActiveFlg() != null) {
						updatePointMaster.set(rootPointMaster.get("activeFlg"),
								retrievedUpdateDataList.get(i).getActiveFlg());
						isUpdatable = true;
					}

					//POINT_NAME_MASTER SQL準備
					CriteriaBuilder cbPointNameMaster = entityManager.getCriteriaBuilder();
					CriteriaUpdate<PointNameMaster> updatePointNameMaster = cbPointNameMaster
							.createCriteriaUpdate(PointNameMaster.class);
					Root<PointNameMaster> rootPointNameMaster = updatePointNameMaster.from(PointNameMaster.class);

					if (retrievedUpdateDataList.get(i).getPointName() != null) {
						updatePointNameMaster.set(rootPointNameMaster.get("pointName"),
								retrievedUpdateDataList.get(i).getPointName());
						isUpdatableName = true;
					}

					//更新処理実行
					if (isUpdatable) {

						//POINT_MASTER
						updatePointMaster.where(cbPointMaster.equal(rootPointMaster.get("pointMasterId"),
								retrievedUpdateDataList.get(i).getPointMasterId()));
						try {
							execCount = entityManager.createQuery(updatePointMaster).executeUpdate();
							System.out.println(execCount);

						} catch (Exception e) {
							System.err.println(e.getMessage());
							message = "更新できませんでした。";
							return message;
						}

					}
					if (isUpdatableName) {

						//POINT_NAME_MASTER
						updatePointNameMaster
								.where(cbPointNameMaster.equal(rootPointNameMaster.get("pointNameMasterId"),
										retrievedUpdateDataList.get(i).getPointMasterId()));
						try {
							execCount = entityManager.createQuery(updatePointNameMaster).executeUpdate();
							System.out.println(execCount);

						} catch (Exception e) {
							System.err.println(e.getMessage());
							message = "更新できませんでした。";
							return message;
						}
					}
				}
			} else if (retrievedUpdateDataList.size() == 0) {
				message = "更新件数は0件です。";
				return message;
			}
		}

		message = "更新しました。";
		return message;
	}

	/**
	 * 更新対象データ抽出処理
	 * 
	 * セッションに保持された初期表示データと渡された更新対象データを要素ごとに比較
	 * 差分が発生した要素のフィールドを新規更新対象リストに格納
	 * 
	 * @param userName
	 * @param upsertDataList
	 * @return retrievedUpdateDataList
	 */
	public List<UpdateConfigOkDetailDto> retreivingUpdateData(String userName,
			List<UpdateConfigOkDetailDto> initDataList,
			List<UpdateConfigOkDetailDto> upsertDataList) {

		System.out.println(initDataList.size());
		System.out.println(upsertDataList.size());

		List<UpdateConfigOkDetailDto> retrievedUpdateDataList = new ArrayList<>();

		for (int i = 0; i < initDataList.size(); i++) {
			for (int j = 0; j < upsertDataList.size(); j++) {

				if (!Boolean.parseBoolean(upsertDataList.get(i).getIsInsertable())) {
					UpdateConfigOkDetailDto updateConfigOkDetailDto = new UpdateConfigOkDetailDto();

					//更新対象データチェック
					//条件分岐によるチェック項目は、更新SQLのset句で使用する
					if (initDataList.get(i).getPointMasterId()
							.equals(upsertDataList.get(j).getPointMasterId())) {

						if (!initDataList.get(i).getPointName()
								.equals(upsertDataList.get(j).getPointName())) {
							updateConfigOkDetailDto.setPointName(
									upsertDataList.get(j).getPointName());
						} else if ((!initDataList.get(i).getPointName()
								.equals(upsertDataList.get(j).getPointName()))
								&& upsertDataList.get(j).getPointName().isEmpty()) {
							return new ArrayList<>();
						}
						if (initDataList.get(i).getPoint() != upsertDataList.get(j).getPoint()
								&& (upsertDataList.get(j).getPoint() > 0)) {
							updateConfigOkDetailDto.setPoint(upsertDataList.get(j).getPoint());
						} else if (initDataList.get(i).getPoint() != upsertDataList.get(j).getPoint()
								&& upsertDataList.get(j).getPoint() <= 0) {
							return new ArrayList<>();
						}
						if (!(initDataList.get(i).getUseMethod()
								.equals(upsertDataList.get(j).getUseMethod()))) {
							updateConfigOkDetailDto.setUseMethod(upsertDataList.get(j).getUseMethod());
						}
						if (!(initDataList.get(i).getActiveFlg()
								.equals(upsertDataList.get(j).getActiveFlg()))) {
							updateConfigOkDetailDto.setActiveFlg(upsertDataList.get(j).getActiveFlg());
						}

						// 更新SQLのwhere句で使用するpointMasterId
						updateConfigOkDetailDto.setPointMasterId(upsertDataList.get(j).getPointMasterId());

						retrievedUpdateDataList.add(updateConfigOkDetailDto);
					}
				}
			}
		}

		return retrievedUpdateDataList;
	}
}
