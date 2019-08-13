package com.fairy.activiti.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fairy.activiti.entity.LeaveBill;
import com.fairy.activiti.service.LeaveBillService;
import com.fairy.activiti.util.FastJsonUtils;

/**
 * 请假单处理
 * @author luxuebing
 * @date 2018/02/05下午2:53:01
 */
@Controller
@RequestMapping("/leaveBill")
public class LeaveBillController {

	public static Logger logger = LoggerFactory.getLogger(LeaveBillController.class);
	@Autowired
	private LeaveBillService service;

	/**
	 * 请假管理首页展示
	 * 
	 * @return
	 */
	@RequestMapping("/toHome")
	public String toHome() {
		return "leaveBill/list";
	}

	@ResponseBody
	@RequestMapping("/getLeaveBillList")
	public String getLeaveBillList() {
		List<LeaveBill> list = service.findLeaveBillList();
		return FastJsonUtils.serializeToJSON(list);
	}
	
	/**
	 * 添加请假申请页面
	 * 
	 * @return
	 */
	@RequestMapping("/toInput")
	public String toInput() {
		return "leaveBill/input";
	}

	/**
	 * 保存请假申请
	 * 
	 * @param bill
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveBill")
	public String saveBill(LeaveBill bill) {
		Map<String, Object> map = Maps.newHashMap();
		try {
			service.saveLeaveBill(bill);
			map.put("success", true);
		} catch (Exception e) {
			logger.error("保存请假申请异常", e);
		}
		return FastJsonUtils.serializeToJSON(map);
	}

	/**
	 * 删除请假申请
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteBill")
	public String deleteBill(String id) {
		Map<String, Object> map = Maps.newHashMap();
		try {
			service.deleteLeaveBillById(id);
			map.put("success", true);
		} catch (Exception e) {
			logger.error("删除请假申请异常" , e);
		}
		return FastJsonUtils.serializeToJSON(map);
	}
}
