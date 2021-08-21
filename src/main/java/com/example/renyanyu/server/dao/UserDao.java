package com.example.renyanyu.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.renyanyu.server.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	User readByName(String name);
}
