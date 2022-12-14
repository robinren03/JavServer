package com.example.renyanyu.server.service;


import com.example.renyanyu.server.entity.History;
import com.example.renyanyu.server.entity.Starred;
import com.example.renyanyu.server.entity.User;

import org.springframework.data.domain.Page;

import com.example.renyanyu.server.entity.Exercise;

import java.util.List;
import java.util.Set;

public interface UserService {
	User readByName(String name);
	User readByUuid(String uuid);
	void updateUser(User user);
	Page<Exercise> getExercise(String token, int page);
	Page<Exercise> getWrongExercise(String token, int page);
	Set<Starred> getCollection(String token);
	List<History> getHistory(String token);
}
