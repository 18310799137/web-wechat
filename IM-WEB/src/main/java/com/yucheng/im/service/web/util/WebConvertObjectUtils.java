package com.yucheng.im.service.web.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebConvertObjectUtils {

	/**
	 * 
	* @Title: convertJsonStrToObject 
	* @Description: 转换json字符串成 实体类对象
	* @param @param jsonStr
	* @param @return
	* @return Object
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T>T convertJsonStrToObject(String jsonStr,Class<T>clazz) {
		return (T)JSONObject.toBean(JSONObject.fromObject(jsonStr),clazz);
	}
	/**
	 * 
	* @Title: convertObjectToJsonStr 
	* @Description: 转换实体类对象成Json字符串
	* @param @param objEntity
	* @param @return
	* @return String
	* @throws
	 */
	public static String convertObjectToJsonStr(Object objEntity) {
		return JSONObject.fromObject(objEntity).toString();
	}
	
	public static String convertObjectArrayToJsonStr(Object arrayEntity) {
		return JSONArray.fromObject(arrayEntity).toString();
	}
	
	
	
}
