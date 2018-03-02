package com.fairy.activiti;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fairy.activiti.constant.LeaveBillConstant;
import com.fairy.activiti.dao.LeaveBillDao;
import com.fairy.activiti.entity.LeaveBill;
import com.fairy.activiti.service.WorkflowService;
import com.fairy.activiti.util.FastJsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest{
	//ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	/*@Test
	public void contextLoads() {
	}
*/
	/*@Test
	public void setVariablesTest() {
		
	}*/
	
	 /** 
     * 部署流程定义 	 类路径下（classPath）
     */  
    @Test  
    public void deploy(){  
        // 获取部署对象  
        Deployment deployment = repositoryService.createDeployment().name("员工请假流程222").addClasspathResource("processes/MyProcess.bpmn").addClasspathResource("processes/MyProcess.png").deploy();
        System.out.println("流程部署ID:"+deployment.getId());  
        System.out.println("流程部署Name:"+deployment.getName());
    }  
    
    
    /**
     * 部署流程定义	zip
     */
    /*@Test
    public void deployByZip(MultipartFile file) {
    	InputStream in = null;
		try {
			in = file.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ZipInputStream zipInputStream = new ZipInputStream(in);
		repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
    }*/
    
    @Test
    public void deployByZip(){
    	InputStream in = this.getClass().getClassLoader().getResourceAsStream("MyProcess.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deploy = repositoryService.createDeployment().name("zip格式部署流程定义").addZipInputStream(zipInputStream).deploy();
		System.out.println("部署id为：" + deploy.getId() + ",部署名称为：" + deploy.getName());
		
    }
    
	/**
	 * 启动流程实例并设置流程变量
	 */
	@Test
	public void startProcessTest(){
		//RuntimeService runtimeService = engine.getRuntimeService();
		String userId = "wangwu";
		//设置认证用户的id
		identityService.setAuthenticatedUserId(userId);
		HashMap<String, Object> variables = new HashMap<>();
		variables.put("applyUser", "luxuebing");
		variables.put("PM", "PM");
		variables.put("President", "President");
		variables.put("VP", "VP");
		variables.put("days",5);
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("myProcess","66666",variables);
		System.out.println("流程实例id:>>>" + instance.getId());
	}
	
	
	/**
	 * 查询我要办理的任务
	 */
	/*@Test
	public void getMyTask() {
		//String assignee = "zhangsan";
		//String assignee = "PM";
		String assignee = "President";
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
		for (Task task : list) {
			System.out.println(task.toString() + "当前任务的办理人为：>>>" + taskService.getVariable(task.getId(), "President"));
		}
	}*/
	
	/**
	 * 办理任务并且加上批注
	 */
	/*@Test
	public void completeTask() {
		//String taskId = "5016";
		String taskId = "10005";
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String message = "总裁同意";
		//给当前任务添加批注信息
		taskService.addComment(taskId, processInstanceId, message);
		Map<String, Object> variables = new HashMap<>();
		variables.put("msg", "agree");
		taskService.complete(taskId, variables);
	}*/
	
	/**
	 * 根据当前任务id查看当前流程图所处的活动节点信息
	 */
	/*@Test
	public void findCoordinate() {
		//String taskId = "13";
		Task result = taskService.createTaskQuery().taskAssignee("President").singleResult();
		//通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(result.getId()).singleResult();
		//通过任务id获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		//通过流程定义id获取流程定义的实体对象（对应的是.bpmn文件中的数据）
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//通过任务id获取流程实例id
		String processInstanceId = task.getProcessInstanceId();
		//通过流程实例id获取当前活动对应的流程实例对象
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		//获取当前的活动id
		String activityId = instance.getActivityId();
		//获取当前活动对象
		ActivityImpl activity = entity.findActivity(activityId);
		System.out.println("x坐标为：" + activity.getX() + "-----y坐标为：" + activity.getY() + "-----高度为：" + activity.getHeight() + "-----宽度为：" + activity.getWidth());
	}*/
	
	/**
	 * 根据流程实例id查看当前流程实例是否结束
	 */
	/*@Test
	public void findProcessState() {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId("5008").singleResult();
		if(instance == null ) {
			System.out.println("当前的流程实例已经结束。。。。");
		}
		HistoricProcessInstance singleResult = historyService.createHistoricProcessInstanceQuery().processInstanceId("5008").singleResult();
		System.out.println("历史流程实例id为：" + singleResult.getId() + "----开始时间为：" + singleResult.getStartTime() + "-----------结束时间为：" + singleResult.getEndTime());
	}*/
	
	/**
	 * 根据用户id查询该用户发起的流程
	 * @param userId
	 * @return
	 */
	@Test
	public void getHistoricProcessInstance() {
		String userId = "lisi";
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().startedBy(userId).list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println(historicProcessInstance.toString());
		}
	}
	
	/**
	 * 查询指定用户发起的流程（流程历史  用户发起）
	 * historyService.createHistoricProcessInstanceQuery()
	 *  .finished()//finished--> 完成的流程；  unfinish ---> 还在运行中的流程
	 *	.startedBy(name).orderByProcessInstanceStartTime().desc()
	 *  .listPage(firstResult, maxResults);
	 */
	@Test
	public void findStartByMe() {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().startedBy("范冰冰").list();
		System.out.println(list);
	}
	/**
	 * 查询指定用户参与的流程信息 （流程历史  用户参与 ）
	 * List hpis = historyService
	 *	.createHistoricProcessInstanceQuery().involvedUser(name)
	 *	.orderByProcessInstanceStartTime().desc().listPage(firstResult, maxResults);
	 */
	
	/**
	 * 查询指定流程的任务流转路径 （流程历史 任务 流转 路经）
	 * historyService.createHistoricTaskInstanceQuery()
	 *	.processInstanceId(processInstanceId)
	 *	.orderByHistoricTaskInstanceEndTime().asc().list();
	 * 
	 */
	
	/**
	 * 查询我审批过的流程实例
	 */
	@Test
	public void finishedByUser() {
		String assignee = "范冰冰";
		
		List<HistoricProcessInstance> list2 = new ArrayList<>();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee).list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			String processInstanceId = historicTaskInstance.getProcessInstanceId();
			list2.add( historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult());
		}
		for (HistoricProcessInstance historicProcessInstance : list2) {
			System.out.println(historicProcessInstance);
		}
		
	}
	
	
	/**
	 * 查看当前流程图的所在节点(单线流程，一个执行对象的时候)
	 */
	public void getCurrentView() {
		//repositoryService.createProcessDefinitionQuery().processDefinitionKey("")
		ActivityImpl activity = null;  
		String processDefinitionId = "";//流程定义id
		String processInstanceId = "";//流程实例id
		ExecutionEntity entity = (ExecutionEntity) runtimeService.createExecutionQuery().processInstanceId(processInstanceId ).singleResult();
		String activityId = entity.getActivityId();
		//String currentActivityId = entity.getCurrentActivityId();
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId );
		List<ActivityImpl> activities = definitionEntity.getActivities();
		for (ActivityImpl activityImpl : activities) {
			if(activityId.equals(activityImpl.getId())) {
				activity = activityImpl;
				break;
			}
		}
		
		System.out.println(activity);
		//return activity;
	}
	
	@Autowired
	WorkflowService workflowService;
	@Autowired
	LeaveBillDao leaveBillDao;
	@Test
	public void startTest() {
		
		//String id = workflowBean.getId();
		String id = "4";
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(id);
		/*//获取请假单实体的类名
		String key = leaveBill.getClass().getSimpleName();*/
		//更新请假单状态从0变成1 表示审核中
		leaveBill.setState(LeaveBillConstant.AUDIT_ING);
		leaveBillDao.updateLeaveBill(leaveBill);
		HashMap<String, Object> variables = new HashMap<>();
		//从session中取出当前登录人，设置为提交申请任务的办理人
		identityService.setAuthenticatedUserId("范冰冰");
		
		variables.put("applyUser", "范冰冰");
		//设置请假天数
		Integer days = leaveBill.getDays();
		variables.put("days", days);
		//将启动的流程实例关联业务
		String businessKey = id;
		runtimeService.startProcessInstanceByKey("myProcess",businessKey,variables);
	}
	
	/**
	 * 查询我的待办任务列表
	 */
	@Test
	public void taskTest(){
		
		List<Task> list = taskService.createTaskQuery().taskAssignee("范冰冰").list();
		String json = FastJsonUtil.serializeToJSON(list);
		System.out.println(json);
	}
	
	/**
	 * 根据请假单id获取历史审批意见
	 */
	@Test
	public void findCommentListByLeaveId() {
		String id = "3";
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(id)
				.singleResult();
		String processInstanceId = hpi.getId();
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		System.out.println(comments);
		
	}
}
