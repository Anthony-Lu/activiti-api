package com.fairy.activiti.enums;

/**
 * 请假单的状态
 * 
 * @author luxuebing
 * @date 2018/02/10下午5:09:19
 */
public enum State {

	STRAT_ENTRY(0, "初始录入"),

	AUDIT_ING(1, "审核中"),

	AUDIT_COMPLETE(2, "审核完成");

	private int key;
	private String value;

	private State(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getValueByKey(int key) {
		State[] values = State.values();
		for (State state : values) {
			if (state.getKey() == key) {
				return state.getValue();
			}
		}
		return "";
	}
}
