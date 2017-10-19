package com.haitai.seal.util;

import java.util.UUID;

/**
 * 基础工具
 * 
 * @author yongying.zheng
 * @version 2014-05-14
 */
public class BasicTool {

	/**
	 * 获取全球唯一uuid
	 * 
	 * @return 32位uuid
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
}
