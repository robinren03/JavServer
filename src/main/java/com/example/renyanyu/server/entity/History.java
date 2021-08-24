package com.example.renyanyu.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="boot_history")
public class History implements Serializable {
	
	private static final long serialVersionUID = 8719878517384639149L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=50, nullable=false)
	private String course;
	
	private String name;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	protected User user;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setCreateDate(Date createdate) {
		this.createdate = createdate;
	}
	
	public Date getCreateDate() {
		return this.createdate;
	}
	
	public History() {
		super();
	}
	
	public History(Long id, String course, String name, User user) {
		super();
		this.id = id;
		this.name = name;
		this.user = user;
	}
}
