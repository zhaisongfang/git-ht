package com.haitai.seal.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.haitai.seal.bean.SealPublishLog;

public interface SealPublishLogBiz {

	/**
	 * 分页查询
	 * @param sealId 印章id
	 * @param pageable
	 * @return
	 */
	Page<SealPublishLog> findPage(String sealId, Pageable pageable);
}
