package com.childcare.point.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateOkDto {
	private String userName;
	private int currentPoint;
	/**
	 *stockまたはuseエンドポイントでリクエストする「pointIdとpointのリスト」を以下に格納
	 *JSONのオブジェクト名が「pointData」のため、フィールド名を「pointData」に設定
	 *また念のため、@JsonPropertyアノテーションでJSONのオブジェクト名を指定
	 */
	@JsonProperty("pointData")
	private List<UpdateOkDetailDto> pointData;
}
