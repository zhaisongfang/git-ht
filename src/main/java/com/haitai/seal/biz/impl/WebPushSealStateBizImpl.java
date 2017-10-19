package com.haitai.seal.biz.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haitai.seal.bean.SealRecord;
import com.haitai.seal.biz.WebPushSealStateBiz;
import com.haitai.seal.component.Config;
import com.haitai.seal.dao.SealRecordDao;
import com.haitai.seal.util.BasicTool;
import com.haitai.seal.util.DomainUtil;
import com.haitai.seal.webService.client.ServiceClient;

@Service("webPushSealStateBiz")
@Transactional(rollbackFor=Throwable.class)
public class WebPushSealStateBizImpl implements WebPushSealStateBiz {

	private DomainUtil domainUtil;

	@Autowired
	private SealRecordDao sealRecordDao;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private Config config;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 同步印章状态，通过 401协议
	 * 
	 * @param idArray
	 *            需要同步状态的印章列表id集合
	 * @return 返回同步状态 0：成功、1：失败
	 * @throws Exception
	 */
	@Override
	public String querySealState(String[] idArray) throws Exception {

		List<SealRecord> sealRecordList = sealRecordDao.findByIdIn(idArray);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 便利印章记录
		for (int i = 0; i < sealRecordList.size(); i++) {
			SealRecord tempSealRecord = sealRecordList.get(i);

			Map<String, String> map = new HashMap<String, String>();
			map.put("publishSystemID", "");
			map.put("originSystemID", "");
			map.put("stampID", tempSealRecord.getId());
			map.put("stampName", tempSealRecord.getSealname());
			map.put("stampType", String.valueOf(tempSealRecord.getSealtype()));
			map.put("stampState",
					String.valueOf(tempSealRecord.getSealstatus()));
			map.put("revocationDate",
					String.valueOf(tempSealRecord.getRevoketime()));
			map.put("revocationReason", tempSealRecord.getRevokereason());
			map.put("version", String.valueOf(tempSealRecord.getSealversion()));
			map.put("validStart",
					(("null").equals(tempSealRecord.getStarttime())
							|| ("").equals(tempSealRecord.getStarttime()) || tempSealRecord
							.getStarttime() == null) ? null : sdf
							.format(tempSealRecord.getStarttime()));
			map.put("validEnd",
					(("null").equals(tempSealRecord.getStoptime())
							|| ("").equals(tempSealRecord.getStoptime()) || tempSealRecord
							.getStoptime() == null) ? null : sdf
							.format(tempSealRecord.getStoptime()));
			map.put("createTime",
					(("null").equals(tempSealRecord.getSealcreatetime())
							|| ("").equals(tempSealRecord.getSealcreatetime()) || tempSealRecord
							.getSealcreatetime() == null) ? null : sdf
							.format(tempSealRecord.getSealcreatetime()));
			map.put("lastUpdateTime",
					(("null").equals(tempSealRecord.getLastupdattime())
							|| ("").equals(tempSealRecord.getLastupdattime()) || tempSealRecord
							.getLastupdattime() == null) ? null : sdf
							.format(tempSealRecord.getLastupdattime()));
			list.add(map);
		}

		String xml = domainUtil.genParam("401", BasicTool.getUUID(), list);
		
		//TODO 地址不知道
		String reStr = ServiceClient.getService("").sendData(xml, "401");// 推送路径标识
		return reStr;
	}

	@Autowired
	public void setDomainUtil(DomainUtil domainUtil) {
		this.domainUtil = domainUtil;
	}
}