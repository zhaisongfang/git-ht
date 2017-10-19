package com.haitai.seal.bean;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SealPushData {
	private String id;

	private String publishsystemid;
	private String originsystemid;
	private String stampID;
	private String sealname;
	private Integer sealtype;
	private Integer sealstatus;
	private Date qfapprovetime;
	private String applyreason;
	private String version;
	private Date starttime;
	private Date stoptime;
	private Date applytime;
	private String sealcenter;//0:上级往下级推送;1:下级往上级推送
	private String ispush;//0:未推送;1:已推送
	
	private Set<SealPushLog> sealPushLogs;
	
	public String getStampID() {
		return stampID;
	}
	
	public void setStampID(String stampID) {
		this.stampID = stampID;
	}
	
	public String getSealcenter() {
		return sealcenter;
	}

	public void setSealcenter(String sealcenter) {
		this.sealcenter = sealcenter;
	}

	public String getIspush() {
		return ispush;
	}

	public void setIspush(String ispush) {
		this.ispush = ispush;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getSealstatus() {
		return sealstatus;
	}

	public void setSealstatus(Integer sealstatus) {
		this.sealstatus = sealstatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getQfapprovetime() {
		return qfapprovetime;
	}

	public void setQfapprovetime(Date qfapprovetime) {
		this.qfapprovetime = qfapprovetime;
	}

	public String getApplyreason() {
		return applyreason;
	}

	public void setApplyreason(String applyreason) {
		this.applyreason = applyreason;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	@ManyToMany
	public Set<SealPushLog> getSealPushLogs() {
		return sealPushLogs;
	}

	public void setSealPushLogs(Set<SealPushLog> sealPushLogs) {
		this.sealPushLogs = sealPushLogs;
	}

}
