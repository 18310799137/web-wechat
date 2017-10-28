package com.yucheng.im.service.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class EncryptString {
	/**
	 * // 编码
	 * 
	 * @param inputString
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public static String decodeString(String inputString)
			throws UnsupportedEncodingException {

		String decodeString = new String(Base64.decodeBase64(inputString
				.getBytes("utf-8")), "utf-8");
		System.out.println(decodeString);
		return decodeString;
	}

	/***
	 * // 解码
	 * 
	 * @param inputString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeString(String inputString)
			throws UnsupportedEncodingException {

		String encodeString = new String(Base64.encodeBase64(inputString
				.getBytes("utf-8")), "utf-8");
		System.out.println(encodeString);
		return encodeString;
	}

}
