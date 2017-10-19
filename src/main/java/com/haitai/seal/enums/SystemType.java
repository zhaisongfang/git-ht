package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum SystemType {
	//0-注册管理子系统, 1-发布验证子系统, 2-签章中心（签发管理子系统）
	REG(0,"注册管理子系统"),
	SIGN(1,"发布验证子系统"),
	CENTER(2,"签章中心（签发管理子系统）");
	
    private int key;
    private String label ;
    
    public static SystemType valueOf(int code){
    	for (SystemType docType : SystemType.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的系统类型");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (SystemType type : SystemType.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private SystemType( int code,String value) {
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
