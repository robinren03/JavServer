package com.example.renyanyu.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.service.UserService;
import com.example.renyanyu.server.service.DataService;

@RestController
@RequestMapping("/user")
public class UserController {
	protected static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private DataService dataService;
	@RequestMapping("/initdata")
	@ResponseBody
	public String initData() {
		dataService.initData();
		return "success";
	}
	@RequestMapping("/getUserByLoginName/{name}")
	@ResponseBody
	public Map<String, Object> readByUsername(@PathVariable String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userService.readByName(name);
		Assert.notNull(user);
		System.out.println(user);
		result.put("name", user.getName());
		result.put("displayName", user.getDisplayName());
		result.put("history", user.getHistory());
		return result;
	}
}
