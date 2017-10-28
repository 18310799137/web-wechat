package com.yucheng.im.service.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * 
 * @Title: UUIDGenerateUtils.java
 * @Package com.yucheng.im.service.web.util
 * @Description: 获取uuid工具类
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月2日 下午5:33:26
 * @version V1.0
 * 
 */
public class UUIDGenerateUtils {

	private static Logger logger = Logger.getLogger(UUIDGenerateUtils.class);

	private static Lock lock = new ReentrantLock();
	/**
	 * 
	 * @Description: 根据主机ip+时间戳生成 在分布式系统中唯一序号  为每个用户群组生成群组id
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月2日 下午5:33:47
	 * @version V1.0
	 * @return
	 */
	public static String getGroupUUID() {
		lock.lock();
		String ips = null;
		String ipAddr = null;
		long timestamp = 0;
		try {
			ipAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("获取主机IP失败,使用random随机生成uuid", e);
			ipAddr = UUID.randomUUID().toString().replaceAll("\\_|\\-", "");
		}finally {
			lock.unlock();
		}
		ips = ipAddr.replaceAll("\\.", "");
		timestamp = System.currentTimeMillis();
		String uuid = MD5String(ips + timestamp);
		return uuid;
	}

	/**
	 * 
	* @Title: MD5String 
	* @Description: MD5加密
	* @param @param encStr
	* @param @return
	* @return String
	* @throws
	 */
	public final static String MD5String(String encStr) {
		char hexArray[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(encStr.getBytes());
			// 获得密文
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			StringBuilder str = new StringBuilder(32);
			for (int i = 0; i < md.length; i++) {
				// 开始拼接加密算法后的16进制表现形式
				str.append(hexArray[md[i] >> 4 & 0xF]).append(
						hexArray[md[i] & 0xF]);
			}
			return str.toString();
		} catch (Exception e) {
			logger.error("Create MD5 str fail - "+e);
			return null;
		}
	}
	/***
	 * 
	* @Title: getDBUuid 
	* @Description: 生成分布式系统唯一数据库id
	* @param @return
	* @return String
	* @throws
	 */
	public static String getDBUuid() {
		String ipAddr = null;
		try {
			ipAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("获取主机IP失败,使用random随机生成uuid", e);
			ipAddr = String.valueOf(System.currentTimeMillis());
		}
		ipAddr = ipAddr.replaceAll("\\.", "");
		String random = UUID.randomUUID().toString().replaceAll("\\_|\\-", "");
		String uuid = MD5String(ipAddr+random);
		return uuid;
	}
	/***
	 * 
	* @Title: getLocalHostIpAddress 
	* @Description: 获取本机ip网络地址 并且去除小数点
	* @param @return
	* @return String
	* @throws
	 */
	public static String getLocalHostIpAddress() {
		String ips = null;
		String ipAddr = null;
		try {
			ipAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("获取主机IP失败,使用random随机生成uuid", e);
			ipAddr = UUID.randomUUID().toString().replaceAll("\\_|\\-", "");
		}
		ips = ipAddr.replaceAll("\\.", "");
		return ips;
	}
	public static void main(String[] args) {
		System.out.println(getDBUuid());
	}
	/**
	 * 
	* @Title: getLocalHostIpAddressNoReplace 
	* @Description: 获取本机网络ip 地址  ,未去除小数点
	* @param @return
	* @return String
	* @throws
	 */
	public static String getLocalHostIpAddressNoReplace() {
		String ipAddr = null;
		try {
			ipAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("获取主机IP失败,使用random随机生成uuid", e);
			ipAddr = UUID.randomUUID().toString().replaceAll("\\_|\\-", "");
		}
		return ipAddr;
	}

}