package com.fairy.activiti;

import java.util.List;

public class UtilTest {

	public static void main(String[] args) {
		
		A a = new A();
		List<Object> list = a.getList();
		list.add("aaa");
		list.add("666");
		
		
		System.out.println(a);
	}
	
	
}
