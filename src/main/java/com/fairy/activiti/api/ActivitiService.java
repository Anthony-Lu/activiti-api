package com.fairy.activiti.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

public interface ActivitiService {

	/**
	 * 部署流程定义（zip格式）
	 * 
	 * @param file
	 * @param fileName
	 */
	void deployProcessByZip(File file, String fileName);

	/**
	 * 删除流程定义
	 * 
	 * @param deploymentId
	 * @param flag
	 *            为true时表示级联删除，会删除掉和当前部署的规则相关的信息，包括历史信息；否则为普通删除，如果当前部署的规则还存在正在制作的流程，会抛异常。
	 */
	void deleteProcessDefinition(String deploymentId, boolean flag);

	/**
	 * 查询流程部署信息
	 * 
	 * @return
	 */
	List<Deployment> findDeploymentList();

	/**
	 * 查询流程定义信息
	 * 
	 * @return
	 */
	List<ProcessDefinition> findProcessDefinitionList();

	/**
	 * 启动流程
	 * 
	 * @param processDefinitionKey
	 *            流程定义id
	 * @param bizKey
	 *            业务id
	 * @param variables
	 *            流程变量
	 */
	ProcessInstance startProcess(String processDefinitionKey, String bizKey, Map<String, Object> variables);

	/**
	 * 查询指定办理人的待办任务
	 * 
	 * @param assignee
	 * @return
	 */
	List<Task> findTaskByAssignee(String assignee);

	/**
	 * 查询候选人的待办任务
	 * 
	 * @param candidateUser
	 * @return
	 */
	List<Task> findTaskByCandidateUser(String candidateUser);

	/**
	 * 查询候选组的待办任务
	 * 
	 * @param candidateGroup
	 * @return
	 */
	List<Task> findTaskByCandidateGroup(String candidateGroup);

	/**
	 * 设置认证用户（比如设置流程发起人）
	 * 
	 * @param authenticatedUserId
	 */
	void setAuthenticatedUserId(String authenticatedUserId);

	/**
	 * 添加批注信息
	 * 
	 * @param taskId
	 * @param processInstanceId
	 * @param message
	 * @return
	 */
	Comment addComment(String taskId, String processInstanceId, String message);

	/**
	 * 认领任务
	 * 
	 * @param taskId
	 * @param userId
	 */
	void claimTask(String taskId, String userId);

	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param variables
	 */
	void completeTask(String taskId, Map<String, Object> variables);

	/**
	 * 根据流程实例id判断流程是否结束
	 * 
	 * @param processInstanceId
	 * @return
	 */
	boolean isFinished(String processInstanceId);

	/**
	 * 根据当前任务id查询该任务的历史批注
	 * 
	 * @param processInstanceId
	 * @return
	 */
	List<Comment> findComments(String taskId);

	/**
	 * 根据流程部署id和流程图资源名称获取输入流
	 * 
	 * @param deploymentId
	 * @param resourceName
	 * @return
	 */
	InputStream findImgInputStream(String deploymentId, String resourceName);

	/**
	 * 获取活动节点坐标
	 * 
	 * @param processInstanceId
	 * @return
	 */
	Map<String, Integer> findCoordinate(String processInstanceId);

	/**
	 * 使用任务id获取当前任务节点对应的FormKey中对应的值
	 * 
	 * @param taskId
	 * @return
	 */
	String findTaskFormKeyByTaskId(String taskId);

	/**
	 * 获取当前任务完成之后的连线走向，可用于前台展示
	 * 
	 * @param taskId
	 * @return
	 */
	List<String> findOutComeListByTaskId(String taskId);

	/**
	 * 根据流程实例id获取流程实例对象
	 * 
	 * @param processInstanceId
	 * @return
	 */
	ProcessInstance getProcessInstance(String processInstanceId);

	/**
	 * 根据流程定义id获取流程定义实体
	 * 
	 * @param processDefId
	 * @return
	 */
	ProcessDefinitionEntity getProcessDefinitionEntity(String processDefId);

	/**
	 * 根据任务id获取任务对象
	 * 
	 * @param taskId
	 * @return
	 */
	Task getTask(String taskId);

	/**
	 * 根据流程实例id获取当前活动对象
	 * 
	 * @param processInstanceId
	 * @return
	 */
	ActivityImpl getActivityByInstanceId(String processInstanceId);

	/**
	 * 根据任务id获取当前活动对象
	 * 
	 * @param taskId
	 * @return
	 */
	ActivityImpl getActivityByTaskId(String taskId);
}
