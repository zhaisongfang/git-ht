package com.haitai.seal.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SealPushLog {
    
    private String id;

    private String target;
    private String targetName;
    private String targetAddress;
    private Date createtime;
    /**
     * 状态:0成功,非零失败
     */
    private Integer status;
    private Set<SealPushData> sealPushDatas=new HashSet<SealPushData>();
    
    @Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
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
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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

	@JsonIgnore
	@ManyToMany(mappedBy="sealPushLogs")
	public Set<SealPushData> getSealPushDatas() {
		return sealPushDatas;
	}
	public void setSealPushDatas(Set<SealPushData> sealPushDatas) {
		this.sealPushDatas = sealPushDatas;
	}
}