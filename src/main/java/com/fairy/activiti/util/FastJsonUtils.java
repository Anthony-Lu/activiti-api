package com.fairy.activiti.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FastJsonUtils {

	private FastJsonUtils() {

	}

	/**
	 * 将对象序列化为字符串；
	 * 
	 * @param data
	 * @return
	 */
	public static String serializeToJSON(Object data) {
		if (data instanceof JSONObject) {
			return ((JSONObject) data).toJSONString();
		}
		return JSON.toJSONString(data);
	}

	/**
	 * 
	 * @param json
	 * @param dataClazz
	 * @return
	 */
	public static <T> T deserializeFromJSON(String json, Class<T> dataClazz) {
		return JSON.parseObject(json, dataClazz);
	}

	public static <T> List<T> deserializeListFromJSON(String json, Class<T> dataClazz) {
		return JSON.parseArray(json, dataClazz);
	}
}
