<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %> 
<%-- <%@ include file="/js/commons.jspf" %>
<%@taglib uri="/struts-tags" prefix="s" %> --%>
<html>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请假任务办理</title>
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
		                <td width="94%" valign="bottom"><span class="STYLE1">请假申请的任务办理</span></td>
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
		  		<form action="/workflow/submitTask" method="POST">
			  		<div align="left" class="STYLE21">
			  			<!-- 任务ID -->
			  			<input type="hidden" value="${taskId}" name="taskId">
			  			<!-- 请假单ID -->
			  			<input type="hidden" value="${leaveBill.id}" name="id">
				 		请假天数:<input name="days" disabled="disabled" style="width: 200px;" value="${leaveBill.days}"><br/>
				 		
				 		请假原因:<input name="content" disabled="disabled" style="width: 200px;"value="${leaveBill.content}"><br/>
				 		
				 		请假备注:<textarea rows="2" cols="30" name="remark" disabled="disabled">${leaveBill.remark}</textarea><br/>
				 		
				 		批&emsp;&emsp;注:<textarea rows="5" cols="50" name="comment"></textarea><br/>
				 		
				 		<!-- 使用连线的名称作为按钮 -->
				 		<c:if test="${not empty outcomeList && outcomeList.size() > 0}">
				 			<c:forEach items="${outcomeList}" var="outComeList">
				 				<input type="submit" name="outcome" value="${outComeList}" class="button_ok"/>
				 			</c:forEach>
				 		</c:if>
			 		</div>
			 	</form>
		  	</td>
		  </tr>
	</table>
	<hr>
	<br>
	<c:choose>
	<c:when test="${not empty commentList && commentList.size() > 0}">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
			    <td height="30"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td height="24" bgcolor="#353c44"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td width="6%" height="19" valign="bottom"><div align="center"><img src="${pageContext.request.contextPath }/images/tb.gif" width="14" height="14" /></div></td>
			                <td width="94%" valign="bottom"><span class="STYLE1">显示请假申请的批注信息</span></td>
			              </tr>
			            </table></td>
			            <td><div align="right"><span class="STYLE1">
			              </span></div></td>
			          </tr>
			        </table></td>
			      </tr>
			    </table>
	 
			    </td>
			  </tr>
			  <tr>
			    <td>
			    
			    <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#a8c7ce" >
			      <tr>
			        <td width="15%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">时间</span></div></td>
			        <td width="10%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">批注人</span></div></td>
			        <td width="75%" height="20" bgcolor="d3eaef" class="STYLE6"><div align="center"><span class="STYLE10">批注信息</span></div></td>
			      </tr>
			      <c:forEach items="${commentList}" var="list">
			      	<tr>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE6"><div align="center">${list.time}  </div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">${list.userId}</div></td>
				        <td height="20" bgcolor="#FFFFFF" class="STYLE19"><div align="center">${list.fullMessage}</div></td>
				    </tr> 
			       </c:forEach>
			    </table></td>
			  </tr>
		</table>
	</c:when> 
	<c:otherwise>
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
			    <td height="30"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td height="24" bgcolor="#F7F7F7"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td width="6%" height="19" valign="bottom"><div align="center"><img src="${pageContext.request.contextPath }/images/tb.gif" width="14" height="14" /></div></td>
			                <td width="94%" valign="bottom"><span><b>暂时没有批注信息</b></span></td>
			              </tr>
			            </table></td>
			          </tr>
			        </table></td>
			      </tr>
			    </table></td>
			  </tr>
		</table>
		</c:otherwise>
		</c:choose>
</body>
</html>