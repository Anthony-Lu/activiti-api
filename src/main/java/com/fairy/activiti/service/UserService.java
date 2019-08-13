package com.fairy.activiti.service;

import com.fairy.activiti.entity.User;

public interface UserService {
	User login(String name);
	User getManagerId(String userId);
}
