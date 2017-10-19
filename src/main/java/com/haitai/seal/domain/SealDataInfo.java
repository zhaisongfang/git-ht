package com.haitai.seal.domain;


public class SealDataInfo {

	private String dataType;
	
	private AppData appData;
	
	private Signature signature;
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public AppData getAppData() {
		return appData;
	}

	public void setAppData(AppData appData) {
		this.appData = appData;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<sealDataInfo>");
		sb.append("<dataType>").append(dataType).append("</dataType>");
		sb.append(appData.toString());
		sb.append(signature.toString());
		sb.append("</sealDataInfo>");
		return sb.toString();
	}
	
}
