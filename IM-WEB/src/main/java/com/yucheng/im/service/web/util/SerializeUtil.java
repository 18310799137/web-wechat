/*package com.yucheng.im.service.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

*//**
 * 
 * @author Administrator
 * 
 * @param <T>
 *//*
public class SerializeUtil {
private static Logger logger = Logger.getLogger(Logger.class);
	*//**
	 * 序列化对象成字节数组
	 * 
	 * @param t
	 * @return
	 *//*
	public static<T> byte[] serialize(T t) {
		ObjectOutputStream objOutput = null;
		ByteArrayOutputStream byteOutput = null;
		try {
			byteOutput = new ByteArrayOutputStream();

			objOutput = new ObjectOutputStream(byteOutput);

			objOutput.writeObject(t);
			return byteOutput.toByteArray();
		} catch (Exception e) {
			logger.error("序列化失败  - Cause:"+e);
			e.printStackTrace();
		}finally {
			if(null!=objOutput) {
				try {
					objOutput.close();
				} catch (IOException e) {
				logger.error("关闭序列化流失败  - Cause:"+e);
				}
			}
			if(null!=byteOutput) {
				try {
					byteOutput.close();
				} catch (IOException e) {
					logger.error("关闭序列化流失败  - Cause:"+e);
				}
			}
		}
		return null;
	}

	*//**
	 * 反序列化成对象
	 * @param <T>
	 * 
	 * @param bytes
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
		ByteArrayInputStream byteOutputStream = null;
		ObjectInputStream objectInputStream =null;
		try {
			byteOutputStream = new ByteArrayInputStream(bytes);
			 objectInputStream = new ObjectInputStream(
					byteOutputStream);
			return   (T) objectInputStream.readObject();
		} catch (Exception e) {
			logger.error("反序列化失败 - Cause:"+e);
			e.printStackTrace();
		}finally {
			if(null!=objectInputStream) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
				logger.error("关闭反序列化流失败  - Cause:"+e);
				}
			}
			if(null!=byteOutputStream) {
				try {
					byteOutputStream.close();
				} catch (IOException e) {
					logger.error("关闭反序列化流失败  - Cause:"+e);
				}
			}
		}
		return null;
	}

}
*/