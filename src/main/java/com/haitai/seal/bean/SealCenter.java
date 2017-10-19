package com.haitai.seal.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SealCenter {
	
	private String id;
	
    private String escenterid;

    private String escentername;

    private String validatecode;

    private String systemid;

    private String systemname;

    private Integer systemtype;

    private String serviceaddress;

    private String deployaddress;

    private String tel;

    private String linkman;

    private String buildorg;

    private String systemcertificate;

    private String sendsystemcertificate;

    private String oid;

    private String signaturevalue;

    private Date registertime;

    private String parentid;

    private String issystem;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEscenterid() {
        return escenterid;
    }

    public void setEscenterid(String escenterid) {
        this.escenterid = escenterid == null ? null : escenterid.trim();
    }

    public String getEscentername() {
        return escentername;
    }

    public void setEscentername(String escentername) {
        this.escentername = escentername == null ? null : escentername.trim();
    }

    public String getValidatecode() {
        return validatecode;
    }

    public void setValidatecode(String validatecode) {
        this.validatecode = validatecode == null ? null : validatecode.trim();
    }
    @Column(unique=true,nullable=false)
    public String getSystemid() {
        return systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid == null ? null : systemid.trim();
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname == null ? null : systemname.trim();
    }

    public Integer getSystemtype() {
        return systemtype;
    }

    public void setSystemtype(Integer systemtype) {
        this.systemtype = systemtype;
    }

    public String getServiceaddress() {
        return serviceaddress;
    }

    public void setServiceaddress(String serviceaddress) {
        this.serviceaddress = serviceaddress == null ? null : serviceaddress.trim();
    }

    public String getDeployaddress() {
        return deployaddress;
    }

    public void setDeployaddress(String deployaddress) {
        this.deployaddress = deployaddress == null ? null : deployaddress.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getBuildorg() {
        return buildorg;
    }

    public void setBuildorg(String buildorg) {
        this.buildorg = buildorg == null ? null : buildorg.trim();
    }
    @Column(columnDefinition="mediumtext")
    public String getSystemcertificate() {
        return systemcertificate = systemcertificate == null ? null : systemcertificate.replaceAll("\\s*", "");
    }

    public void setSystemcertificate(String systemcertificate) {
        this.systemcertificate = systemcertificate == null ? null : systemcertificate.trim();
    }

    public String getSendsystemcertificate() {
        return sendsystemcertificate;
    }

    public void setSendsystemcertificate(String sendsystemcertificate) {
        this.sendsystemcertificate = sendsystemcertificate == null ? null : sendsystemcertificate.trim();
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    public String getSignaturevalue() {
        return signaturevalue;
    }

    public void setSignaturevalue(String signaturevalue) {
        this.signaturevalue = signaturevalue == null ? null : signaturevalue.trim();
    }

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public String getIssystem() {
        return issystem;
    }

    public void setIssystem(String issystem) {
        this.issystem = issystem == null ? null : issystem.trim();
    }
}