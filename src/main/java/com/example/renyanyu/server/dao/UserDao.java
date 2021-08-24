package com.example.renyanyu.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 

import com.example.renyanyu.server.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	User readByName(String name);
	User readByUuid(String uuid);
	
	/*
	@Query("UPDATE boot_user SET displayName = :displayName WHERE uuid = :uuid")
	public int updateDisplayName(@Param("displayName") String displayName, @Param("uuid") String uuid);
	*/
	
}
