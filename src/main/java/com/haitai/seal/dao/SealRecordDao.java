package com.haitai.seal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.haitai.seal.bean.SealRecord;

public interface SealRecordDao extends JpaRepository<SealRecord, String>,JpaSpecificationExecutor<SealRecord>{

	List<SealRecord> findByIdIn(String[] idArray);

}
