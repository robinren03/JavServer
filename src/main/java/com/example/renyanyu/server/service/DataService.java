package com.example.renyanyu.server.service;

public interface DataService {
	public void initData();
	public int addHistory(String token, String course, String name);
	public int addStar(String token, String course, String name);
	public int addExercise(String token, String uriname, String qBody, 
			String qAnswer, boolean isWrong,int qId);

}
