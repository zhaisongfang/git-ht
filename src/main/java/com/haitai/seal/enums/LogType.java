package com.haitai.seal.enums;

import java.util.HashMap;
import java.util.Map;

public enum LogType {

	//注意: 此顺序号必须连续
	SEAL_APPLY(0,"印章申请成功"),
	SEAL_FROZEN(1,"印章冻结成功"),
	SEAL_DOWNLOAD(2,"印章下载成功"),
	SEAL_UNFROZEN(3,"印章解冻成功"),
	SEAL_CANCEL(4,"印章撤消成功"),
	SEAL_SYSIGN(5,"系统注册成功"),
	SEAL_SYSMODIFY(6,"系统修改成功"),
	SEAL_ZXSIGN_YES(7,"向上级中心注册成功"),
	SEAL_QFSNGN_YES(8,"向签发系统注册成功"),
	SEAL_LOADZX(9,"下载中心系统授权文件"),
	SEAL_LOADZXT(10,"下载子系统授权文件"),
	SEAL_PUSHYZ(11,"印章信息推送"),
	SEAL_GETYZ(12,"印章信息拉取"),
	SEAL_APPLY_AUDIT_YES(13,"印章申请审核通过"),
	SEAL_APPLY_AUDIT_NO(14,"印章申请审核不通过"),
	SEAL_FROZEN_AUDIT_YES(15,"印章冻结审核通过"),
	SEAL_FROZEN_AUDIT_NO(16,"印章冻结审核不通过"),
	SEAL_UNFROZEN_AUDIT_YES(17,"印章解冻审核通过"),
	SEAL_UNFROZEN_AUDIT_NO(18,"印章解冻审核不通过"),
	SEAL_CANCEL_AUDIT_YES(19,"印章撤消审核通过"),
	SEAL_CANCEL_AUDIT_NO(20,"印章撤消审核不通过"),
	SEAL_QFSNGN_NO(21,"向签发系统注册失败"),
	SEAL_ZXSIGN_NO(22,"向上级中心注册失败"),
	SEAL_SYSDELETE(23,"系统删除成功");
	
    private int key;
    private String label ;
    
    public static LogType valueOf(int code){
    	for (LogType docType : LogType.values()) {
			if(docType.key==code){
				return docType;
			}
		}
    	throw new IllegalArgumentException("不支持的日志类型");
    }
    
    public static Map<Integer,String> toMap(){
    	Map<Integer,String> reMap=new HashMap<Integer, String>();
    	for (LogType type : LogType.values()) {
    		reMap.put(type.key, type.label);
    	}
    	return reMap;
    }
    
    // 构造函数，枚举类型只能为私有
    private LogType( int code,String value) {
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
