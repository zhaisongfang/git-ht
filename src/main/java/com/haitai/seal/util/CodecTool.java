package com.haitai.seal.util;

import org.apache.commons.codec.binary.Base64;

public class CodecTool {

	/**
	 * 使用base64编码
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encodeStr(String plainText) {
		byte[] b=plainText.getBytes();
        Base64 base64=new Base64();
        b=base64.encode(b);
        String s=new String(b);
        return s;
	}

	/**
	 * 使用base64解码
	 * 
	 * @param encodeStr
	 * @return
	 */
	public static String decodeStr(String encodeStr) {
		byte[] b=encodeStr.getBytes();
        Base64 base64=new Base64();
        b=base64.decode(b);
        String s=new String(b);
        return s;
	}

	public static String encodeBufferBase64(byte[] b) {
		Base64 base64=new Base64();
        b=base64.encode(b);
        String s=new String(b);
        return s;
	}

}
