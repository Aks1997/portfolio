package com.portfolio.base.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="users")
public class User {
	
	@Id
	@GeneratedValue
	@Column(name= "id")
	private Long id;
	
	@Column(name= "username")
	private Long userName;
	
	@Column(name= "pass")
	private Long password;
	
	@Column(name= "uname")
	private Long name;
	
	@Column(name= "profession")
	private Long profession;
	
	@Column(name= "about")
	private Long about;
	
	public User() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserName() {
		return userName;
	}

	public void setUserName(Long userName) {
		this.userName = userName;
	}

	public Long getPassword() {
		return password;
	}

	public void setPassword(Long password) {
		this.password = password;
	}

	public Long getName() {
		return name;
	}

	public void setName(Long name) {
		this.name = name;
	}

	public Long getProfession() {
		return profession;
	}

	public void setProfession(Long profession) {
		this.profession = profession;
	}

	public Long getAbout() {
		return about;
	}

	public void setAbout(Long about) {
		this.about = about;
	}
}
