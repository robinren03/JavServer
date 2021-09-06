package com.example.renyanyu.server.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.example.renyanyu.server.dao.UserDao;
import com.example.renyanyu.server.entity.Starred;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.renyanyu.server.entity.History;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.entity.Exercise;
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
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> readByUsername(
			@RequestParam(value = "username", required = true) String userName,
			@RequestParam(value = "password", required = true) String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userService.readByName(userName);
		if(user == null) {
			result.put("error", 1);
			result.put("errormsg", "没有此用户");
			return result;
		}
		String credential = user.getPassword();
		if (! credential.equals(password)) {
			result.put("error", 1);
			result.put("errormsg", "用户名或密码错误");
			return result;
		}
		
		user.setUUID(java.util.UUID.randomUUID().toString());
		userService.updateUser(user);
		
		result.put("error", 0);
		result.put("name", user.getName());
		result.put("displayName", user.getDisplayName());
		
		result.put("history", user.getHistory());
		result.put("Token", user.getUUID());
		result.put("star", user.getStar());
		return result;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(
			@RequestParam(value = "username", required = true) String userName,
			@RequestParam(value = "displayname", required = true) String displayName,
			@RequestParam(value = "password", required = true) String password) {
		User user = userService.readByName(userName);
		if(user != null) { 
			return "当前手机号已被注册";
		}
		user = new User();
		user.setId(0L);
		user.setName(userName);
		user.setDisplayName(displayName);
		user.setPassword(password);
		userService.updateUser(user);
		return "success";
	}
	
	@RequestMapping(value = "/exercise", method = RequestMethod.GET)
	@ResponseBody
	public List<Exercise> getExercise(HttpServletRequest request,
			@RequestParam(value = "page", required = true) int page){
		String token = request.getHeader("Token");
		if(token == null) return null;
		Page<Exercise> el = userService.getExercise(token, page);
		return el.getContent();
		
	}
	
	@RequestMapping(value = "/wrongex", method = RequestMethod.GET)
	@ResponseBody
	public List<Exercise> getWrongExercise(HttpServletRequest request,
			@RequestParam(value = "page", required = true) int page){
		String token = request.getHeader("Token");
		if(token == null) return null;
		Page<Exercise> el = userService.getWrongExercise(token, page);
		return el.getContent();
	}

	@RequestMapping(value = "/collection", method = RequestMethod.GET)
	@ResponseBody
    public Set<Starred> getCollection(
            HttpServletRequest request,
            @RequestParam(value = "token", required = true) String token
    )
    {
        if(token == null) return null;
        return userService.getCollection(token);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public List<History> getHistory(
            HttpServletRequest request,
            @RequestParam(value = "token", required = true) String token
    )
    {
        if(token == null) return null;
        return userService.getHistory(token);
    }

}
