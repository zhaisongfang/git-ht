package com.haitai.seal.biz;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.haitai.seal.bean.SealApply;
import com.haitai.seal.bean.SealCenter;
import com.haitai.seal.bean.SealRecord;

public interface SealRecordBiz {


	/**
	 * 分页查询
	 * @param record 
	 * @param pageable
	 * @return
	 */
	Page<SealRecord> findPage(SealRecord record, Pageable pageable);

	/**
	 * 拉数据
	 * @param starttime
	 * @param stoptime
	 * @throws Exception 
	 */
	void pullData(Date starttime, Date stoptime) throws Exception;

	/**
	 * 推数据
	 * @param starttime
	 * @param stoptime
	 * @throws Exception 
	 */
	void pushData(String[] pushStr) throws Exception;
	
	/**
	 * 保存同步数据
	 * @param dataInfos
	 * @return 
	 * @throws ParseException
	 */
	void syncData(List<Map<String, String>> dataInfos) throws ParseException;

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	SealRecord selectByPrimaryKey(String id);

	/**
	 * 保存
	 * @param sealRecord
	 */
	void save(SealRecord sealRecord);
	/**
	 * 发布
	 * @return 是否成功发布
	 */
	boolean publicDate(SealCenter relcenter, List<SealApply> seals,String username);
    /**
     * 
     * @param id
     * @return
     */
    SealRecord get(String id);
	/**
	 * 手动发布
	 * @param id
	 * @return 
	 * @throws Exception 
	 */
    int publishAgain(String id,String username);
    /**
     * 批量发布
     * @param ids
     * @param relcenters
     * @param username 
     */
	int batchPublish(String[] ids, List<SealCenter> relcenters, String username);
	/**
	 * 
	 * @param apply
	 * @param relcenters
	 * @return
	 */
    
	int autoPublish(SealApply apply, List<SealCenter> relcenters,String username);

}
