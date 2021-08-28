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

	public int addHistory(String token, String course, String name)
	{
		User user = userDao.readByUuid(token);
		
		if(user == null) return -1;
		
		List<History> hl = user.getHistory();
		
		History history = new History();
		history.setId(0L);
		history.setCourse(course);
		history.setName(name);
		history.setUser(user);
		history.setCreateDate(new Date());
		Long id = -1L;
		for(History x:hl)
			if(x.equals(history)) {
				id = x.getId();
				hl.remove(x);
				break;
			}
		hl.add(history);
		user.setHistory(hl);
		userDao.save(user);
		if(id>=0L) historyDao.deleteById(id);
		return 0;
	}
	
	public int addStar(String token, String course, String name)
	{
		User user = userDao.readByUuid(token);
		
		if(user == null) return -1;
		Set<Starred> hl = user.getStar();
		Starred star = new Starred();
		star.setId(0L);
		star.setCourse(course);
		star.setName(name);
		star.setUser(user);
		if(!hl.contains(star)) {
			hl.add(star);
			user.setStar(hl);
			userDao.save(user);
		}
		else {
			Long id = 0L;
			for(Starred x : hl)
				if(x.getName().equals(name) && x.getCourse().equals(course)) {
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
