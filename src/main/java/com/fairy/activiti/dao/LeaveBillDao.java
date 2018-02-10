package com.fairy.activiti.dao;

import java.util.List;

import com.fairy.activiti.entity.LeaveBill;

public interface LeaveBillDao {

	List<LeaveBill> findLeaveBillList();

	void saveLeaveBill(LeaveBill leaveBill);

	LeaveBill findLeaveBillById(String id);

	void updateLeaveBill(LeaveBill leaveBill);

	void deleteLeaveBillById(String id);
}
