package com.example.renyanyu.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.renyanyu.server.entity.Exercise;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.dao.UserDao;
import com.example.renyanyu.server.dao.ExerciseDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ExerciseDao exerciseDao;
	
	private int size = 15;
	
	@Override
	public User readByName(String name) {
		return userDao.readByName(name);
	}
	
	@Override
	public void updateUser(User user) {
		userDao.save(user);
	}
	
	@Override
	public Page<Exercise> getExercise(String token, int page) {
		User user = userDao.readByUuid(token);
		if(user == null) return null;
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "id");
		return exerciseDao.findByUser(user, pageable);
	}
	
	@Override
	public Page<Exercise> getWrongExercise(String token, int page) {
		User user = userDao.readByUuid(token);
		if(user == null) return null;
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "id");
		return exerciseDao.findByUserAndIsWrong(user, true, pageable);
	}
	
}
