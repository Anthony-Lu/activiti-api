package com.fairy.activiti.service.impl;

import com.fairy.activiti.bean.WorkflowBean;
import com.fairy.activiti.constant.LeaveBillConstant;
import com.fairy.activiti.dao.LeaveBillDao;
import com.fairy.activiti.entity.LeaveBill;
import com.fairy.activiti.service.WorkflowService;
import com.fairy.activiti.util.SessionUtils;
import com.fairy.activiti.util.StringUtils;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 
 * @author luxuebing
 * @date 2018/02/10下午5:04:26
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {
	private static final String PROCESS_DEFINITON_KEY = "myProcess";
	
	private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private LeaveBillDao leaveBillDao;
	@Autowired
	private IdentityService identityService;

	
	/**
	 * 部署流程定义 （zip格式）
	 */
	@Override
	public void saveNewDeploye(File file, String filename) {
		try {
			// 将File类型的文件转换成流
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
			repositoryService.createDeployment().name(filename).addZipInputStream(zipInputStream).deploy();
		} catch (FileNotFoundException e) {
			/*e.printStackTrace();
			logger.error("部署流程定义出错>>>>>" , e);*/
			throw new RuntimeException("部署流程定义出错", e);
		}
	}

	/**
	 * 查询部署对象信息 对应表act_re_deployment
	 */
	@Override
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().asc().list();
		return list;
	}

	/**
	 * 查询流程定义信息 对应表act_re_prodef
	 */
	@Override
	public List<ProcessDefinition> findProcessDefinitionList() {
		// 根据版本号升序排列
		List<ProcessDefinition> list = repositoryService
				.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion()
				.asc()
				.list();
		return list;
	}

	/**
	 * 使用部署对象id和资源图片名称，获取流程图的输入流
	 */
	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}

	/**
	 * 根据部署对象id删除流程定义
	 */
	@Override
	public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
		// 最后一个参数为true时表示级联删除，会删除掉和当前部署的规则相关的信息，包括历史信息；否则为普通删除，如果当前部署的规则还存在正在制作的流程，会抛异常。
		repositoryService.deleteDeployment(deploymentId, true);
	}

	/**
	 * 启动流程实例
	 */
	@Override
	public void saveStartProcess(WorkflowBean workflowBean) {
		String id = workflowBean.getId();
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(id);
		/*
		 * //获取请假单实体的类名 String key = leaveBill.getClass().getSimpleName();
		 */
		// 更新请假单状态从0变成1 表示审核中
		leaveBill.setState(LeaveBillConstant.AUDIT_ING);
		leaveBillDao.updateLeaveBill(leaveBill);
		HashMap<String, Object> variables = new HashMap<>();
		// 从session中取出当前登录人，设置为提交申请任务的办理人
		identityService.setAuthenticatedUserId(SessionUtils.get().getName());

		variables.put("applyUser", SessionUtils.get());
		// 设置请假天数
		Integer days = leaveBill.getDays();
		variables.put("days", days);
		// 将启动的流程实例关联业务
		String businessKey = id;
		runtimeService.startProcessInstanceByKey(PROCESS_DEFINITON_KEY, businessKey, variables);
	}

	/**
	 * 查询用户的待办任务
	 */
	@Override
	public List<Task> findTaskListByName(String name) {
		List<Task> list = taskService.createTaskQuery().taskAssignee(name).orderByTaskCreateTime().asc().list();
		return list;
	}

	/**
	 * 使用任务id获取当前任务节点对应的FormKey中对应的值
	 */
	@Override
	public String findTaskFormKeyByTaskId(String taskId) {
		TaskFormData formData = formService.getTaskFormData(taskId);
		String url = formData.getFormKey();
		return url;
	}

	/**
	 * 使用任务id获取请假单id，从而获取请假单的信息
	 */
	@Override
	public LeaveBill findLeaveBillByTaskId(String taskId) {
		// 通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 通过任务对象获取当前流程实例id
		String processInstanceId = task.getProcessInstanceId();
		// 通过流程实例id获取流程实例对象，从而获取businessKey
		String businessKey = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult().getBusinessKey();
		// 通过id查询请假单实体
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(businessKey);
		return leaveBill;
	}

	/**
	 * 获取当前任务完成之后的连线走向，在前台展示
	 */
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {
		ArrayList<String> outComeList = new ArrayList<>();
		// 通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 通过任务对象获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		// 通过任务对象获取流程实例id
		String processInstanceId = task.getProcessInstanceId();
		// 通过流程实例id获取流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 通过流程定义id获取流程定义实体（对应.bpmn文件）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 通过流程实例获取当前活动id
		String activityId = processInstance.getActivityId();
		// 通过当前活动id获取当前活动对象
		ActivityImpl activity = processDefinitionEntity.findActivity(activityId);
		// 获取当前活动结束后的连线名称
		List<PvmTransition> list = activity.getOutgoingTransitions();
		for (PvmTransition pvmTransition : list) {
			// 获取连线名称
			String name = (String) pvmTransition.getProperty("name");
			if (StringUtils.isNotEmpty(name)) {
				outComeList.add(name);
			} else {
				outComeList.add("默认提交");
			}
		}
		return outComeList;
	}

	/**
	 * 办理完成任务
	 */
	@Override
	public void saveSubmitTask(WorkflowBean workflowBean) {
		// 任务id
		String taskId = workflowBean.getTaskId();
		// 批注信息
		String comment = workflowBean.getComment();
		// 请假单id
		String id = workflowBean.getId();
		// 获取流程实例id
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		// 设置审批人
		Authentication.setAuthenticatedUserId(SessionUtils.get().getName());
		//identityService.setAuthenticatedUserId("");
		// 添加批注信息
		taskService.addComment(taskId, processInstanceId, comment);
		Map<String, Object> variables = new HashMap<>();
		// 完成任务
		variables.put("President", "王中军");
		variables.put("VP", "副总裁");
		variables.put("PM", "范冰冰经纪人");
		String outcome = workflowBean.getOutcome();

		if (outcome != null && !outcome.equals("默认提交")) {
			if (outcome.equals("同意")) {
				outcome = "agree";
			} else {
				outcome = "reject";
			}
			variables.put("msg", outcome);
		}
		taskService.complete(taskId, variables);
		// 指定下一个任务的办理人
		// 判断流程是否结束
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		if (null == pi) {
			// 更新请假单状态
			LeaveBill leaveBill = leaveBillDao.findLeaveBillById(id);
			leaveBill.setState(LeaveBillConstant.AUDIT_COMPLETE);
			leaveBillDao.updateLeaveBill(leaveBill);
		}
	}

	/**
	 * 通过当前任务id获取历史任务的批注信息
	 */
	@Override
	public List<Comment> findCommentByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		return comments;
	}

	/**
	 * 通过请假单id获取历史批注信息
	 */
	@Override
	public List<Comment> findCommentByLeaveBillId(String id) {

		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(id)
				.singleResult();
		String processInstanceId = hpi.getId();
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		return comments;
	}

	/**
	 * 使用任务id获取流程定义对象
	 */
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
				.singleResult();
		return pd;
	}

	/**
	 * 通过当前任务id获取当前活动节点所在的坐标(查看流程进度)
	 */
	@Override
	public Map<String, Integer> findCoordinateByTask(String taskId) {
		HashMap<String, Integer> map = new HashMap<>();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		// 获取流程定义的实体对象（对应.bpmn文件）
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 获取流程实例
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		String activityId = pi.getActivityId();
		// 获取当前活动对象
		ActivityImpl activity = entity.findActivity(activityId);
		// 获取坐标
		map.put("x", activity.getX());
		map.put("y", activity.getY());
		map.put("width", activity.getWidth());
		map.put("height", activity.getHeight());
		return map;
	}

	/**
	 * 根据业务单据的id获取当前流程图
	 */
	@Override
	public InputStream findImageInputStream(String id) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id)
				.singleResult();
		String processDefinitionId = processInstance.getProcessDefinitionId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		InputStream stream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getDiagramResourceName());
		return stream;
	}

	/**
	 * 根据业务单据的id获取当前活动节点(查看流程进度)
	 */
	@Override
	public Map<String, Integer> findCoordinateByLeaveId(String leaveId) {

		ActivityImpl act = null;
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(leaveId)
				.singleResult();
		String processDefinitionId = instance.getProcessDefinitionId();
		// String instanceId = instance.getProcessInstanceId();
		// 获取流程定义实体
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		List<ActivityImpl> activities = processDefinitionEntity.getActivities();

		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.processInstanceBusinessKey(leaveId).singleResult();
		// 获取当前任务执行到哪个活动节点
		String activityId = executionEntity.getActivityId();
		for (ActivityImpl actity : activities) {
			if (activityId.equals(actity.getId())) {
				act = actity;
				break;
			}
		}

		// 获取当前活动节点的坐标
		HashMap<String, Integer> map = new HashMap<>();
		map.put("x", act.getX());
		map.put("y", act.getY());
		map.put("width", act.getWidth());
		map.put("height", act.getHeight());

		return map;
	}

	/**
	 * 通过请假单id获取流程实例
	 */
	@Override
	public ProcessDefinition findProcessDefinitionByLeaveId(String leaveId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(leaveId).singleResult();
		String processDefinitionId = processInstance.getProcessDefinitionId();
		return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
	}
}
