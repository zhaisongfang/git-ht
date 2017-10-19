package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuditResult {

	REG_AUDIT(0,"待注册系统审批"),
	REG_NO(1,"注册系统不通过"),
	QFXT_AUDIT(2,"待签发系统审批"),
	QFXT_NO(3,"签发系统审批不通过"),
    QFXT_YES(4,"签发系统审批通过");
	
    private int key;
    private String label ;
    
    // 构造函数，枚举类型只能为私有
    private AuditResult( int code,String value) {
    	this.key = code;
    	this.label = value;
    }
    
    public static AuditResult valueOf(int code){
    	for (AuditResult docType : AuditResult.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的审核结果");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (AuditResult type : AuditResult.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    public static Map<Integer,String> toRegMap() {
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	reMap.put(REG_NO.key, "不通过");
    	reMap.put(QFXT_AUDIT.key, "通过");
    	return reMap;
    }
    
    public static Map<Integer,String> toValMap() {
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	reMap.put(QFXT_NO.key, "不通过");
    	reMap.put(QFXT_YES.key, "通过");
    	return reMap;
    }

    @Override
    public String toString() {
        return label;
    }
    
	public int getKey() {
		return key;
	}

	public void setKey(int code) {
		this.key = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String value) {
		this.label = value;
	}

}
