package com.childcare.point.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.childcare.point.dto.PointConfigDsplDataDto;
import com.childcare.point.dto.PointConfigDto;
import com.childcare.point.dto.UpdateConfigOkDetailDto;
import com.childcare.point.entity.PointMaster;
import com.childcare.point.entity.PointNameMaster;
import com.childcare.point.repository.PointMasterRepository;
import com.childcare.point.repository.PointNameMasterRepository;
import com.childcare.point.service.redis.RedisService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class PointConfigServiceImpl {

	@Autowired
	private PointMasterRepository pointMasterRepository;

	@Autowired
	private PointNameMasterRepository pointNameMasterRepository;

	@Autowired
	private RedisService redisService;

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

		//redisを使用してインメモリに取得データ保持
		redisService.saveSessionData("config-data:" + userName, pointConfigDsplDataDtoList);

		return pointConfigDto;
	}

	/**
	 * ポイント設定画面表示データ登録更新
	 * 
	 * insertFlg="1"の場合、2TBLに登録
	 * insertFlg="0"の場合、差分を対象TBLに更新
	 * 
	 * @param pointConfigData
	 * @return
	 */
	public boolean upsertPointConfigData(String userName, List<UpdateConfigOkDetailDto> insertPointConfigData,
			List<UpdateConfigOkDetailDto> updatePointConfigData) {

		//登録処理
		if (insertPointConfigData != null) {
			for (int i = 0; i < insertPointConfigData.size(); i++) {
				//INSERT機能のみ
				//insertFlg="1"のレコードが対象データ
				//pointMasterIdでTBL検索して、レコードが取得できなければ登録
				//レコードが取得できた場合、「登録できません」のメッセージを表示
				//POINT_MASTER TBL登録
				PointMaster pointMaster = new PointMaster();
				pointMaster.setPointMasterId(insertPointConfigData.get(i).getPointMasterId());
				pointMaster.setPoint(insertPointConfigData.get(i).getPoint());
				pointMaster.setUseMethod(insertPointConfigData.get(i).getUseMethod());
				pointMaster.setActiveFlg(insertPointConfigData.get(i).getActiveFlg());
				pointMaster.setUpdateUserId(insertPointConfigData.get(i).getUserName());

				pointMasterRepository.save(pointMaster);

				//POINT_NAME_MASTER TBL登録
				PointNameMaster pointNameMaster = new PointNameMaster();
				pointNameMaster.setPointNameMasterId(insertPointConfigData.get(i).getPointMasterId());
				pointNameMaster.setPointName(insertPointConfigData.get(i).getPointName());

				pointNameMasterRepository.save(pointNameMaster);
			}
		}

		//更新処理
		//redisTemplateからpointConfigDataを取得
		//取得データとリクエストデータの差分を確認
		//差分があった場合、対象データを更新
		if (updatePointConfigData != null) {

			//更新データ抽出処理
			List<UpdateConfigOkDetailDto> retrievedUpdateData = retreivingUpdateData(userName, updatePointConfigData);
			if (retrievedUpdateData.size() > 0) {
				//更新処理を実行
				for (int i = 0; i < retrievedUpdateData.size(); i++) {
					CriteriaBuilder cb = entityManager.getCriteriaBuilder();
					CriteriaUpdate<PointMaster> update = cb.createCriteriaUpdate(PointMaster.class);
					Root<PointMaster> rootEntity = update.from(PointMaster.class);
					List<Predicate> predicate = new ArrayList<>();

					if (retrievedUpdateData.get(i).getPointName() != null) {
						update.set(rootEntity.get("pointName"), retrievedUpdateData.get(i).getPointName());
					}
					if (retrievedUpdateData.get(i).getPoint() != 0) {
						update.set(rootEntity.get("point"), retrievedUpdateData.get(i).getPoint());
					}
					if (retrievedUpdateData.get(i).getUseMethod() != null) {
						update.set(rootEntity.get("useMethod"), retrievedUpdateData.get(i).getUseMethod());
					}
					if (retrievedUpdateData.get(i).getActiveFlg() != null) {
						update.set(rootEntity.get("activeFlg"), retrievedUpdateData.get(i).getActiveFlg());
					}

					//where
					Predicate condition = cb.equal(rootEntity.get("pointMasterId"),
							retrievedUpdateData.get(i).getPointMasterId());
					update.where(condition);

					//exec
					entityManager.createQuery(update).executeUpdate();
				}
			}

		}

		return true;
	}

	//	public List<Map<String, String>> retreivingUpdateData(String userName,
	public List<UpdateConfigOkDetailDto> retreivingUpdateData(String userName,
			List<UpdateConfigOkDetailDto> updatePointConfigData) {

		//直近画面表示データを取得
		List<UpdateConfigOkDetailDto> initPointConfigData = (List<UpdateConfigOkDetailDto>) redisService
				.getData("config-data:" + userName);

		//		Map<String, String> retrievedUpdateData = new HashMap<String, String>();
		//		List<Map<String, String>> retrievedUpdateDataList = new ArrayList<>();
		List<UpdateConfigOkDetailDto> retrievedUpdateData = new ArrayList<>();

		for (int i = 0; i < initPointConfigData.size(); i++) {
			for (int j = 0; j < updatePointConfigData.size(); j++) {
				UpdateConfigOkDetailDto updateConfigOkDetailDto = new UpdateConfigOkDetailDto();

				//更新データのみチェック
				//ポイントIDはキー項目とし、画面からの改廃は不可とする
				//どうしても改廃が必要な場合は、TBL直接操作して、updateUserId="ADMIN"で更新
				if (initPointConfigData.get(i).getPointMasterId()
						.equals(updatePointConfigData.get(j).getPointMasterId())) {

					if (!initPointConfigData.get(i).getPointName()
							.equals(updatePointConfigData.get(j).getPointName())) {
						updateConfigOkDetailDto.setPointName(
								updatePointConfigData.get(j).getPointName());
					}
					if (initPointConfigData.get(i).getPoint() != updatePointConfigData.get(j).getPoint()) {
						updateConfigOkDetailDto.setPoint(updatePointConfigData.get(j).getPoint());
					}
					if (!initPointConfigData.get(i).getUseMethod()
							.equals(updatePointConfigData.get(j).getUseMethod())) {
						updateConfigOkDetailDto.setUseMethod(updatePointConfigData.get(j).getUseMethod());
					}
					if (!initPointConfigData.get(i).getActiveFlg()
							.equals(updatePointConfigData.get(j).getActiveFlg())) {
						updateConfigOkDetailDto.setActiveFlg(updatePointConfigData.get(j).getActiveFlg());
					}

					retrievedUpdateData.add(updateConfigOkDetailDto);
				}
			}
		}

		return retrievedUpdateData;
	}
}
