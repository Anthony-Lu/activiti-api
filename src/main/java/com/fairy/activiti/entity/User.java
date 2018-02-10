package com.fairy.activiti.entity;

import java.io.Serializable;

/**
 * 用户实体
 * 对应表user
 * @author luxuebing
 * @date 2018/02/05上午11:05:08
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8401962023282880423L;

	private Integer id;
	private String name;
	private String password;
	private String role;
	private String email;
	private Integer managerId;
	private User manager;

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
}
