package com.fairy.activiti.constant;

/**
 * 请假单的状态常量类
 * 
 * @author luxuebing
 * @date 2018/02/06下午9:45:04
 */
public class LeaveBillConstant {

	private LeaveBillConstant() {
		
	}
	
	public static final int START_ENTRY = 0;// 初始状态
	
	public static final int AUDIT_ING = 1;// 审核中
	
	public static final int AUDIT_COMPLETE = 2;// 审核完成
}
