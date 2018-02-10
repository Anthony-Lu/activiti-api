<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ include file="/js/commons.jspf" %>
<%@taglib uri="/struts-tags" prefix="s"%> --%>
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请假管理</title>
</head>
<body>
 	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="30"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td height="24" bgcolor="#353c44"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="6%" height="19" valign="bottom"><div align="center"><img src="${pageContext.request.contextPath }/images/tb.gif" width="14" height="14" /></div></td>
		                <td width="94%" valign="bottom"><span class="STYLE1">请假申请列表列表</span></td>
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
		        <td height="20" bgcolor="#FFFFFF" class="STYLE10" colspan="8"><div align="left">
					<a id="apply_leave" href = "${pageContext.request.contextPath}/leaveBill/toInput">添加请假申请</a>
				</div></td>
		  </tr> 
		  <tr>
		    <td>
		    <table>
		<thead >
			<tr>
				<th>ID</th>
				<th>请假人</th>
				<th>请假天数</th>
				<th>请假事由</th>
				<th>请假备注</th>
				<th>请假时间</th>
				<th>请假状态</th>
				<th>操作</th>
		</thead>
		<tbody id="myTbody"></tbody>
	
	</table>
		    <%-- <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#a8c7ce">
		      <tr>
		        <td width="5%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">ID</span></div></td>
		        <td width="10%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假人</span></div></td>
		        <td width="5%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假天数</span></div></td>
		        <td width="15%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假事由</span></div></td>
		        <td width="20%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假备注</span></div></td>
		        <td width="15%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假时间</span></div></td>
		        <td width="5%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">请假状态</span></div></td>
		        <td width="25%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">操作</span></div></td>
		      </tr>
		      		<tr>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE6" id="leave_id"><div align="center">1</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">范冰冰</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">8</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">回家过年</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">回家过年，望批准</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">2018/2/13</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19">
				        	<div align="center">请假人请假天数请假事由请假备注请假时间请假状态操作
				        		审核中...
				        	</div>
			            </td>
				        <td height="20" bgcolor="#FFFFFF"><div align="center" class="STYLE21">
				        	<s:if test="state==0">
			        			<a href="${pageContext.request.contextPath }/leaveBillAction_input.action?id=<s:property value="id"/>" >修改</a>
								<a href="leaveBillAction_delete.action?id=<s:property value="id"/>" >删除</a>
								<a href="${pageContext.request.contextPath }/workflowAction_startProcess.action?id=<s:property value="id"/>" >申请请假</a>
			        		</s:if>
			 				<s:elseif test="state==1">
			 					<a href="${pageContext.request.contextPath }/workflowAction_viewHisComment.action?id=<s:property value="id"/>" >查看审核记录</a>
			 				</s:elseif>
			 				<s:else>
			 					<a href="leaveBillAction_delete.action?id=<s:property value="id"/>" >删除</a>
			 					<a href="${pageContext.request.contextPath }/workflowAction_viewHisComment.action?id=<s:property value="id"/>" >查看审核记录</a>
			 				</s:else>
						</div></td>
				    </tr> 
		    </table> --%>
		    
		    </td>
		  </tr>
	</table>
	
	
	<script type="text/javascript">
	/* 
		var str = '<a href="${pageContext.request.contextPath }/leaveBillAction_input.action?id=" >修改</a>';
			str +='<a href="leaveBillAction_delete.action?id=" >删除</a>';
			str +='<a href="${pageContext.request.contextPath }/workflowAction_startProcess.action?id=" >申请请假</a>'; */
	var str = '<a href="${pageContext.request.contextPath }/workflowAction_startProcess.action?id=" >申请请假</a>';
	function getLeaveBillList() {
		$.ajax({
			url:"/leaveBill/getLeaveBillList",
			data:{},
			type:"post",
			dataType:"json",
			success:function(data) {
				
				for(var i = 0;i <data.length;i++) {
					
				     var state = data[i].state;
				     if(state == 0) {
				    	 state = "初始录入";
				     }else if (state == 1) {
				    	 state = "审批中";
				    	 str = '<a href="${pageContext.request.contextPath }/workflowAction_viewHisComment.action?id="/>查看审核记录</a>';
				     }else{
				    	 state = "审批完成";
				    	 str = '<a href="${pageContext.request.contextPath }/workflowAction_viewHisComment.action?id="/>查看审核记录</a>';
				     }
					 var $trTemp = $("<tr></tr>");
					 $trTemp.append("<td>"+ data[i].id +"</td>");
					 $trTemp.append("<td>"+ data[i].user.name +"</td>");
					 $trTemp.append("<td>"+ data[i].days+"</td>");
					 $trTemp.append("<td>"+ data[i].content +"</td>");
					 $trTemp.append("<td>"+ data[i].remark +"</td>");
					 $trTemp.append("<td>"+ data[i].leaveDate +"</td>");
					 $trTemp.append("<td>"+ state +"</td>");
					 $trTemp.append("<td>"+ str +"</td>");
					 $trTemp.appendTo("#myTbody");
				}
				
			}
		})
	}		
	getLeaveBillList();
	</script>
</body>
</html>