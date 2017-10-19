package com.haitai.seal.domain;

public class Signature {

	private String certificate;
	private String OID;
	private String signatureValue;
	
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public String getSignatureValue() {
		return signatureValue;
	}
	public void setSignatureValue(String signatureValue) {
		this.signatureValue = signatureValue;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<signature>");
		sb.append("<certificate>").append(certificate==null?"":certificate).append("</certificate>");
		sb.append("<OID>").append(OID==null?"":OID).append("</OID>");
		sb.append("<signatureValue>").append(signatureValue==null?"":signatureValue).append("</signatureValue>");
		sb.append("</signature>");
		return sb.toString();
	}
}
