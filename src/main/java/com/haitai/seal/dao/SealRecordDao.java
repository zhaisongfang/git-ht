package com.haitai.seal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//SUN JENKINS2.83 sfz zsf
import com.haitai.seal.bean.SealRecord;

public interface SealRecordDao extends JpaRepository<SealRecord, String>,JpaSpecificationExecutor<SealRecord>{

	List<SealRecord> findByIdIn(String[] idArray);

}
