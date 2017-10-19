package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum Upordown {

	UP(0,"上级系统信息"),
	DOWN(1,"下级系统信息");
	
    private int key;
    private String label ;
    
    public static Upordown valueOf(int code){
    	for (Upordown docType : Upordown.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的上下级");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (Upordown type : Upordown.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private Upordown( int code,String value) {
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
