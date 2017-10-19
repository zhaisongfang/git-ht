package com.haitai.seal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.haitai.seal.bean.SealPushLog;

public interface SealPushLogDao extends JpaRepository<SealPushLog, String>, JpaSpecificationExecutor<SealPushLog>{

}
