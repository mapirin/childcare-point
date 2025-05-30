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
import com.childcare.point.service.redis.RedisService;

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
	@Transactional
	public boolean upsertPointConfigData(String userName,
			List<UpdateConfigOkDetailDto> upsertDataList) {

		//登録処理
		if (upsertDataList != null) {

			for (int i = 0; i < upsertDataList.size(); i++) {
				if (upsertDataList.get(i).isInsertFlg()) {
					//INSERT機能のみ
					//insertFlg="1"のレコードが対象データ
					//pointMasterIdでTBL検索して、レコードが取得できなければ登録
					//レコードが取得できた場合、「登録できません」のメッセージを表示
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
				}
			}
		}

		//更新処理
		//redisTemplateからpointConfigDataを取得
		//取得データとリクエストデータの差分を確認
		//差分があった場合、対象データを更新
		List<UpdateConfigOkDetailDto> retrievedUpdateDataList = retreivingUpdateData(userName, upsertDataList);

		if (retrievedUpdateDataList.size() > 0 || retrievedUpdateDataList != null) {
			
			System.out.println(retrievedUpdateDataList.size());

			//更新処理を実行
			for (int i = 0; i < retrievedUpdateDataList.size(); i++) {
				boolean flg=false;
				
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();
				CriteriaUpdate<PointMaster> update = cb.createCriteriaUpdate(PointMaster.class);
				Root<PointMaster> rootEntity = update.from(PointMaster.class);

				if (retrievedUpdateDataList.get(i).getPointName() != null) {
					update.set(rootEntity.get("pointName"), retrievedUpdateDataList.get(i).getPointName());
					flg=true;
//					update.set("pointName", retrievedUpdateDataList.get(i).getPointName());
				}
				if (retrievedUpdateDataList.get(i).getPoint() != 0) {
					update.set(rootEntity.get("point"), retrievedUpdateDataList.get(i).getPoint());
					System.out.println(retrievedUpdateDataList.get(i).getPoint());
					flg=true;
//					update.set("point", retrievedUpdateDataList.get(i).getPoint());
				}
				if (retrievedUpdateDataList.get(i).getUseMethod() != null) {
					update.set(rootEntity.get("useMethod"), retrievedUpdateDataList.get(i).getUseMethod());
					flg=true;
//					update.set("useMethod", retrievedUpdateDataList.get(i).getUseMethod());
				}
				if (retrievedUpdateDataList.get(i).getActiveFlg() != null) {
					update.set(rootEntity.get("activeFlg"), retrievedUpdateDataList.get(i).getActiveFlg());
					flg=true;
//					update.set("activeFlg", retrievedUpdateDataList.get(i).getActiveFlg());
				}

				//where
//				Predicate condition = cb.equal(rootEntity.get("pointMasterId"),
//						retrievedUpdateDataList.get(i).getPointMasterId());

				//exec
				try {
					if(flg) {
						update.where(cb.equal(rootEntity.get("pointMasterId"),
								retrievedUpdateDataList.get(i).getPointMasterId()));
						System.out.println(retrievedUpdateDataList.get(i).getPointMasterId());
						
						int execCount=entityManager.createQuery(update).executeUpdate();
						System.out.println(execCount);
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return true;
	}

	//	public List<Map<String, String>> retreivingUpdateData(String userName,
	public List<UpdateConfigOkDetailDto> retreivingUpdateData(String userName,
			List<UpdateConfigOkDetailDto> upsertDataList) {

		//直近画面表示データを取得
		List<PointConfigDsplDataDto> initPointConfigDataList = (List<PointConfigDsplDataDto>) redisService
				.getData("config-data:" + userName);

		//		Map<String, String> retrievedUpdateDataList = new HashMap<String, String>();
		//		List<Map<String, String>> retrievedUpdateDataListList = new ArrayList<>();
		List<UpdateConfigOkDetailDto> retrievedUpdateDataListList = new ArrayList<>();

		System.out.println(initPointConfigDataList.size());
		System.out.println(upsertDataList.size());

		for (int i = 0; i < initPointConfigDataList.size(); i++) {
			for (int j = 0; j < upsertDataList.size(); j++) {

				if (!upsertDataList.get(i).isInsertFlg()) {
					UpdateConfigOkDetailDto updateConfigOkDetailDto = new UpdateConfigOkDetailDto();

					//更新データのみチェック
					//ポイントIDはキー項目とし、画面からの改廃は不可とする
					//どうしても改廃が必要な場合は、TBL直接操作して、updateUserId="ADMIN"で更新
					if (initPointConfigDataList.get(i).getPointMasterId()
							.equals(upsertDataList.get(j).getPointMasterId())) {

						if (!initPointConfigDataList.get(i).getPointName()
								.equals(upsertDataList.get(j).getPointName())) {
							updateConfigOkDetailDto.setPointName(
									upsertDataList.get(j).getPointName());
						}
						if (initPointConfigDataList.get(i).getPoint() != upsertDataList.get(j).getPoint()) {
							updateConfigOkDetailDto.setPoint(upsertDataList.get(j).getPoint());
						}
						if (!initPointConfigDataList.get(i).getUseMethod()
								.equals(upsertDataList.get(j).getUseMethod())) {
							updateConfigOkDetailDto.setUseMethod(upsertDataList.get(j).getUseMethod());
						}
						if (!initPointConfigDataList.get(i).getActiveFlg()
								.equals(upsertDataList.get(j).getActiveFlg())) {
							updateConfigOkDetailDto.setActiveFlg(upsertDataList.get(j).getActiveFlg());
						}
						
						updateConfigOkDetailDto.setPointMasterId(upsertDataList.get(j).getPointMasterId());

						retrievedUpdateDataListList.add(updateConfigOkDetailDto);
					}
				}
			}
		}

		return retrievedUpdateDataListList;
	}
}
