package com.haitai.seal.biz;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.haitai.seal.bean.SealCenter;
import com.haitai.seal.domain.SealDataInfo;

@Transactional
public interface CenterBiz {
	/**
	 * 分页查询
	 * 
	 * @param pageabel
	 * @return
	 */
	Page<SealCenter> findPage(Pageable pageabel);
	
	/**
	 * 保存上级发布验证的配置
	 * @param sealDataInfo webservice参数对象
	 * @return
	 */
	void saveUpperSign(SealDataInfo sealDataInfo) throws ParseException;
	/**
	 * 保存同步数据
	 * @param dataInfos
	 * @return 
	 * @throws ParseException
	 */
	void syncData502(List<Map<String, String>> dataInfos) throws ParseException;
	
	/**
	 * 向签发注册
	 * @param requestInfo
	 * @return 
	 * @throws ParseException
	 */
	void regORrelToval(SealDataInfo requestInfo) throws ParseException;

	/**
	 * 查询本系统的信息
	 * @return
	 */
	public SealCenter isSystemSearch();
	
	/**
	 * 注册系统信息入库
	 * @param sealCenter
	 * @param userName 当前系统登录用户名
	 * @return
	 */
	public void saveSystem(SealCenter sealCenter,String userName);
	/**
	 * 
	 * @param systemId 系统id
	 * @param userName 当前系统的系统用户名
	 * @return
	 */
	public void delSystem(String systemId,String userName);
	/**
	 * 显示系统管理中的详细信息
	 * @param escenterid
	 * @return
	 */
    SealCenter queryListByID(String escenterid);
    
	/**
	 * 
	 * @param isCenter 1为同级中心,2为下级中心
	 * @param type 0注册系统 1发布系统 2签发系统
	 * @return
	 */
	public List<SealCenter> searchList(int isCenter,String systemID,int type);
	

	public SealCenter searchParent(String systemID);
	/**
	 * 
	 * @param isCenter 1为同级中心,2为下级中心
	 * @param type 0注册系统 1发布系统 2签发系统
	 * @return
	 */
	public List<SealCenter> searchList(int isCenter, int type) ;
    /**
     * 上级签章系统信息
	 * @param systemID 
	 * @return
	 */
	public SealCenter searchParent();
	/**
	 * 查询子系统授权书文件信息
	 * @param systemId 系统id
	 * @param userName 当前系统名称
	 * @return
	 * @throws Exception 
	 */
	String downChildSystemFile(String id, String username) throws Exception;
	/**
	 * 查询签发系统授权文件信息
	 * @param systemId 系统id
	 * @param userName 当前系统名称
	 * @return
	 * @throws Exception 
	 */
	String downCenterSystem(String id, String username) throws Exception;
	/**
	 * 按照id查询当前系统信息
	 * @param id
	 * @return
	 */
	SealCenter findSystemById(String id);
	
	/**
	 * 推送处理
	 * @param requestInfo
	 * @throws Exception
	 */
	void sendToVal(SealDataInfo requestInfo) throws Exception;
	/**
	 * 是否有SystemId重复
	 * @param systemid
	 * @return
	 */
    SealCenter findSystemBySystemid(String systemid);
    /**
     * 是否有Systemname重复
     * @param systemname
     * @return
     */
    SealCenter findSystemBySystemname(String systemname);
    /**
     * 系统修改,去重复查询SystemId
     * @param systemid
     * @return
     */
    boolean findNoRepatBySystemid(String id,String systemid);
    /**
     * 系统修改,去重复查询systemname
     * @param systemname
     * @return
     */
    boolean findNoRepatBySystemname(String id,String systemname);



}
