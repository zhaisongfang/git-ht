package com.haitai.seal.biz;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.haitai.seal.bean.SealLog;
import com.haitai.seal.dao.SealLogDao;
import com.haitai.seal.util.BasicTool;



public class BaseBiz {
	
	@Autowired
	protected SealLogDao sealLogDao;
	
	protected Logger logger=LoggerFactory.getLogger(getClass());
	
	/**
	 * 系统操作日志(记录在数据库中)
	 * @param type LogType枚举类型
	 * @param status LogStatus枚举类型
	 * @param sign 操作记录的id
	 * @param userName 操作用户登录用户名
	 */
	public void addLog(int type,int status,String sign,String userName){
		SealLog log=new SealLog();
		log.setId(BasicTool.getUUID());
		log.setType(type);
		log.setObjectid(sign);
		log.setCreatetime(new Date());
		log.setUsername(userName);
		sealLogDao.save(log);
	}
	
}
