package com.example.renyanyu.server.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

import com.alibaba.fastjson.*;

import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.entity.Exercise;
import com.example.renyanyu.server.entity.History;
import com.example.renyanyu.server.service.UserService;
import com.example.renyanyu.server.service.DataService;
import com.example.renyanyu.server.controller.HttpRequest;

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
	public List<Exercise> getExercise(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "token", required = true) String token){
		Page<Exercise> el = userService.getExercise(token, page);
		return el.getContent();
		
	}
	
	@RequestMapping(value = "/wrongex", method = RequestMethod.GET)
	@ResponseBody
	public List<Exercise> getWrongExercise(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "token", required = true) String token){
		Page<Exercise> el = userService.getWrongExercise(token, page);
		return el.getContent();
	}

	@RequestMapping(value = "/collection", method = RequestMethod.GET)
	@ResponseBody
    public Set<Starred> getCollection(
            @RequestParam(value = "token", required = true) String token
    )
    {
        return userService.getCollection(token);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public List<History> getHistory(
            @RequestParam(value = "token", required = true) String token
    )
    {
        return userService.getHistory(token);
    }
    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    @ResponseBody
    public Set<Exercise> getQuiz(
    		@RequestParam(value = "token", required = true) String token
    )
    {
    	User user = userService.readByUuid(token);
    	if(user == null) return null;
    	Set<Exercise> exercise = new HashSet<Exercise>(getWrongExercise(1, token));
    	List<History> his = user.getHistory();
    	Random random = new Random();
    	int his_size = his.size();
    	int i = random.nextInt(his_size);
    	int times = 0;
    	while(exercise.size() < 10 || times < his_size) {
    		History temp = his.get(i);
    		String name = temp.getName();
    		String ret = RequestController.getExercise(name);
    		if(!ret.equals("failed")) {
    			JSONObject jsonObject = JSONObject.parseObject(ret).getJSONObject("data");
    			List<String> ex = (List<String>) jsonObject;
    			for(String x : ex)
    			{
    				JSONObject data = JSONObject.parseObject(x);
    				if(data.getString("qAnswer").length() == 1) {
    					Exercise tempe = new Exercise();
    					tempe.setIsWrong(false);
    					tempe.setQAnswer(data.getString("qAnswer"));
    					tempe.setQBody(data.getString("qBody"));
    					tempe.setQId(Integer.valueOf(data.getString("qId")).intValue());
    					tempe.setUriname(temp.getUri());
    					exercise.add(tempe);
    				}
    			}
    		}
    		i++;
    		if(i==his_size) i=0;
    		times++;
    	}
    	return exercise;
    }
}
