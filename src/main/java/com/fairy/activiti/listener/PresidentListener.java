package com.fairy.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 设置总裁审批监听器
 * @author luxuebing
 * @date 2018/02/05下午11:26:44
 */
public class PresidentListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String assignee = "president";
		delegateTask.setAssignee(assignee);
	}

}
