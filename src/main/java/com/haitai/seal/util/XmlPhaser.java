package com.haitai.seal.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlPhaser {

	public static String getProperty(String source,String propertyName) throws Exception{
		return getProperty(source,"<"+propertyName+">","</"+propertyName+">");
	}
	
	private static String getProperty(String source,String regStart,String regEnd) throws Exception{
		String pattern = regStart+"([\\s\\S]*?)"+regEnd;
		Pattern r = Pattern.compile(pattern);
	    Matcher m = r.matcher(source);
	    if (m.find( )) {
	    	return m.group(1);
	    }
	    throw new Exception("无法找到属性:"+regStart);
	}
	
	public static String getSubString(String source,String propertyName) throws Exception{
		return getSubString(source,"<"+propertyName+">","</"+propertyName+">");
	}
	private static String getSubString(String source,String regStart,String regEnd) throws Exception{
		String pattern = regStart+"([\\s\\S]*?)"+regEnd;
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(source);
	    if (m.find( )) {
	    	return m.group(0);
	    }
	    throw new Exception("无法找到属性:"+regStart);
	}
	
	public static String toXml(Object obj){
		StringBuilder sb=new StringBuilder();
		if(obj instanceof List<?>){
			List<?> list=(List<?>) obj;
			for (Object object : list) {
				sb.append(toXml(object));
			}
			return sb.toString();
		}else if(obj instanceof Map<?,?>){
			Map<?, ?> map=(Map<?, ?>) obj;
			for (Entry<?, ?> entry : map.entrySet()) {
				sb.append(getTagBegin(entry.getKey().toString()));
				sb.append(toXml(entry.getValue()));
				sb.append(getTagEnd(entry.getKey().toString()));
			}
			return sb.toString();
		}else{
			return obj==null?"":obj.toString();
		}
	}
	
	public static String getTagBegin(String tagName){
		return "<"+tagName+">";
	}
	
	public static String getTagEnd(String tagName){
		return "</"+tagName+">";
	}
}
