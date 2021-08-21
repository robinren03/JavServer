package com.example.renyanyu.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.renyanyu.server.dao.UserDao;
import com.example.renyanyu.server.dao.HistoryDao;
import com.example.renyanyu.server.entity.User;
import com.example.renyanyu.server.entity.History;

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
		List<History> hl= new ArrayList<History>();
		user.setHistory(hl);
		userDao.save(user);
	}

}
