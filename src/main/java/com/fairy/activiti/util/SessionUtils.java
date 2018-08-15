package com.fairy.activiti.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fairy.activiti.entity.User;

/**
 * session工具类
 * @author luxuebing
 * @date 2018/02/06下午9:59:52
 */
public class SessionUtils {
	
	private static final String GLOBLE_SESSION_KEY = "user";
	
	public static User get() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return (User) request.getSession().getAttribute(GLOBLE_SESSION_KEY);
	}
	
	public static void set(String username) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(username != null) {
			request.getSession().setAttribute(GLOBLE_SESSION_KEY, username);
		}else{
			request.getSession().removeAttribute(GLOBLE_SESSION_KEY);
		}
	}
}
