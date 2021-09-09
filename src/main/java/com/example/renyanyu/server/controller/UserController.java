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
	public Map<String, String> getWrongExercise(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "token", required = true) String token){
		
		Page<Exercise> el = userService.getWrongExercise(token, page);
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", el.getContent().toString());
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
//		Random random = new Random();
//		if(historyList.size()>2)
//		{
//			for(int i=0,k=0;k<2&&i<(historyList.size()-2);i++)
//			{
//				History history = historyList.get(2+random.nextInt(historyList.size()-2));
//				String uri=history.getUri();
//				if(uriSet.contains(uri))
//				{
//					continue;
//				}
//				else
//				{
//					k++;
//					uriSet.add(uri);
//				}
//				String ret = RequestController.getExercise(history.getName());
//				if(!ret.equals("failed")) {
//					JSONArray jsonArray = (JSONArray)(JSONObject.parseObject(ret).get("data"));
//					for(Object jsonObject:jsonArray)
//					{
//						questionList.add((JSONObject) jsonObject);
//					}
//				}
//			}
//		}


//
//		System.out.println("IN!!!!");
//
//
//		System.out.println("Here");
//    	Set<Exercise> exercise = new HashSet<Exercise>(getWrongExercise(1, token));
//
//
//    	Random random = new Random();
//    	int his_size = his.size();
//    	int i = random.nextInt(his_size);
//    	int times = 0;
//		System.out.println("doWhile");
//		List<JSONObject> questionList=new ArrayList<>();
//    	while(questionList.size() < 200 || times < his_size) {
//    		History temp = his.get(i);
//    		String name = temp.getName();
//    		String ret = RequestController.getExercise(name);
//    		if(!ret.equals("failed")) {
//    			JSONArray jsonArray = (JSONArray)(JSONObject.parseObject(ret).get("data"));
//
//				System.out.println("All is well");
//				List<String> ex=new ArrayList<>();
//				for(Object jsonObject:jsonArray)
//				{
//					questionList.add((JSONObject) jsonObject);
////					ex.add(jsonObject.toString());
//				}
//////    			List<String> ex = (List<String>) jsonArray;
////				System.out.println("Nothing wrong");
////    			for(String x : ex)
////    			{
////    				JSONObject data = JSONObject.parseObject(x);
////    				if(data.getString("qAnswer").length() == 1) {
////    					Exercise tempe = new Exercise();
////    					tempe.setIsWrong(false);
////    					tempe.setQAnswer(data.getString("qAnswer"));
////    					tempe.setQBody(data.getString("qBody"));
////    					tempe.setQId(Integer.valueOf(data.getString("id")).intValue());
////    					tempe.setUriname(temp.getUri());
////    					exercise.add(tempe);
////    				}
////    			}
////				System.out.println("OK");
//    		}
//    		i++;
//    		if(i==his_size) i=0;
//    		times++;
//    	}
//		System.out.println("success");
		System.out.println("success!!!!");
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
}
