package com.example.renyanyu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Override
	public User readByName(String name) {
		return userDao.readByName(name);
	}

}
