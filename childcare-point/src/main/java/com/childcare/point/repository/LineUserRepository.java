package com.childcare.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.childcare.point.entity.LineUser;

@Repository
public interface LineUserRepository extends JpaRepository<LineUser,String>{
	List<LineUser> findAll();
}
