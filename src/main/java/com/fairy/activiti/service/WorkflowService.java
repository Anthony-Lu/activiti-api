package com.fairy.activiti.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.fairy.activiti.bean.WorkflowBean;
import com.fairy.activiti.entity.LeaveBill;

public interface WorkflowService {

	void saveNewDeploye(File file, String filename);

	List<Deployment> findDeploymentList();

	List<ProcessDefinition> findProcessDefinitionList();

	InputStream findImageInputStream(String deploymentId, String imageName);

	InputStream findImageInputStream(String id);
	
	void deleteProcessDefinitionByDeploymentId(String deploymentId);

	void saveStartProcess(WorkflowBean workflowBean);

	List<Task> findTaskListByName(String name);

	String findTaskFormKeyByTaskId(String taskId);

	LeaveBill findLeaveBillByTaskId(String taskId);

	List<String> findOutComeListByTaskId(String taskId);

	void saveSubmitTask(WorkflowBean workflowBean);

	List<Comment> findCommentByTaskId(String taskId);

	List<Comment> findCommentByLeaveBillId(String id);

	ProcessDefinition findProcessDefinitionByTaskId(String taskId);
	
	ProcessDefinition findProcessDefinitionByLeaveId(String leaveId);
	
	Map<String, Integer> findCoordinateByTask(String taskId);
	
	Map<String, Integer> findCoordinateByLeaveId(String leaveId);
	
}
