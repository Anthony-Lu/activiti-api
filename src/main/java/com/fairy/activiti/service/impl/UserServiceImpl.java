package com.fairy.activiti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fairy.activiti.dao.UserDao;
import com.fairy.activiti.entity.User;
import com.fairy.activiti.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Override
	public User login(String name) {
		return userDao.findUserByName(name);
		
	}
	@Override
	public User getManagerId(String userId) {
		return userDao.findManager(userId);
	}

}
