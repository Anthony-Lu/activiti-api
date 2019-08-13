package com.fairy.activiti.service;

import java.util.List;

import com.fairy.activiti.entity.LeaveBill;

public interface LeaveBillService {
	List<LeaveBill> findLeaveBillList();
	void saveLeaveBill(LeaveBill leaveBill);
	LeaveBill findLeaveBillById(String id);
	void deleteLeaveBillById(String id);
}
