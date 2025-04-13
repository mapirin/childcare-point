package com.childcare.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.childcare.point.entity.UserPoint;

public interface UserPointRepository extends JpaRepository<UserPoint, String> {
	//表示するポイントを取得
//	@Query("SELECT u FROM userPoint u WHERE u.userName = :userName")
	UserPoint findByUserName(@Param("userName")String userName);

}
