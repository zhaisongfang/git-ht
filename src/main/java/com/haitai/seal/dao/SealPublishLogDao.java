package com.haitai.seal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.haitai.seal.bean.SealPublishLog;

public interface SealPublishLogDao extends JpaRepository<SealPublishLog, String>,JpaSpecificationExecutor<SealPublishLog>{

}
