package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum SealStatus {

	MAKE_APPLY(0,"制章申请"),
	MAKE(1,"正常"),
	DOWNLOAD(2,"已下载"),
	FROZEN_APPLY(3,"冻结申请"),
	FROZEN(4,"已冻结"),
	UNFROZEN_APPLY(5,"解冻申请"),
	CANCEL_APPLY(6,"撤销申请"),
	CANCEL(7,"已撤销");
	
    private int key;
    private String label ;
    
    public static SealStatus valueOf(int code){
    	for (SealStatus docType : SealStatus.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的状态");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (SealStatus type : SealStatus.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    /**
     * 为接口提供状态装换map
     * @return
     */
    public static Map<Integer,String> toStandardMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	reMap.put(MAKE.key, "1");
    	reMap.put(MAKE_APPLY.key, "1");
    	reMap.put(UNFROZEN_APPLY.key, "1");
    	reMap.put(DOWNLOAD.key, "1");
    	reMap.put(FROZEN.key, "3");
    	reMap.put(FROZEN_APPLY.key, "3");
    	reMap.put(CANCEL.key, "2");
    	reMap.put(CANCEL_APPLY.key, "2");
    	return reMap;
    }
    
    /**
     * 为接口提供状态装换map
     * @return
     */
    public static Map<String,Integer> fromStandardMap(){
    	Map<String,Integer> reMap=new HashMap<String,Integer>();
    	reMap.put("1",DOWNLOAD.key);
    	reMap.put("3",FROZEN.key);
    	reMap.put("2",CANCEL.key);
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private SealStatus( int code,String value) {
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
