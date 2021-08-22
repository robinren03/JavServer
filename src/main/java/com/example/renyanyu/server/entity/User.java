package com.example.renyanyu.server.entity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.renyanyu.server.entity.History;

@Entity
@Table(name="boot_user")
public class User implements Serializable {
	private static final long serialVersionUID = -6550777752269466791L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=50,nullable=false)
	private String name;
	
	private String displayName;
	
	private String password;
	
	@Column(length=50,nullable=true)
	private String UUID;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user" , fetch = FetchType.EAGER)	
	private List<History> history = new ArrayList<History>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	} 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;	
	}
	public List<History> getHistory(){
		return history;
	}
	public void setHistory(List<History> history){
		this.history = history;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String UUID) {
		this.UUID = UUID;
	}
	public User() {
		super();
	}
	
	public User(Long id, String name, String displayName, String password
			, List<History> history, String UUID)
	{
		super();
		this.id = id;
		this.name = name;		
		this.displayName = displayName;
		this.password = password;
		this.history = history;
		this.UUID = UUID;
	} 
}