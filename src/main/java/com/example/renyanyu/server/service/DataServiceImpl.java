package com.example.renyanyu.server.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.renyanyu.server.dao.UserDao;
import com.example.renyanyu.server.dao.HistoryDao;
import com.example.renyanyu.server.dao.StarredDao;
import com.example.renyanyu.server.dao.ExerciseDao;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.entity.History;
import com.example.renyanyu.server.entity.Starred;
import com.example.renyanyu.server.entity.Exercise;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private HistoryDao historyDao;
	
	@Autowired
	private StarredDao starredDao;
	
	@Autowired
	private ExerciseDao exerciseDao;
	
	public void initData() {
		userDao.deleteAll();
		historyDao.deleteAll();
		starredDao.deleteAll();
		
		User user = new User();
		user.setId(0L);
		user.setName("13321135493");
		user.setDisplayName("admin");
		user.setPassword("123456");
		userDao.save(user);
	}
	
	public int addStar(String token, String name, String type, String uri, String course)
	{
		User user = userDao.readByUuid(token);
		
		if(user == null) return -1;
		Set<Starred> hl = user.getStar();
		Starred star = new Starred();
		star.setId(0L);
		star.setName(name);
		star.setType(type);
		star.setUri(uri);
		star.setCourse(course);
		star.setUser(user);
		if(!hl.contains(star)) {
			hl.add(star);
			user.setStar(hl);
			userDao.save(user);
		}
		else {
			Long id = 0L;
			for(Starred x : hl)
				if(x.getUri().equals(uri)) {
					id = x.getId();
					break;
				}
			hl.remove(star);
			user.setStar(hl);
			userDao.save(user);
			starredDao.deleteById(id);
		}
		return 0;
	}

	public int haveStarred(String token, String name, String type, String uri, String course)
	{
		User user = userDao.readByUuid(token);
		if(user == null) return -1;
		Set<Starred> hl = user.getStar();
		Starred star = new Starred();
		star.setId(0L);
		star.setName(name);
		star.setType(type);
		star.setUri(uri);
		star.setUser(user);
		star.setCourse(course);
		if(hl.contains(star)) return 1;
		return 0;
	}

	public Long addToHistory(String token, String name, String type, String uri, String course)
	{
		User user = userDao.readByUuid(token);
		if(user == null) return -1L;
		List<History> historyList = user.getHistory();
		History history = new History();
		history.setId(0L);
		history.setName(name);
		history.setType(type);
		history.setUri(uri);
		history.setTime(new Date());
		history.setUser(user);
		history.setCourse(course);
		Long id = -1L;
		for(History x:historyList)
			if(x.getUri().equals(uri)) {
				id = x.getId();
				historyList.remove(x);
				break;
			}
		historyList.add(history);
		user.setHistory(historyList);
		userDao.save(user);
		if(id>=0L) historyDao.deleteById(id);;
		return history.getId();
	}
	
	public int deleteFromHistory(String token, Long id)
	{
		User user = userDao.readByUuid(token);
		if(user == null) return -1;
		List<History> historyList = user.getHistory();
		for(History x : historyList)
			if(x.getId().equals(id)) {
				historyList.remove(x);
				user.setHistory(historyList);
				userDao.save(user);
				historyDao.deleteById(id);
				return 0;
			}
		return 1;
	}

	@Override
	public int addExercise(String token, String uriname, 
			String qBody, String qAnswer, boolean isWrong, int qId) {
		User user = userDao.readByUuid(token);
		if(user == null) return -1;
		Exercise exercise = new Exercise();
		exercise.setId(0L);
		exercise.setIsWrong(isWrong);
		exercise.setQAnswer(qAnswer);
		exercise.setQBody(qBody);
		exercise.setQId(qId);
		exercise.setUriname(uriname);
		exercise.setUser(user);
		List<Exercise> hl = user.getExercise();
		Long id = -1L;
		for(Exercise x:hl)
			if(x.equals(exercise)) {
				id = x.getId();
				hl.remove(x);
				break;
			}
		hl.add(exercise);
		user.setExercise(hl);
		userDao.save(user);
		if(id>=0) exerciseDao.deleteById(id);
		return 0;
	}
	
}
