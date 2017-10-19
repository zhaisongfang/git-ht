package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum SealType {

	UNIT(1,"单位印章"),
	PERSON(2,"个人印章");
	
    private int key;
    private String label ;
    
    public static SealType valueOf(int code){
    	for (SealType docType : SealType.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的类型");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (SealType type : SealType.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private SealType( int code,String value) {
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
