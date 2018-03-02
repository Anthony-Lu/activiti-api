package com.fairy.activiti;

public class EnumTest {

	
	public static void main(String[] args) {
		int key = 1;
		Season[] values = Season.values();
		/*for(int i = 0;i < values.length;i++) {
			if(values[i].getKey() == key) {
				//return values[i].getName();
				System.out.println(values[i].getName());
			}
			
		}*/
		
		//Mode.OFF;
		//new User()
		
		for (Season season : values) {
			if(season.getKey() == key) {
				System.out.println(season.getValue());
			}
		}
	}
	
}
