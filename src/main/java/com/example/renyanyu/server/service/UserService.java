package com.example.renyanyu.server.service;


import com.example.renyanyu.server.entity.User;

import org.springframework.data.domain.Page;

import com.example.renyanyu.server.entity.Exercise;

public interface UserService {
	User readByName(String name);
	void updateUser(User user);
	Page<Exercise> getExercise(String token, int page);
	Page<Exercise> getWrongExercise(String token, int page);
}
