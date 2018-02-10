package com.fairy.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 设置项目经理审批的监听器
 * @author luxuebing
 * @date 2018/02/05下午11:16:34
 */
public class PMListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@Autowired
	//private UserService userService;
	@Override
	public void notify(DelegateTask delegateTask) {
		//通过流程变量获取发起人id
		//String userId = (String) delegateTask.getVariable("applyUser");
		//通过发起人id查询项目经理
		//String managerId = userService.getManagerId(userId);
		//设置修项目经理审批
		String assignee = "PM";
		delegateTask.setAssignee(assignee);
	}
}
