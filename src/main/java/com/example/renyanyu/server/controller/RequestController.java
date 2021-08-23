package com.example.renyanyu.server.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.service.UserService;
import com.example.renyanyu.server.service.DataService;

@RestController
@RequestMapping("/request")
public class RequestController {
	private static String id = "";
	
	private static void setupId()
	{
		String json = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login", "phone=13321135493&password=password1");
		if(!json.equals("failed")) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			id = jsonObject.getString("id");
			
		}
	}
	
	private static void addHistory(String token, String course, String name)
	{
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@ResponseBody
	public String searchByCourse(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "searchKey", required = true) String searchKey) {
		Map<String, Object> result = new HashMap<String, Object>();
		
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
						addHistory(token, course, name);
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
						//addHistory(token, course, name);
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
}