package com.childcare.point.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateConfigOkDto {
	@JsonProperty("userName")
	private String userName;
	/**
	 *config/updateエンドポイントでリクエストする「UpdateConfigOkDetailDtoのリスト」を以下に格納
	 *JSONのオブジェクト名が「insertPointConfigData/updatePointConfigData」のため、
	 *フィールド名を「insertPointConfigData/updatePointConfigData」に設定
	 *また念のため、@JsonPropertyアノテーションでJSONのオブジェクト名を指定
	 */
	@JsonProperty("initDataList")
	private List<UpdateConfigOkDetailDto> initDataList;
	@JsonProperty("upsertDataList")
	private List<UpdateConfigOkDetailDto> upsertDataList;
//	@JsonProperty("updatePointConfigData")
//	private List<UpdateConfigOkDetailDto> updatePointConfigData;
}
