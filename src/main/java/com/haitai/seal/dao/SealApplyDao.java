package com.haitai.seal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.haitai.seal.bean.SealApply;

public interface SealApplyDao  extends JpaRepository<SealApply, String>,JpaSpecificationExecutor<SealApply>{

	List<SealApply> findBySealidOrderByQfapprovetimeDesc(String id);
	
	List<SealApply> findBySealidInAndAuditresult(String[] idArray,Integer auditresult);
	
	List<SealApply> findBySealname(String sealname);
	@Query("select o from SealApply o where sealid=?1 and auditresult=4")
	List<SealApply> findBySealid(String sealid);
}
