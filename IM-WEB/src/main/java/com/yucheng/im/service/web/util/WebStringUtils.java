package com.yucheng.im.service.web.util;

public class WebStringUtils {

	/**
	 * 
	 * @Description:检验字符串是否为空
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月29日下午2:01:44
	 * @version V1.0
	 * @param params
	 * @return
	 */
	public static boolean checkStringIsNull(String ...params) {
		if(null==params) {
			return false;
		}
		for (String string : params) {
			if(null==string||"".equals(string.trim())) {
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
		checkStringIsNull("test","test2");
	}
}
