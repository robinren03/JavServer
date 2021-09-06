package com.example.renyanyu.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="boot_exercise")
public class Exercise implements Serializable, Comparable<Exercise> {
	
	private static final long serialVersionUID = 8293278516384639149L;
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=50, nullable=false)
	private String uriname;
	
	private String qBody;
	
	private String qAnswer;
	
	private boolean isWrong;
	
	private int qId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	protected User user;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUriname() {
		return uriname;
	}
	
	public void setUriname(String uriname) {
		this.uriname = uriname;
	}
	
	public String getQAnswer() {
		return qAnswer;
	}
	
	public void setQAnswer(String qAnswer) {
		this.qAnswer = qAnswer;
	}
	
	public String getQBody() {
		return qBody;
	}
	
	public void setQBody(String qBody) {
		this.qBody = qBody;
	}
	
	public int getQId() {
		return qId;
	}
	
	public void setQId(int qId) {
		this.qId = qId; 	
	}
	
	public User getUser() {
		return user;
	}
	
	public void setIsWrong(boolean isWrong) {
		this.isWrong = isWrong;
	}
	
	public boolean getIsWrong() {
		return isWrong;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Exercise() {
		super();
	}
	
	public Exercise(Long id, String uriname, String qAnswer, int qId, 
			String qBody, User user, boolean isWrong) {
		super();
		this.id = id;
		this.uriname = uriname;
		this.qAnswer = qAnswer;
		this.qId = qId;
		this.qBody = qBody;
		this.user = user;
		this.isWrong = isWrong;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		Exercise that = (Exercise) obj;
		if(that.qId != this.qId) return false;
		if(!that.user.equals(this.user)) return false;
		return true;
	}
	
	@Override
	public int compareTo(Exercise o) {
		return this.id.compareTo(o.id);
	}
	
	@Override 
	public int hashCode() {
		return (this.qId + "," + this.user.getId().toString()).hashCode();
	}
}
