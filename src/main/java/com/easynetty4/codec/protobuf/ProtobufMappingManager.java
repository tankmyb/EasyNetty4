package com.easynetty4.codec.protobuf;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.MessageLite;

/**
 * 协议ID映射管理 自动生成文件
 */
public class ProtobufMappingManager {

	/** msgId <-> MessageLite Req请求映射 */
	private static Map<Integer, MessageLite> idClazzMap = new HashMap<Integer, MessageLite>();

	/** MessageLiteClass <--> msgId Resp响应映射 */
	private static Map<Class<? extends MessageLite>, Integer> clazzIdMap = new HashMap<Class<? extends MessageLite>, Integer>();

	public static void put(Integer key,MessageLite value){
		idClazzMap.put(key, value);
		clazzIdMap.put(value.getClass(), key);
	}
	public static MessageLite getMessage(int messageId) {
		return idClazzMap.get(messageId);
	}

	public static int getMessageId(Class<?> clazz) {
		return clazzIdMap.get(clazz);
	}
}