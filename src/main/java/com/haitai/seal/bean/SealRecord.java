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
public class SealRecord {

    private String id;

    private String sealname;

    private Integer sealtype;

    private SealStatus sealstatus;

    private String seal;

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

    private Date applystarttime;

    private String applyfile;

    private String seallist;

    private String remark;

    private Date applystoptime;

    private Date sealcreatetime;

    private Date revoketime;

    private String revokereason;

    private Date lastupdattime;

    private String sealversion;

    private Integer issync;

    private String publishsystemid;

    private String originsystemid;

    private boolean applystatus;

    private Date receivetime;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSealname() {
        return sealname;
    }

    public void setSealname(String sealname) {
        this.sealname = sealname == null ? null : sealname.trim();
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

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal == null ? null : seal.trim();
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
        this.picturedata = picturedata == null ? null : picturedata.trim();
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
        this.sealdept = sealdept == null ? null : sealdept.trim();
    }

    public String getApplyreason() {
        return applyreason;
    }

    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason == null ? null : applyreason.trim();
    }

    public String getApplyperson() {
        return applyperson;
    }

    public void setApplyperson(String applyperson) {
        this.applyperson = applyperson == null ? null : applyperson.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getApplystarttime() {
        return applystarttime;
    }

    public void setApplystarttime(Date applystarttime) {
        this.applystarttime = applystarttime;
    }

    public String getApplyfile() {
        return applyfile;
    }

    public void setApplyfile(String applyfile) {
        this.applyfile = applyfile == null ? null : applyfile.trim();
    }

    @Column(columnDefinition="mediumtext")
    public String getSeallist() {
        return seallist;
    }

    public void setSeallist(String seallist) {
        this.seallist = seallist == null ? null : seallist.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getApplystoptime() {
        return applystoptime;
    }

    public void setApplystoptime(Date applystoptime) {
        this.applystoptime = applystoptime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSealcreatetime() {
        return sealcreatetime;
    }

    public void setSealcreatetime(Date sealcreatetime) {
        this.sealcreatetime = sealcreatetime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getRevoketime() {
        return revoketime;
    }

    public void setRevoketime(Date revoketime) {
        this.revoketime = revoketime;
    }

    public String getRevokereason() {
        return revokereason;
    }

    public void setRevokereason(String revokereason) {
        this.revokereason = revokereason == null ? null : revokereason.trim();
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastupdattime() {
        return lastupdattime;
    }

    public void setLastupdattime(Date lastupdattime) {
        this.lastupdattime = lastupdattime;
    }


    public String getSealversion() {
        return sealversion;
    }

    public void setSealversion(String sealversion) {
        this.sealversion = sealversion;
    }

    public Integer getIssync() {
        return issync;
    }

    public void setIssync(Integer issync) {
        this.issync = issync;
    }

    public String getPublishsystemid() {
        return publishsystemid;
    }

    public void setPublishsystemid(String publishsystemid) {
        this.publishsystemid = publishsystemid == null ? null : publishsystemid.trim();
    }

    public String getOriginsystemid() {
        return originsystemid;
    }

    public void setOriginsystemid(String originsystemid) {
        this.originsystemid = originsystemid == null ? null : originsystemid.trim();
    }

    public boolean isApplystatus() {
        return applystatus;
    }

    public void setApplystatus(boolean applystatus) {
        this.applystatus = applystatus;
    }
    @Temporal(TemporalType.TIMESTAMP)
    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }

}