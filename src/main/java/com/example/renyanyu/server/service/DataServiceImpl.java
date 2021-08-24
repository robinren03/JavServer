package com.example.renyanyu.server.service;

import java.util.LinkedList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.renyanyu.server.dao.UserDao;
import com.example.renyanyu.server.dao.HistoryDao;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.entity.History;
import com.example.renyanyu.server.service.UserService;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private HistoryDao historyDao;
	
	public void initData() {
		userDao.deleteAll();
		historyDao.deleteAll();
		
		User user = new User();
		user.setId(0L);
		user.setName("13321135493");
		user.setDisplayName("admin");
		user.setPassword("123456");
		List<History> hl= new LinkedList<History>();
		user.setHistory(hl);
		userDao.save(user);
	}

	public void addHistory(String token, String course, String name)
	{
		User user = userDao.readByUuid(token);
		
		if(user == null) return;
		
		List<History> hl = user.getHistory();
		for(Iterator<History> it= hl.iterator(); it.hasNext(); )
		{
			History x = it.next();
			if(x.getName().equals(name) && x.getCourse().equals(course)) {
				System.out.println("equal");
				History p = x;
				hl.remove(x);
				historyDao.delete(p);
				break;
			}
		}
		
		History history = new History();
		history.setId(0L);
		history.setCourse(course);
		history.setName(name);
		history.setUser(user);
		history.setCreateDate(new Date());
		
		hl.add(history);
		System.out.println(hl);
		int len = hl.size();
		if(len>=2000) hl=hl.subList(len-2000, len);
		user.setHistory(hl);
		userDao.save(user);
		return;
	}
}
