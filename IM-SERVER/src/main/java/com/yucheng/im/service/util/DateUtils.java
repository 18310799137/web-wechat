package com.yucheng.im.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * 
	* @Title: getDateStr 
	* @Description: 获取日期格式 YYYY-MM-dd HH:mm:ss
	* @param @return
	* @return String
	* @throws
	 */
	public static String getDateStr() {
		return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
	}
}
