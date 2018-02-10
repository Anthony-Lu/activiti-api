<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ include file="/js/commons.jspf" %>
<%@taglib uri="/struts-tags" prefix="s"%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>

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
		                <td width="94%" valign="bottom">
		                	<span class="STYLE1">
		                			新增/修改请假申请
		                	</span>
		                	</td>
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
		    	<div align="left" class="STYLE21">
		    		<!-- <s:hidden name="id"></s:hidden>
		    		<s:hidden name="user.id"></s:hidden>
			 		请假天数:<s:textfield name="days" cssStyle="width: 200px;"/><br/>
			 		请假原因:<s:textfield name="content" cssStyle="width: 800px;"/><br/>
			 		备&emsp;&emsp;注:<s:textarea name="remark" cols="50" rows="5"/><br/> -->
			 		
			 		请假天数：<input type="text" id="leave_days" style="width: 200px;"/><br/>
			 		请假原因：<input type="text" id="leave_reason" style="width: 200px;"><br/>
			 		备&emsp;&emsp;注：<textarea rows="5" cols="50" id="leave_remark"></textarea><br/>
			 		<input type="button" value="提交" class="button_ok" id="saveBtn"/>
				</div>
		    </td>
		  </tr>
	</table>
	 	
	<script type="text/javascript">
			$("#saveBtn").click(function(){
				$.ajax({
					url:"/leaveBill/saveBill",
					data:{
						
						"days":$("#leave_days").val(),
						"content":$("#leave_reason").val(),
						"remark":document.getElementById("leave_remark").value
					},
					type:"post",
					dataType:"json",
					success:function(data) {
						if(data.success) {
							location.href = "/leaveBill/toHome";
						}else{
							alert("error");
						}
						
					}
				})
				
			})
	</script>
</body>
</html>