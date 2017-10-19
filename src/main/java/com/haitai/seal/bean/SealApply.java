package com.haitai.seal.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haitai.seal.enums.SealStatus;

@Entity
public class SealApply {
	
    private String id;

    /**
     * 印章id
     */
    private String sealid;
    
    private String sealname;

    private Integer sealtype;

    /**
     * 状态
     */
    private SealStatus sealstatus;

    private Integer picturetype;

    @JsonIgnore
    private String picturedata;

    private Integer picturehigh;

    private Integer picturewide;

    private Date starttime;

    private Date stoptime;

    private String sealdept;

    private String applyreason;

    private String applyperson;

    private String telephone;

    private Date applytime;

    private String applyfile;

    /**
     * 证书
     */
    private String seallist;

    private String remark;

    private Integer auditresult; // 审批结果
    
    private String auditcontent; // 审批内容
    
    private Date audittime;      // 审批时间
    
    private String approveperson; // 审批人
    
    private String qfapprovecontent; // 签发审核内容
    
    private Date qfapprovetime; // 签发审核时间
    
    private String publishsystemid;
    
    private String originsystemid;
    
    private String attachpath;
    
    private Date receivetime;
    /**
     * 版本
     */
    private String version;
    
    
    public String getQfapprovecontent() {
        return qfapprovecontent;
    }

    public void setQfapprovecontent(String qfapprovecontent) {
        this.qfapprovecontent = qfapprovecontent;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getQfapprovetime() {
        return qfapprovetime;
    }

    public void setQfapprovetime(Date qfapprovetime) {
        this.qfapprovetime = qfapprovetime;
    }

    public String getApproveperson() {
        return approveperson;
    }

    public void setApproveperson(String approveperson) {
        this.approveperson = approveperson;
    }

    public String getSealid() {
        return sealid;
    }

    public void setSealid(String sealid) {
        this.sealid = sealid;
    }

    @Column(columnDefinition="mediumtext")
    public String getAuditcontent() {
        return auditcontent;
    }

    public void setAuditcontent(String auditcontent) {
        this.auditcontent = auditcontent;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAudittime() {
        return audittime;
    }

    public void setAudittime(Date audittime) {
        this.audittime = audittime;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSealname() {
        return sealname;
    }

    public void setSealname(String sealname) {
        this.sealname = sealname;
    }

    public Integer getSealtype() {
        return sealtype;
    }

    public void setSealtype(Integer sealtype) {
        this.sealtype = sealtype;
    }

    public SealStatus getSealstatus() {
        return sealstatus;
    }

    public void setSealstatus(SealStatus sealstatus) {
        this.sealstatus = sealstatus;
    }

    public Integer getPicturetype() {
        return picturetype;
    }

    public void setPicturetype(Integer picturetype) {
        this.picturetype = picturetype;
    }

    @Column(columnDefinition="longtext")
    public String getPicturedata() {
        return picturedata;
    }

    public void setPicturedata(String picturedata) {
        this.picturedata = picturedata;
    }

    public Integer getPicturehigh() {
        return picturehigh;
    }

    public void setPicturehigh(Integer picturehigh) {
        this.picturehigh = picturehigh;
    }

    public Integer getPicturewide() {
        return picturewide;
    }

    public void setPicturewide(Integer picturewide) {
        this.picturewide = picturewide;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStoptime() {
        return stoptime;
    }

    public void setStoptime(Date stoptime) {
        this.stoptime = stoptime;
    }

    public String getSealdept() {
        return sealdept;
    }

    public void setSealdept(String sealdept) {
        this.sealdept = sealdept;
    }

    public String getApplyreason() {
        return applyreason;
    }

    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason;
    }

    public String getApplyperson() {
        return applyperson;
    }

    public void setApplyperson(String applyperson) {
        this.applyperson = applyperson;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public String getApplyfile() {
        return applyfile;
    }

    public void setApplyfile(String applyfile) {
        this.applyfile = applyfile;
    }

    @Column(columnDefinition="longtext")
    public String getSeallist() {
        return seallist;
    }

    public void setSeallist(String seallist) {
        this.seallist = seallist;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAuditresult() {
        return auditresult;
    }

    public void setAuditresult(Integer auditresult) {
        this.auditresult = auditresult;
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPublishsystemid() {
		return publishsystemid;
	}

	public void setPublishsystemid(String publishsystemid) {
		this.publishsystemid = publishsystemid;
	}

	public String getOriginsystemid() {
		return originsystemid;
	}

	public void setOriginsystemid(String originsystemid) {
		this.originsystemid = originsystemid;
	}

    public String getAttachpath() {
        return attachpath;
    }

    public void setAttachpath(String attachpath) {
        this.attachpath = attachpath;
    }
    @Temporal(TemporalType.TIMESTAMP)
    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }

}