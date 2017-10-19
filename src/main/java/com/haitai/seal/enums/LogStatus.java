package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum LogStatus {

	  SYSTEM (0,"系统日志"),
	    SEAL(1,"印章日志"); 
	
    private int key;
    private String label ;
    
    public static LogStatus valueOf(int code){
    	for (LogStatus docType : LogStatus.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的日志状态");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (LogStatus type : LogStatus.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private LogStatus( int code,String value) {
        this.key = code;
        this.label = value;
    }

    @Override
    public String toString() {
        return label;
    }

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
