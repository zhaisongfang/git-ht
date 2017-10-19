package com.haitai.seal.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

public class AppData {

	private String appID;
	private String dataTime;
	private String version;
	private String systemID;
	private List<Map<String, String>> dataInfo;
	
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
		sb.append("<appData>");
		sb.append("<appID>").append(appID==null?"":appID).append("</appID>");
		sb.append("<dataTime>").append(dataTime==null?"":dataTime).append("</dataTime>");
		sb.append("<version>").append(version==null?"":version).append("</version>");
		sb.append("<systemID>").append(systemID==null?"":systemID).append("</systemID>");
		for (Map<String, String> infoMap : dataInfo) {
			sb.append("<dataInfo>");
			for (Map.Entry<String, String> entry : infoMap.entrySet()) {
				sb.append("<").append(entry.getKey()).append(">");
				sb.append(entry.getValue()==null?"":StringEscapeUtils.escapeXml11(entry.getValue()));
				sb.append("</").append(entry.getKey()).append(">");
			}
			sb.append("</dataInfo>");
		}
		sb.append("</appData>");
		return sb.toString();
	}
	
	
}
