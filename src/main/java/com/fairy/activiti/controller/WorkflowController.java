package com.fairy.activiti.controller;

import com.fairy.activiti.bean.WorkflowBean;
import com.fairy.activiti.entity.LeaveBill;
import com.fairy.activiti.entity.User;
import com.fairy.activiti.service.LeaveBillService;
import com.fairy.activiti.service.WorkflowService;
import com.fairy.activiti.util.FastJsonUtils;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workflow")
public class WorkflowController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private LeaveBillService leaveBillService;

    /**
     * @return
     */
    @RequestMapping("/toIndex")
    public String index() {
        return "workflow/NewFile";
    }

    /**
     * 流程上传页面
     *
     * @return
     */
    @RequestMapping("/toDeployProcess")
    public String toDeployProcess() {
        return "workflow/NewFile";
    }

    /**
     * 部署管理首页
     *
     * @return
     */
    @RequestMapping("/toDeployHome")
    @ResponseBody
    public String toDeployHome() {
        Map<String, Object> map = new HashMap<>();

        List<Deployment> deplotmentList = workflowService.findDeploymentList();
        List<ProcessDefinition> processDefinitionList = workflowService.findProcessDefinitionList();

        map.put("processDefinitionList", processDefinitionList);
        map.put("deplotmentList", deplotmentList);

        return FastJsonUtils.serializeToJSON(map);
    }

    /**
     * 部署流程
     * zip格式的文件，格式类型是File
     *
     * @param myProcessFile
     * @return
     */
    @ResponseBody
    @RequestMapping("/deploy")
    public String deploy(MultipartFile myProcessFile) {
        logger.info("开始部署名为{}的流程定义", myProcessFile.getName());

        CommonsMultipartFile cf = (CommonsMultipartFile) myProcessFile;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        //手动创建临时文件  
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
                file.getName());
        //System.out.println(tmpFile.toString());
        try {
            myProcessFile.transferTo(tmpFile);
            workflowService.saveNewDeploye(tmpFile, myProcessFile.getName());
        } catch (Exception e) {
            logger.error("流程部署出错", e);
        }
        logger.info("部署完成");
        //return "wordflow/workflow";
        return "success";
    }

    /**
     * 删除部署信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteDeployment")
    public String deleteDeployment(String id) {
        workflowService.deleteProcessDefinitionByDeploymentId(id);
        return "wordflow/workflow";
    }

    /**
     * 查看流程图
     *
     * @param deploymentId
     * @param imgName
     * @param request
     * @param resp
     * @return
     */
    @RequestMapping("/viewImg")
    public String viewImg(String deploymentId, String imgName, HttpServletRequest request, HttpServletResponse resp) {
        //InputStream inputStream = workflowService.findImageInputStream(deploymentId, imgName);
        //OutputStream outputStream = null ;
        int len = 0;
        byte[] b = new byte[1024];
        try (InputStream inputStream = workflowService.findImageInputStream(deploymentId, imgName);
             OutputStream outputStream = resp.getOutputStream()) {
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
        } catch (IOException e) {
            logger.error("读取流程图片异常", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 启动流程
     *
     * @param workflowBean
     * @return
     */
    @RequestMapping("/startProcess")
    public String startProcess(WorkflowBean workflowBean) {
        workflowService.saveStartProcess(workflowBean);
        return "workflow/workflow";
    }

    /**
     * 查看当前登录人的任务列表
     *
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTaskList")
    public String getTaskList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Task> list = workflowService.findTaskListByName(user.getName());
        //用fastjson序列化List<Task>会报错，因此将List<Task>中的内容复制到List<Map>中，再进行序列化
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (Task task : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("assignee", task.getAssignee());
            map.put("createTime", task.getCreateTime());
            arrayList.add(map);
        }
        return FastJsonUtils.serializeToJSON(arrayList);
    }

    /**
     * 获取任务表单
     *
     * @param taskId
     * @return
     */
    @ResponseBody
    @RequestMapping("/viewTaskForm")
    public String viewTaskForm(String taskId) {
        //获取任务节点的表单key，从而决定要跳转的表单页面
        String url = workflowService.findTaskFormKeyByTaskId(taskId);
        url += "?taskId=" + taskId;
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return FastJsonUtils.serializeToJSON(map);
    }

    /**
     * 审批页面
     * 1、请假单的信息（请假原因，请假天数，请假人等，回显至表单）
     * 2、当前任务完成后的流程连线名称
     * 3、当前任务的历史审批意见
     *
     * @param taskId
     * @return
     */
    @RequestMapping("/audit")
    public ModelAndView audit(String taskId) {
        //根据任务id，获取请假单id，从而获取请假单信息
        LeaveBill leaveBill = workflowService.findLeaveBillByTaskId(taskId);

        ModelAndView mm = new ModelAndView();

        //根据任务id获取流程定义实体对象，从而获取当前任务完成之后的连线名称
        List<String> outcomeList = workflowService.findOutComeListByTaskId(taskId);

        //根据任务id查询所有历史审批信息，帮助当前审批人完成审批
        List<Comment> commentList = workflowService.findCommentByTaskId(taskId);

        mm.setViewName("workflow/taskForm");
        mm.addObject("taskId", taskId);
        mm.addObject("leaveBill", leaveBill);
        mm.addObject("outcomeList", outcomeList);
        mm.addObject("commentList", commentList);

        return mm;
    }

    /**
     * 提交任务
     *
     * @param bean
     * @return
     */
    @RequestMapping("/submitTask")
    public String submitTask(WorkflowBean bean) {
        workflowService.saveSubmitTask(bean);
        return "workflow/task";
    }

    /**
     * 查看流程定义的流程图（以图片的形式显示出来）
     *
     * @param leaveBillId
     * @param response
     * @return
     */
    @RequestMapping("/showImg")
    public String showImg(String leaveBillId, HttpServletResponse response) {

        InputStream inputStream = workflowService.findImageInputStream(leaveBillId);
        OutputStream outputStream = null;

        byte[] b = new byte[1024];
        int len = -1;
        try {
            outputStream = response.getOutputStream();
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
        } catch (IOException e) {
            logger.error("读取流程图片出错", e);
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                logger.error("读取流程图片出错", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取流程图当前活动节点的坐标
     *
     * @param taskId 当前任务的主键id
     * @return
     */
    @RequestMapping("/viewCurrentImage")
    public ModelAndView viewCurrentImage(String taskId) {
        ModelAndView mm = new ModelAndView();
        ProcessDefinition processDefinition = workflowService.findProcessDefinitionByTaskId(taskId);
        //获取当前活动节点的坐标
        Map<String, Integer> CoordinateMap = workflowService.findCoordinateByTask(taskId);

        mm.setViewName("workflow/image");
        mm.addObject("deploymentId", processDefinition.getDeploymentId());
        mm.addObject("imageName", processDefinition.getDiagramResourceName());
        mm.addObject("coordinate", CoordinateMap);

        return mm;
    }

    @RequestMapping("/toImg")
    public String toImg() {
        return "workflow/image";
    }

    /**
     * 查询当前任务的历史审批信息
     *
     * @param leaveBillId 请假单据的主键id
     * @return
     */
    @ResponseBody
    @RequestMapping("/viewHisComment")
    public String viewHisComment(String leaveBillId) {
        HashMap<Object, Object> map = new HashMap<>();
        List<Comment> commentList = workflowService.findCommentByLeaveBillId(leaveBillId);
        LeaveBill leaveBill = leaveBillService.findLeaveBillById(leaveBillId);
        map.put("commentList", commentList);
        map.put("leaveBill", leaveBill);
        return FastJsonUtils.serializeToJSON(map);
    }

    /**
     * 任务管理首页
     *
     * @return
     */
    @RequestMapping("/toTask")
    public String toTask() {
        return "workflow/task";
    }

    /**
     * 通过请假单id查看流程图节点
     *
     * @param id
     * @return
     */
    @RequestMapping("/getCurrentTaskByLeaveId")
    public ModelAndView getCurrentTaskByLeaveId(String id) {
        id = "4";
        ModelAndView model = new ModelAndView();
        Map<String, Integer> map = workflowService.findCoordinateByLeaveId(id);
        ProcessDefinition processDefinition = workflowService.findProcessDefinitionByLeaveId(id);
        model.setViewName("workflow/image");
        model.addObject("deploymentId", processDefinition.getDeploymentId());
        model.addObject("imageName", processDefinition.getDiagramResourceName());
        model.addObject("coordinate", map);

        return model;
    }

}
