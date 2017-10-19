package com.haitai.seal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.haitai.seal.bean.SealPushData;

public interface SealPushDataDao extends JpaRepository<SealPushData, String>,JpaSpecificationExecutor<SealPushData>{

	/**
	 * 查询
	 * @param ispush 0:未推送;1:已推送
	 * @param sealcenter 0:上级往下级推送;1:下级往上级推送
	 * @return
	 */
	List<SealPushData> findByispushAndSealcenter(String ispush, String sealcenter);

}
