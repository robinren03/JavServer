package com.example.renyanyu.server.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.renyanyu.server.service.DataService;

@RestController
@RequestMapping("/request")
public class RequestController {
	@Autowired
	private DataService dataService;
	
	private static String id = "";
	
	private static void setupId()
	{
		String json = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login", "phone=13321135493&password=password1");
		if(!json.equals("failed")) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			id = jsonObject.getString("id");
			
		}
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@ResponseBody
	public String searchByCourse(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "searchKey", required = true) String searchKey) {
		
		LinkedHashMap<String, String> request = new LinkedHashMap<String, String>();
		request.put("course", course);
		request.put("searchKey", searchKey);
		for(int i=0; i<=2; i++) {
			request.put("id", id);
			String temp = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					System.out.println(temp);
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value="/instance", method = RequestMethod.GET)
	@ResponseBody
	public String getInstanceInfo(
			HttpServletRequest request,
			@RequestParam(value = "course", required = false) String course,
			@RequestParam(value = "name", required = true) String name) {
		String token = request.getHeader("Token");
		LinkedHashMap<String, String> request1 = new LinkedHashMap<String, String>();
		if(course != null) request1.put("course", course);
		request1.put("name", name);
		for(int i=0; i<=2; i++) {
			request1.put("id", id);
			String temp = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName", request1);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					if(token != null) {
						int ret = dataService.addHistory(token, course, name);
						if(ret != 0) {
							jsonObject.put("user-online", false);
						}else
							jsonObject.put("user-online", true);
					}
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value = "/question", method = RequestMethod.POST)
	@ResponseBody
	public String getQuestion(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "inputQuestion", required = true) String inputQuestion
			)
	{
		
		String preReq = "course=" + course + "&inputQuestion=" + inputQuestion;
		for(int i=0; i<=2; i++) {
			String request = preReq + "&id=" + id;
			String temp = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value = "/link", method = RequestMethod.POST)
	@ResponseBody
	public String getLink(
			@RequestParam(value = "course", required = false) String course,
			@RequestParam(value = "context", required = true) String context
			)
	{
		
		String preReq = "context=" + context;
		if(course != null) preReq += "&course=" + course;
		for(int i=0; i<=2; i++) {
			String request = preReq + "&id=" + id;
			String temp = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value="/exercise", method = RequestMethod.GET)
	@ResponseBody
	public String getExercise(
			HttpServletRequest request,
			@RequestParam(value = "uriName", required =true) String uriName) {
		String token = request.getHeader("Token");
		LinkedHashMap<String, String> request1 = new LinkedHashMap<String, String>();
		request1.put("uriName", uriName);
		for(int i=0; i<=2; i++) {
			request1.put("id", id);
			String temp = HttpRequest.sendGet(
					"http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName", request1);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					if(token != null) {
					}
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value = "/related", method = RequestMethod.POST)
	@ResponseBody
	public String getRelated(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "subjectName", required = true) String subjectName
			)
	{
		
		String preReq =  "subjectName=" + subjectName + "&course=" + course;
		for(int i=0; i<=2; i++) {
			String request = preReq + "&id=" + id;
			String temp = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value = "/card", method = RequestMethod.POST)
	@ResponseBody
	public String getCard(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "uri", required = true) String uri
			)
	{
		
		String preReq =  "uri=" + uri + "&course=" + course;
		for(int i=0; i<=2; i++) {
			String request = preReq + "&id=" + id;
			String temp = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/getKnowledgeCard", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				System.out.println(temp);
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					return temp;
				}
				else setupId();
			}
		}
		return "failed";
	}
	
	@RequestMapping(value = "/star", method = RequestMethod.GET)
	@ResponseBody
	public String makeStarred(
			HttpServletRequest request,
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "name", required = true) String name
			)
	{
		String token = request.getHeader("Token");
		int ret = dataService.addStar(token, course, name);
		if (ret == 0) return "success";
		return "failed";
	}
}

/*
class HistoryChange implements Runnable {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private HistoryDao historyDao;
	
	private Thread t;
	private String name, course ,token;
	HistoryChange(String token, String course, String name){
		this.token = token;
		this.course = course;
		this.name = name;
	}
	
	public void run() {
		System.out.println(token);
		User user = userDao.readByUuid(token);
		if(user == null) {
			System.out.println("USER IS NULL");
			return;
		}
		History history = new History();
		history.setId(0L);
		history.setCourse(course);
		history.setName(name);
		history.setUser(user);
		history.setCreateDate(new Date());
		historyDao.save(history);
		List<History> hl = user.getHistory();
		hl.add(history);
		user.setHistory(hl);
		userDao.save(user);
	}
	
	public void start() {
		if (t == null) {
	         t = new Thread (this);
	         t.start ();
	      }
	}
}*/