package com.childcare.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "line_users")
@Data
public class LineUser {

	@Id
	private String lineUserId;
}
