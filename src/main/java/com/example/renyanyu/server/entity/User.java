package com.example.renyanyu.server.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="boot_user")
public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = -6550777752269466791L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=50,nullable=false)
	private String name;
	
	private String displayName;
	
	private String password;
	
	@Column(length=50,nullable=true)
	private String uuid;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user" , fetch = FetchType.EAGER)	
	private List<History> history = new LinkedList<History>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user" , fetch = FetchType.EAGER)	
	private Set<Starred> star = new TreeSet<Starred>();
	
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
	public Set<Starred> getStar(){
		return star;
	}
	public void setStar(Set<Starred> star){
		this.star= star;	
	}
	public String getUUID() {
		return uuid;
	}
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	public User() {
		super();
	}
	
	public User(Long id, String name, String displayName, String password
			,String uuid, List<History> history, Set<Starred> star)
	{
		super();
		this.id = id;
		this.name = name;		
		this.displayName = displayName;
		this.password = password;
		this.history = history;
		this.uuid = uuid;
		this.star = star;
	} 
	
	@Override
	public int compareTo(User o) {
		return -(this.id.compareTo(o.id));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		User o = (User) obj;
		return o.id.equals(this.id);
	}
}