<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- <%@ include file="/js/commons.jspf" %>
<%@taglib uri="/struts-tags" prefix="s"%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务管理</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
</head>
<body>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		 	 <input type="hidden" value="" id="taskId"/>
		    <td height="30"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td height="24" bgcolor="#353c44"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="6%" height="19" valign="bottom"><div align="center"><img src="${pageContext.request.contextPath }/images/tb.gif" width="14" height="14" /></div></td>
		                <td width="94%" valign="bottom"><span class="STYLE1">个人任务管理列表</span></td>
		              </tr>
		            </table></td>
		            <td><div align="right"><span class="STYLE1">
		              </span></div></td>
		          </tr>
		        </table></td>
		      </tr>
		    </table></td>
		  </tr>
		  <tr>
		    <td>
		    
		    
		   <%--  <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#a8c7ce">
		      <tr>
		        <td width="15%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">任务ID</span></div></td>
		        <td width="25%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">任务名称</span></div></td>
		        <td width="20%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">创建时间</span></div></td>
		        <td width="20%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">办理人</span></div></td>
		        <td width="20%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">操作</span></div></td>
		      </tr>
		      		<tr>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE6"><div align="center"><s:property value="id"/></div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center"><s:property value="name"/></div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center"><s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"/></div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center"><s:property value="assignee"/></div></td>
				        <td height="20" bgcolor="#FFFFFF"><div align="center" class="STYLE21">
				        	<a href="${pageContext.request.contextPath }/workflowAction_viewTaskForm.action?taskId=<s:property value="id"/>">办理任务</a>
							<a target="_blank" href="workflowAction_viewCurrentImage.action?taskId=<s:property value="id"/>">查看当前流程图</a>
				        </div></td>
				    </tr> 
		    </table> --%>
		    
		    <table>
		    	<thead>
		    		<tr>
		    			<th>任务ID</th>
		    			<th>任务名称</th>
		    			<th>创建时间</th>
		    			<th>办理人</th>
		    			<th>操作</th>
		    		</tr>
		    	</thead>
		    	
		    	<tbody id="myTbody"></tbody>
		    </table>
		    </td>
		  </tr>
	</table>
	
	
	<script type="text/javascript">
		var str = '<a href="#" id="handlerTask" onclick="handlerTask()">办理任务</a>';
			str += '&nbsp;&nbsp;&nbsp;&nbsp;' + '<a target="_blank" href="#" id="showImg" onclick="viewCurrentImg()">查看当前流程图</a>';
		function getTaskList() {
			$.ajax({
				url:"/workflow/getTaskList",
				data:{},
				type:"post",
				dataType:"json",
				success:function(data) {
					for(var i = 0;i <data.length;i++) {
						 var $trTemp = $("<tr></tr>");
						 var createTime = new Date(parseInt(data[i].createTime)).toLocaleString();
						 $trTemp.append("<td>"+ data[i].id +"</td>");
						 $trTemp.append("<td>"+ data[i].name +"</td>");
						 $trTemp.append("<td>"+ createTime+"</td>");
						 $trTemp.append("<td>"+ data[i].assignee +"</td>");
						 $trTemp.append("<td>"+ str +"</td>");
						 $trTemp.appendTo("#myTbody");
					}
				}
				
			})
		}
		
		getTaskList();

		//办理任务
		function handlerTask(){
			location.href = "/workflow/audit?taskId=" + $("#myTbody tr td").eq(0).text();
		}
		
		//查看流程图
		function viewCurrentImg() {
			location.href = "/workflow/viewCurrentImage?taskId=" + $("#myTbody tr td").eq(0).text();
			//location.href = "/workflow/getCurrentTaskByLeaveId";
		}
		
	</script>
</body>
</html>