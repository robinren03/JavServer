package com.example.renyanyu.server.controller;

import java.util.*;

import com.example.renyanyu.server.entity.Starred;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
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
	public Map getWrongExercise(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "token", required = true) String token){
		
		Page<Exercise> el = userService.getWrongExercise(token, page);
		Map map = new HashMap<>();
		map.put("content", el.getContent());
		map.put("pages", String.valueOf(el.getTotalPages()));
		return map;
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
    public List<JSONObject> getQuiz(
    		@RequestParam(value = "token", required = true) String token
    )
    {
		User user = userService.readByUuid(token);
		if(user == null) return null;
		List<History> historyList = user.getHistory();
		historyList.sort(History::compareTo);
		List<JSONObject> questionList=new ArrayList<>();
		HashSet<String> uriSet=new HashSet<>();
		Random random = new Random();
		//TODO:之后继续完善：1.没有历史也能做题。2.不是简单地随机historyList.size()次，而是找找还有没有没试过的
		//TODO:题目去重
		int k=0,i=0;
		while(k<2&&i<10)
		{
			i++;
			History history = historyList.get(random.nextInt(historyList.size()));
			String uri=history.getName();
			if(uriSet.contains(uri))
			{
				System.out.println("while(1)");
				continue;
			}
			else
			{
				k++;
				uriSet.add(uri);
			}
			String ret = RequestController.getExercise(history.getName());
			if(!ret.equals("failed")) {
				JSONArray jsonArray = (JSONArray)(JSONObject.parseObject(ret).get("data"));
				for(Object jsonObject:jsonArray)
				{
					questionList.add((JSONObject) jsonObject);
				}
			}
		}
		if(questionList.size()>0)
		{
			System.out.println(questionList);
			return questionList;
		}
    	else
    	{
    		return null;
		}
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateUser(
          @RequestParam(value = "displayName", required = false) String displayName,
          @RequestParam(value = "password", required = false) String password,
          @RequestParam(value = "old_password", required = false) String old_password,
          @RequestParam(value = "token", required = true) String token,
          @RequestParam(value = "mode", required = true) String mode)
    {
       User user = userService.readByUuid(token);
       if(user == null) {
          return "invalid token";//登录过期或已在别的设备上登录
       }
       if(Objects.equals(mode, "modifyDisplayName"))
       {
          if(displayName != null) user.setDisplayName(displayName);
          userService.updateUser(user);
          return "successfully modified display name";
       }
       else if(Objects.equals(mode, "modifyPassword"))
       {
          if(! user.getPassword().equals(old_password)) {
             return "wrong password";//密码与原密码不符
          }
          if(password != null) user.setPassword(password);
          userService.updateUser(user);
          return "successfully modified password";
       }
       else
       {
          return "illegal mode";
       }
    }
}
