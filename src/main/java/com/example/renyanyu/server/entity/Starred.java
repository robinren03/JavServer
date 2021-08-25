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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="boot_starred")
public class Starred implements Serializable, Comparable<Starred> {
	
	private static final long serialVersionUID = 8293278516384639149L;
	
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
	
	public Starred() {
		super();
	}
	
	public Starred(Long id, String course, String name, User user) {
		super();
		this.id = id;
		this.name = name;
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		Starred that = (Starred) obj;
		if(!that.name.equals(this.name)) return false;
		if(!that.course.equals(this.course)) return false;
		if(!that.user.equals(this.user)) return false;
		return true;
	}
	
	@Override
	public int compareTo(Starred o) {
		return this.id.compareTo(o.id);
	}
	
	@Override 
	public int hashCode() {
		return (this.course + this.name + this.user.getId()).hashCode();
	}
}
