package com.childcare.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.childcare.point.entity.LineUser;

@Repository
public interface LineUserRepository extends JpaRepository<LineUser,String>{
	/**
	 * パラメータに指定されたLINEユーザIDをもとに、LINEユーザIDを取得
	 *　//TODO パラメータのユーザIDが存在するかどうかだけでOKなので修正 
	 * 
	 * @param userId
	 * @return
	 */
	String findByLineUserId(String userId);
	
	/**
	 * 友達登録されたすべてのLINEユーザを取得
	 */
	List<LineUser> findAll();
}
