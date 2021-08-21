package com.example.renyanyu.server.service;

import com.example.renyanyu.server.entity.User;

public interface UserService {
	User readByName(String name);
}
