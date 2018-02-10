package com.fairy.activiti;

public enum Season {

	SPRING(1,"春天"),
	SUMMER(2,"夏天"), 
	AUTUNM(3,"秋天"),
	WINTER(4,"冬天");

	private int key;
	private String value ;
	
	private Season(int key,String value) {

		this.key = key;
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * 根据key获取value
	 * @param key
	 * @return
	 */
	public static String getValueByKey(int key) {
		Season[] seasons = Season.values();
		for (Season season : seasons) {
			if(season.getKey() == key) {
				return season.getValue();
			}
		}
		return "";
	}
}
