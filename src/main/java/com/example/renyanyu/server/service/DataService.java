package com.example.renyanyu.server.service;

public interface DataService {
	public void initData();
	public int addStar(String token, String name, String type, String uri, String course);
	public int haveStarred(String token, String name, String type, String uri, String course);
	public Long addToHistory(String token, String name, String type, String uri, String course);
	public int deleteFromHistory(String token, Long id);
	public int addExercise(String token, String uriname, String qBody, 
			String qAnswer, boolean isWrong,int qId);

}
