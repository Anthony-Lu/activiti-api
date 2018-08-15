package com.fairy.activiti.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fairy.activiti.util.StringUtils;
/**
 * 
 * @author luxuebing
 * @date 2018/02/28下午2:09:00
 */
@Service
public class ActivitiServiceImpl implements ActivitiService {

	private static final Logger logger = LoggerFactory.getLogger(ActivitiServiceImpl.class);

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private FormService formService;

	/**
	 * 部署流程定义（zip格式）
	 */
	@Override
	public void deployProcessByZip(File file, String fileName) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
			repositoryService.createDeployment().name(fileName).addZipInputStream(zipInputStream);
		} catch (FileNotFoundException e) {
			logger.error("部署名称为{}的流程出错", fileName, e);
		}
	}

	/**
	 * 根据部署id删除流程定义
	 */
	@Override
	public void deleteProcessDefinition(String deploymentId, boolean flag) {

		repositoryService.deleteDeployment(deploymentId, flag);
	}

	/**
	 * 查询流程部署信息（根据部署时间升序排列）
	 */
	@Override
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().asc().list();
		return list;
	}

	/**
	 * 查询流程定义信息（根据版本号升序排列）
	 */
	@Override
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		return list;
	}

	/**
	 * 启动流程
	 */
	@Override
	public ProcessInstance startProcess(String processDefinitionKey, String bizKey, Map<String, Object> variables) {
		return runtimeService.startProcessInstanceByKey(processDefinitionKey, bizKey, variables);
	}

	/**
	 * 查询指定办理人的待办任务（根据任务的创建时间升序排列）
	 */
	@Override
	public List<Task> findTaskByAssignee(String assignee) {
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().asc().list();
		return list;
	}

	/**
	 * 查询候选人的待办任务（根据任务的创建时间升序排列）
	 */
	@Override
	public List<Task> findTaskByCandidateUser(String candidateUser) {
		List<Task> list = taskService.createTaskQuery().taskCandidateUser(candidateUser).orderByTaskCreateTime().asc()
				.list();
		return list;
	}

	/**
	 * 查询候选组的待办任务（根据任务的创建时间升序排列）
	 */
	@Override
	public List<Task> findTaskByCandidateGroup(String candidateGroup) {
		List<Task> list = taskService.createTaskQuery().taskCandidateGroup(candidateGroup).orderByTaskCreateTime().asc()
				.list();
		return list;
	}

	/**
	 * 设置认证用户（比如设置流程发起人）
	 */
	@Override
	public void setAuthenticatedUserId(String authenticatedUserId) {
		identityService.setAuthenticatedUserId(authenticatedUserId);
	}

	/**
	 * 添加批注信息
	 */
	@Override
	public Comment addComment(String taskId, String processInstanceId, String message) {
		return taskService.addComment(taskId, processInstanceId, message);
	}

	/**
	 * 认领任务
	 */
	@Override
	public void claimTask(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	/**
	 * 完成任务
	 */
	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

	/**
	 * 根据流程实例id判断流程是否结束
	 */
	@Override
	public boolean isFinished(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance == null ? true : false;
	}

	/**
	 * 根据当前任务id查询该任务的历史批注
	 */
	@Override
	public List<Comment> findComments(String taskId) {
		Task task = getTask(taskId);
		String processInstanceId = task.getProcessInstanceId();
		List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}

	/**
	 * 获取流程图片输入流
	 */
	@Override
	public InputStream findImgInputStream(String deploymentId, String resourceName) {
		InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		return inputStream;
	}

	/**
	 * 查看流程进度(获取活动节点的坐标)
	 */
	@Override
	public Map<String, Integer> findCoordinate(String processInstanceId) {
		HashMap<String, Integer> map = new HashMap<>();

		ActivityImpl activity = getActivityByInstanceId(processInstanceId);

		map.put("x", activity.getX());
		map.put("y", activity.getY());
		map.put("height", activity.getHeight());
		map.put("width", activity.getWidth());

		return map;
	}

	/**
	 * 获取formKey
	 */
	@Override
	public String findTaskFormKeyByTaskId(String taskId) {
		TaskFormData formData = formService.getTaskFormData(taskId);
		return formData.getFormKey();
	}

	/**
	 * 获取当前任务完成之后的连线走向，可用于前台展示
	 */
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {
		ArrayList<String> outComeList = new ArrayList<>();

		ActivityImpl activity = getActivityByTaskId(taskId);

		// 获取当前活动结束后的连线名称
		List<PvmTransition> list = activity.getOutgoingTransitions();
		for (PvmTransition pvmTransition : list) {
			String name = (String) pvmTransition.getProperty("name");
			if (StringUtils.isNotEmpty(name)) {
				outComeList.add(name);
			}
		}
		return outComeList;
	}

	/**
	 * 根据流程实例id获取流程实例对象
	 */
	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		return instance;
	}

	/**
	 * 根据流程定义id获取流程定义实体
	 */
	@Override
	public ProcessDefinitionEntity getProcessDefinitionEntity(String processDefId) {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefId);
		return processDefinitionEntity;
	}

	/**
	 * 根据任务id获取任务对象
	 */
	@Override
	public Task getTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	/**
	 * 根据流程实例id获取当前活动对象
	 */
	@Override
	public ActivityImpl getActivityByInstanceId(String processInstanceId) {
		// 获取流程实例对象
		ProcessInstance processInstance = getProcessInstance(processInstanceId);

		// 获取当前活动id
		String activityId = processInstance.getActivityId();

		// 获取流程定义id
		String processDefinitionId = processInstance.getProcessDefinitionId();

		// 获取流程定义实体（对应.bpmn文件）
		ProcessDefinitionEntity processDefinitionEntity = getProcessDefinitionEntity(processDefinitionId);

		// 获取当前活动对象
		ActivityImpl activity = processDefinitionEntity.findActivity(activityId);

		return activity;
	}

	/**
	 * 根据任务id获取当前活动对象
	 */
	@Override
	public ActivityImpl getActivityByTaskId(String taskId) {
		// 通过任务id获取任务对象
		Task task = getTask(taskId);

		// 通过任务对象获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();

		// 通过任务对象获取流程实例id
		String processInstanceId = task.getProcessInstanceId();

		// 通过流程实例id获取流程实例对象
		ProcessInstance processInstance = getProcessInstance(processInstanceId);

		// 通过流程实例对象获取当前活动id
		String activityId = processInstance.getActivityId();

		// 通过流程定义id获取流程定义实体（对应.bpmn文件）
		ProcessDefinitionEntity processDefinitionEntity = getProcessDefinitionEntity(processDefinitionId);

		// 通过当前活动id获取当前活动对象
		ActivityImpl activity = processDefinitionEntity.findActivity(activityId);

		return activity;
	}
}
