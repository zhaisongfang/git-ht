package com.haitai.seal.domain;

import java.util.List;
import java.util.Map;

public class SqAppData {
	
	public static final int TYPE_LEVEL_SAME=1;
	public static final int TYPE_LEVEL_CHILD=2;
	
	private int type=TYPE_LEVEL_SAME;
	
	private String ESCenterID;
	private String ESCenterName;
	private String validateCode;
	private String systemID;
	private List<Map<String, String>> dataInfo;
	
	
	public String getESCenterID() {
		return ESCenterID;
	}
	public void setESCenterID(String eSCenterID) {
		ESCenterID = eSCenterID;
	}
	public String getESCenterName() {
		return ESCenterName;
	}
	public void setESCenterName(String eSCenterName) {
		ESCenterName = eSCenterName;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public String getSystemID() {
		return systemID;
	}
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	public List<Map<String, String>> getDataInfo() {
		return dataInfo;
	}
	public void setDataInfo(List<Map<String, String>> dataInfo2) {
		this.dataInfo = (List<Map<String, String>>) dataInfo2;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		if(type==TYPE_LEVEL_SAME){
			sb.append("<appData>");
			sb.append("<systemID>").append(ESCenterID).append("</systemID>");
			sb.append("<systemName>").append(ESCenterName).append("</systemName>");
			sb.append("<validateCode>").append(validateCode).append("</validateCode>");
			sb.append("<parent_ESSM>");
			for (Map<String, String> infoMap : dataInfo) {
				for (Map.Entry<String, String> entry : infoMap.entrySet()) {
					sb.append("<").append(entry.getKey()).append(">");
					sb.append(entry.getValue());
					sb.append("</").append(entry.getKey()).append(">");
				}
			}
			sb.append("</parent_ESSM>");
			sb.append("</appData>");
		}else{
			sb.append("<appData>");
			sb.append("<ESCenterID>").append(ESCenterID).append("</ESCenterID>");
			sb.append("<ESCenterName>").append(ESCenterName).append("</ESCenterName>");
			sb.append("<validateCode>").append(validateCode).append("</validateCode>");
			sb.append("<parentCenter>");
			for (Map<String, String> infoMap : dataInfo) {
				sb.append("<parentSystem>");
				for (Map.Entry<String, String> entry : infoMap.entrySet()) {
					sb.append("<").append(entry.getKey()).append(">");
					sb.append(entry.getValue());
					sb.append("</").append(entry.getKey()).append(">");
				}
				sb.append("</parentSystem>");
			}
			sb.append("</parentCenter>");
			sb.append("</appData>");
		}
		
		return sb.toString();
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
