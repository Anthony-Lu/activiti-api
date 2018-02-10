package com.fairy.activiti.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求单据实体 
 * 对应表t_leave
 * @author luxuebing
 * @date 2018/02/05上午11:04:41
 */
public class LeaveBill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4305694496828558814L;

	private String id;
	private Date leaveDate;
	private Integer days;
	private String content;
	private Integer state;
	private String remark;
	private Integer userId;
	private User user;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
