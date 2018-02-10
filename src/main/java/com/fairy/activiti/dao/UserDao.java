package com.fairy.activiti.dao;

import com.fairy.activiti.entity.User;

public interface UserDao {

	User findUserByName(String username);
	User findManager(String username);
	User findBoss();
}
