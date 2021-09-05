package com.example.renyanyu.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.renyanyu.server.service.DataService;

import joinery.DataFrame;
import joinery.DataFrame.Predicate;

@RestController
@RequestMapping("/request")
public class RequestController {
	@Autowired
	private DataService dataService;
	
	private static String id = "";
	
	private static final String[] subjects = new String[] {
			"chinese", "english", "math", "physics", "chemistry",
			"biology", "history", "geo", "politics"
	};
	
	private static void setupId()
	{
		String json = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeAuth/user/login", "phone=13321135493&password=password1");
		if(!json.equals("failed")) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			id = jsonObject.getString("id");
			
		}
	}
	
	private String searchByCourse(String course, String searchKey) {
		LinkedHashMap<String, String> request = new LinkedHashMap<String, String>();
		request.put("searchKey", searchKey);
		request.put("course", course);
		for(int i=0; i<=2; i++) {
		  request.put("id", id);
		  String temp = HttpRequest.sendGet("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList", request);
		  if(temp.equals("failed")) {
				setupId();
		  } else {
			JSONObject jsonObject = JSONObject.parseObject(temp);
			if(jsonObject.getString("code").equals("0")) {
				return temp;
			}
			else setupId();
		  }
		}
		return "failed";
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@ResponseBody
	public String getSearch(
			@RequestParam(value = "course", required = false) String course,
			@RequestParam(value = "searchKey", required = true) String searchKey) {
		
		if(course == null) {
			JSONArray list = new JSONArray();
			for(int i=0; i<9; i++)
			{
				String gotResult = searchByCourse(subjects[i], searchKey);
				if (!gotResult.equals("failed")) {
					JSONObject jsonObject = JSONObject.parseObject(gotResult);
					list.addAll(jsonObject.getJSONArray("data"));
				}
			}
			JSONObject result = new JSONObject();
			result.put("code", "0");
			result.put("msg", "成功");
			result.put("data", list);
			return result.toString();
				
		}else {
			return searchByCourse(course, searchKey);
		}
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
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) {
					if(token != null) {
						String realname = jsonObject.getJSONObject("data").getString("uri");
						int ret = dataService.addHistory(token, course, realname);
						if(ret != 0) jsonObject.put("user-online", false);
						else jsonObject.put("user-online", true);
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
			HttpServletRequest req,
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "uri", required = true) String uri
			)
	{
		String token = null;
		if(req != null) {
			token = req.getHeader("Token");
		}
		String preReq =  "uri=" + uri + "&course=" + course;
		for(int i=0; i<=2; i++) {
			String request = preReq + "&id=" + id;
			String temp = HttpRequest.sendPost("http://open.edukg.cn/opedukg/api/typeOpen/open/getKnowledgeCard", request);
			if(temp.equals("failed")) {
				setupId();
			} else {
				JSONObject jsonObject = JSONObject.parseObject(temp);
				if(jsonObject.getString("code").equals("0")) 
				{
					if(token != null) {
						int ret = dataService.addHistory(token, course, uri);
						if(ret != 0) {
							jsonObject.put("user-online", false);
						}else
							jsonObject.put("user-online", true);
					}
					return temp.toString();
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



	
	@RequestMapping(value="/donexercise", method = RequestMethod.POST)
	@ResponseBody
	public String addExercise(
			HttpServletRequest request,
			@RequestParam(value = "uriname", required = true) String uriname,
			@RequestParam(value = "qBody", required = true) String qBody,
			@RequestParam(value = "qAnswer", required = true) String qAnswer,
			@RequestParam(value = "isWrong", required = true) boolean isWrong,
			@RequestParam(value = "qId", required = true) int qId
			)
	{
		String token = request.getHeader("Token");
		if(token == null) return "failed";
		int ret = dataService.addExercise(token, uriname, qBody, qAnswer, isWrong, qId);
		if (ret == 0) return "success";
		return "failed";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getList(
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "page", required = true)int page)
	{
		Resource resource = new ClassPathResource("./"+ course + ".csv");
		try {
			InputStream is = resource.getInputStream();
			DataFrame<Object> dt = DataFrame.readCsv(is);
			List<Object> ls = dt.select(new Predicate<Object>() {
				@Override
				public Boolean apply(List<Object> values) {
					return String.class.cast(values.get(1)).equals("http://www.w3.org/2000/01/rdf-schema#label");
				}
			}).slice(page*15, page*15 + 14).col(0);
			List<Object> req = new ArrayList<Object>();
			for(Object uri:ls)
			{
				String temp = getCard(null, course, uri.toString());
				if(!temp.equals("failed")) {
					JSONObject jsonObject = JSONObject.parseObject(temp);
					Map<String, Object> map = (Map<String, Object>)jsonObject.getJSONObject("data");
					map.put("entity_url", uri.toString());
					req.add(map);
				}
			}
			return req;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getRecommend(
			@RequestParam(value = "course", required = true) String course)
	{
		Resource resource = new ClassPathResource("./"+ course + ".csv");
		try {
			InputStream is = resource.getInputStream();
			DataFrame<Object> dt = DataFrame.readCsv(is);
			dt = dt.select(new Predicate<Object>() {
				@Override
				public Boolean apply(List<Object> values) {
					return String.class.cast(values.get(1)).equals("http://www.w3.org/2000/01/rdf-schema#label");
				}
			});
			int len = dt.length() / 5;
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE);
			int page = (year * 10000) % len+ month * 100 + day;
			page = page % len;
			List<Object> ls = dt.slice(page*5, page*5 + 4).col(0);
			List<Object> req = new ArrayList<Object>();
			for(Object uri:ls)
			{
				String temp = getCard(null, course, uri.toString());
				if(!temp.equals("failed")) {
					JSONObject jsonObject = JSONObject.parseObject(temp);
					Map<String, Object> map = (Map<String, Object>)jsonObject.getJSONObject("data");
					map.put("entity_url", uri.toString());
					req.add(map);
				}
			}
			return req;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	
}
