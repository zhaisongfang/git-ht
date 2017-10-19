package com.haitai.seal.biz;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.haitai.seal.bean.SealApply;
import com.haitai.seal.bean.SealPushData;
import com.haitai.seal.bean.SealPushLog;
import com.haitai.seal.domain.SealDataInfo;

public interface SealApplyBiz {

	/**
	 * 分页查询
	 * @param apply
	 * @param pageable
	 * @return
	 */
	Page<SealApply> findPage(SealApply apply, Pageable pageable);

	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	SealApply get(String id);

	/**
	 * 通过SealId查询,按签发审核时间倒序排列
	 * @param id
	 * @return
	 */
	List<SealApply> findBySealid(String id);

	/**
	 * 印章发放
	 * @param stampID
	 * @return
	 */
	String makeSeal(String stampID) throws Exception;

	/**
	 * 向同级签发系统注册
	 * @return 注册返回值
	 */
	String reg(String fileStr,String username) throws Exception;

	/**
	 * 向上级中心注册
	 * @return 注册返回值
	 */
	void upRegSubmit(String fileStr,String username) throws Exception;

	/**
	 * 审批
	 * @param sealApply
	 * @param username 
	 * @return 
	 * @throws Exception 
	 */
	String approve(SealApply sealApply, String username) throws Exception;

	void process199(SealDataInfo requestInfo) throws ParseException;

	/**
	 * 系统操作日志(记录在数据库中)
	 * @param type LogType枚举类型
	 * @param status 系统或印章
	 * @param objid 操作记录的id
	 * @param objname 操作记录名称
	 * @param userName 操作用户登录用户名
	 */
	void addLog(int type,int status,String objid,String objname,String userName);

	Page<? extends Object> findPageBylog(Pageable pageable);

	/**
	 * 保存申请信息
	 * @param sealApply
	 */
	void saveApply(SealApply sealApply);

	/**
	 * 签发审批
	 * @param old
	 * @param username
	 * @return 
	 * @throws Exception 
	 */
	String qfApprove(SealApply old, String username) throws Exception;

	/**
	 * 定时推送印章
	 * @throws Exception
	 */
	void timerTask() throws Exception;

	/**
	 * 根据开始时间和结束时间查询
	 * @param sealRecord
	 * @return
	 * @throws ParseException 
	 */
	List<SealApply> findByStartAndStop(String starttime, String stoptime) throws ParseException;
	/**
	 * 
	 * @param apply
	 */
	void saveOtherApply(SealApply apply);
	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param file
	 */
	boolean saveFile(String path, String fileName, MultipartFile file);

	/**
	 * 分页查询推送失败日志
	 * @param pushLog 查询条件
	 * @param pageable 分页对象
	 * @return
	 */
	Page<SealPushLog> findPushFailLog(SealPushLog pushLog,Pageable pageable);

	/**
	 * 分页查询推送失败日志详细-印章列表
	 * @param pushLogId
	 * @param pushData
	 * @param pageable
	 * @return
	 */
	Page<SealPushData> findPushFailDetailLog(String pushLogId, SealPushData pushData, Pageable pageable);
	/**
	 * 
	 * @param sealname
	 * @return
	 */
    boolean hasSealName(String sealname);


}
