package com.haitai.seal.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 印章发布记录
 * @author chen
 *
 */
@Entity
public class SealPublishLog {
	private String id;

	private String stampID;
	private String applyID;
	private String sealname;
	private Integer sealtype;
	private Integer sealstatus;
	private Date qfapprovetime;
	private String applyreason;
	private String version;
	private Date starttime;
	private Date stoptime;
	private Date applytime;
	
	private String targetId;
    private String targetName;
    private String targetAddress;
    private Date sendTime;
    private String username;
	/**
     * 状态:0成功,非零失败
     */
    private Integer status;
	
	public String getStampID() {
		return stampID;
	}
	
	public void setStampID(String stampID) {
		this.stampID = stampID;
	}
	
	public String getApplyID() {
		return applyID;
	}

	public void setApplyID(String applyID) {
		this.applyID = applyID;
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

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return 状态:0成功,非零失败
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status 状态:0成功,非零失败
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}