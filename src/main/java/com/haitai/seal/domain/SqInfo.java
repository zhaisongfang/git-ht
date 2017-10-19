package com.haitai.seal.domain;


public class SqInfo {

	private SqAppData sappData;
	private Signature signature;
	

	public SqAppData getSappData() {
		return sappData;
	}

	public void setSappData(SqAppData sappData) {
		this.sappData = sappData;
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
		sb.append("<ESealCenterRegister>");
		sb.append(sappData.toString());
		sb.append(signature.toString());
		sb.append("</ESealCenterRegister>");
		return sb.toString();
	}
	public String toRegOrRelString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ESealSubsystemRegister>");
		sb.append(sappData.toString());
		sb.append(signature.toString());
		sb.append("</ESealSubsystemRegister>");
		return sb.toString();
	}
	
}
