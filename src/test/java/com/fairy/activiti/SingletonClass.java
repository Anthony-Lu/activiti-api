package com.fairy.activiti;

public class SingletonClass {

	private static volatile SingletonClass instance = null;
	
	private SingletonClass(){
		
	}
	
	public static SingletonClass getInstance (){
		
		if(instance == null) {
			synchronized (SingletonClass.class) {
				if(instance == null) {
					instance = new SingletonClass();
				}
			}
		}
		return instance;
	}
}

enum Singleton{
	INSTANCE;
}