package com.haitai.seal.biz.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haitai.seal.bean.SealApply;
import com.haitai.seal.bean.SealCenter;
import com.haitai.seal.bean.SealPublishLog;
import com.haitai.seal.bean.SealPushData;
import com.haitai.seal.bean.SealRecord;
import com.haitai.seal.biz.CenterBiz;
import com.haitai.seal.biz.SealRecordBiz;
import com.haitai.seal.dao.SealApplyDao;
import com.haitai.seal.dao.SealCenterDao;
import com.haitai.seal.dao.SealPublishLogDao;
import com.haitai.seal.dao.SealPushDataDao;
import com.haitai.seal.dao.SealRecordDao;
import com.haitai.seal.domain.SealDataInfo;
import com.haitai.seal.enums.AuditResult;
import com.haitai.seal.enums.SealStatus;
import com.haitai.seal.util.BasicTool;
import com.haitai.seal.util.DomainUtil;
import com.haitai.seal.webService.client.ServiceClient;

@Service("sealRecordBiz")
@Transactional(rollbackFor=Throwable.class)
public class SealRecordBizImpl implements SealRecordBiz {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SealRecordDao sealRecordDao;
	@Autowired
	private SealApplyDao sealApplyDao;
	@Autowired
	private CenterBiz centerBiz;
	@Autowired
	private SealPushDataDao sealPushDataDao;

	@Autowired
	private SealPublishLogDao sealPublishLogDao;

	private DomainUtil domainUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Page<SealRecord> findPage(SealRecord record, Pageable pageable) {
		return sealRecordDao.findAll(getWhereClause(record), pageable);
	}

	private Specification<SealRecord> getWhereClause(final SealRecord record) {
		return new Specification<SealRecord>() {
			@Override
			public Predicate toPredicate(Root<SealRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<Predicate>();
				if (record.getSealstatus() != null) {
					predicate.add(cb.equal(root.get("sealstatus").as(Integer.class), record.getSealstatus().getKey()));
				}
				if (StringUtils.isNotBlank(record.getSealname())) {
					Path<String> name = root.get("sealname");
					predicate.add(cb.like(name, "%" + record.getSealname() + "%"));
				}
				if (record.getStarttime() != null) {
					predicate.add(cb.greaterThan(root.get("sealcreatetime").as(Date.class), record.getStarttime()));
				}
				if (record.getStoptime() != null) {
					predicate.add(cb.lessThan(root.get("sealcreatetime").as(Date.class), record.getStoptime()));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}

	@Override
	public void pullData(Date starttime, Date stoptime) throws Exception {
		Map<String, String> info = new LinkedHashMap<String, String>();
		info.put("beginTime", sdf.format(starttime));
		info.put("endTime", stoptime == null ? "" : sdf.format(stoptime));

		String str = domainUtil.genParam("403", BasicTool.getUUID(), info);

		SealCenter center = centerBiz.searchParent();

		if (null != center) {

			String reStr = ServiceClient.getService(center.getServiceaddress() + "?wsdl").sendData(str, "403");// 推送路径标识

			SealDataInfo responseInfo = domainUtil.phase(reStr);

			syncData(responseInfo.getAppData().getDataInfo());

			if ("404".equals(responseInfo.getDataType())) {

				syncData(responseInfo.getAppData().getDataInfo());
				List<SealCenter> valCenter = centerBiz.searchList(2, 1);
				if (null != valCenter && valCenter.size() > 0) {
					for (SealCenter val : valCenter) {
						ServiceClient.getService(val.getServiceaddress() + "?wsdl").sendData(reStr, "401");// 推送路径标识
					}
				}
				;
			} else {
				throw new RuntimeException("返回的dataType错误!");
			}
			;
		} else {
			return;
		}
	}

	@Override
	public void pushData(String[] pushStr) throws Exception {
		List<SealApply> sealApplyList = sealApplyDao.findBySealidInAndAuditresult(pushStr,AuditResult.QFXT_YES.getKey());

		SealCenter centerP = centerBiz.searchParent();

		if (centerP == null) {
			// 保存数据到本地，并设置ispush为0------0:未推送;1:已推送
			for (SealApply sealApply : sealApplyList) {
				SealPushData sealPushData = genPushData(sealApply);
				// 上级往下级推送
				sealPushData.setSealcenter("0");
				sealPushDataDao.save(sealPushData);
			}
		} else {
			// 保存数据到本地，并设置ispush为0------0:未推送;1:已推送
			for (SealApply sealApply : sealApplyList) {
				SealPushData sealPushData = genPushData(sealApply);
				// 下级往上级推送
				sealPushData.setSealcenter("1");
				sealPushDataDao.save(sealPushData);
			}
		}
	}

	/**
	 * 保存成待推送记录
	 * 
	 * @param sealApply
	 * @return
	 */
	private SealPushData genPushData(SealApply sealApply) {
		SealPushData sealPushData = new SealPushData();
		sealPushData.setId(sealApply.getId());
		sealPushData.setPublishsystemid(sealApply.getPublishsystemid());
		sealPushData.setOriginsystemid(sealApply.getOriginsystemid());
		sealPushData.setStampID(sealApply.getSealid());
		sealPushData.setSealname(sealApply.getSealname());
		sealPushData.setSealtype(sealApply.getSealtype());
		sealPushData.setSealstatus(sealApply.getSealstatus().getKey());
		sealPushData.setQfapprovetime(sealApply.getQfapprovetime());
		sealPushData.setApplyreason(sealApply.getApplyreason());
		sealPushData.setVersion(sealApply.getVersion());
		sealPushData.setStarttime(sealApply.getStarttime());
		sealPushData.setStoptime(sealApply.getStoptime());
		sealPushData.setApplytime(sealApply.getApplytime());
		sealPushData.setIspush("0");
		return sealPushData;
	}

	@Override
	public boolean publicDate(SealCenter relcenter, List<SealApply> seals,String username) {
		boolean result = true;
		for (SealApply apply : seals) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("stampID", apply.getSealid());
				map.put("stampName", apply.getSealname());
				map.put("stampType", apply.getSealtype() + "");
				map.put("version", apply.getVersion());
				map.put("stampState", SealStatus.toStandardMap().get(apply.getSealstatus().getKey()));
				map.put("revocationDate", sdf.format(apply.getQfapprovetime()));
				map.put("revocationReason", apply.getApplyreason());
				map.put("validStart", sdf.format(apply.getStarttime()));
				map.put("validEnd", sdf.format(apply.getStoptime()));
				map.put("createTime", sdf.format(apply.getQfapprovetime()));
				// 生成301xml文件
				String xml = domainUtil.genParam("301", apply.getId(), map);
				String reStr = ServiceClient.getService(relcenter.getServiceaddress() + "?wsdl").sendData(xml, "301");
				domainUtil.phase(reStr);
				genPublishLog(relcenter, apply,username, true);
			} catch (Exception e) {
				logger.error("印章发布失败", e);
				logger.error("系统名称:{},印章名称:{}", relcenter.getSystemname(), apply.getSealname());
				genPublishLog(relcenter, apply,username, false);
				result = false;
			}
		}
		return result;
	}

	/**
	 * 记录发布记录
	 * 
	 * @param relcenter
	 * @param apply
	 * @param success
	 */
	private void genPublishLog(SealCenter relcenter, SealApply apply, String username,boolean success) {
		SealPublishLog sealPublishLog = new SealPublishLog();
		sealPublishLog.setId(BasicTool.getUUID());
		sealPublishLog.setApplyID(apply.getId());
		sealPublishLog.setStampID(apply.getSealid());
		sealPublishLog.setSealname(apply.getSealname());
		sealPublishLog.setSealtype(apply.getSealtype());
		sealPublishLog.setVersion(apply.getVersion());
		sealPublishLog.setSealstatus(apply.getSealstatus().getKey());
		sealPublishLog.setQfapprovetime(apply.getQfapprovetime());
		sealPublishLog.setApplyreason(apply.getApplyreason());
		sealPublishLog.setStarttime(apply.getStarttime());
		sealPublishLog.setStoptime(apply.getStoptime());
		sealPublishLog.setQfapprovetime(apply.getQfapprovetime());
		sealPublishLog.setTargetId(relcenter.getSystemid());
		sealPublishLog.setTargetName(relcenter.getSystemname());
		sealPublishLog.setTargetAddress(relcenter.getServiceaddress());
		sealPublishLog.setSendTime(new Date());
		sealPublishLog.setStatus(success ? 0 : 1);
		sealPublishLog.setUsername(username);
		sealPublishLogDao.save(sealPublishLog);
	}

	/*
	 * @Override public void publicDate(SealCenter relcenter,List<SealApply>
	 * seals) throws Exception { //生成301xml文件 HashMap<String, String> map = new
	 * HashMap<String, String>(); map.put("stampID",apply.getSealid());
	 * map.put("stampName", apply.getSealname()); map.put("stampType",
	 * apply.getSealtype()+""); map.put("version", apply.getVersion());
	 * map.put("stampState",
	 * SealStatus.toStandardMap().get(apply.getSealstatus().getKey()));
	 * map.put("revocationDate",sdf.format(apply.getQfapprovetime()));
	 * map.put("revocationReason", apply.getApplyreason());
	 * map.put("validStart", sdf.format(apply.getStarttime()));
	 * map.put("validEnd", sdf.format(apply.getStoptime()));
	 * map.put("createTime", sdf.format(apply.getQfapprovetime())); String xml =
	 * domainUtil.genParam("301", apply.getId(), map); //调用接口将信息发布到发布验证系统
	 * List<SealCenter> relcenter= centerBiz.searchList(1, 1); for(SealCenter
	 * center:relcenter){ String reStr =
	 * ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(
	 * xml, "301"); domainUtil.phase(reStr); } }
	 */
	/*
	 * @Override public int publishByHands(String id) throws Exception {
	 * List<SealApply> applys=sealApplyDao.findBySealid(id); for(SealApply
	 * apply:applys){ publicDate(apply); } }
	 */

	public void syncData(List<Map<String, String>> dataInfos) throws ParseException {
		for (Map<String, String> dataInfo : dataInfos) {
			SealRecord sealRecord = new SealRecord();
			// 数据发布系统标识
			sealRecord.setPublishsystemid(dataInfo.get("publishSystemID"));
			// 数据来源系统标识
			sealRecord.setOriginsystemid(dataInfo.get("originSystemID"));
			// 印章id
			sealRecord.setId(dataInfo.get("stampID"));
			// 印章名称
			sealRecord.setSealname(dataInfo.get("stampName"));
			// 印章类型
			sealRecord.setSealtype(
					StringUtils.isBlank(dataInfo.get("stampType")) ? null : Integer.valueOf(dataInfo.get("stampType")));
			// 印章状态
			// sealRecord.setSealstatus(SealStatus.fromStandardMap().get(dataInfo.get("stampState")));
			// 撤销时间
			sealRecord.setRevoketime(StringUtils.isBlank(dataInfo.get("revocationDate")) ? null
					: sdf.parse(dataInfo.get("revocationDate")));
			// 撤销原因
			sealRecord.setRevokereason(dataInfo.get("revocationReason"));
			// 印章数据版本号
			sealRecord.setSealversion(dataInfo.get("version").equals("null") ? "0" : dataInfo.get("version"));
			// 有效开始时间
			sealRecord.setStarttime(
					StringUtils.isBlank(dataInfo.get("validStart")) ? null : sdf.parse(dataInfo.get("validStart")));
			// 有效结束时间
			sealRecord.setStoptime(
					StringUtils.isBlank(dataInfo.get("validEnd")) ? null : sdf.parse(dataInfo.get("validEnd")));
			// 创建时间
			sealRecord.setSealcreatetime(
					StringUtils.isBlank(dataInfo.get("createTime")) ? null : sdf.parse(dataInfo.get("createTime")));
			// 最后更新时间
			sealRecord.setLastupdattime(StringUtils.isBlank(dataInfo.get("lastUpdateTime")) ? null
					: sdf.parse(dataInfo.get("lastUpdateTime")));

			sealRecordDao.save(sealRecord);
		}
	}

	@Override
	public SealRecord selectByPrimaryKey(String id) {

		return sealRecordDao.getOne(id);
	}

	@Override
	public void save(SealRecord sealRecord) {
		sealRecordDao.save(sealRecord);

	}

	@Autowired
	public void setDomainUtil(DomainUtil domainUtil) {
		this.domainUtil = domainUtil;
	}

	@Override
	public SealRecord get(String id) {
		return sealRecordDao.findOne(id);
	}
    @Override
    public int publishAgain(String id,String username){
    	 int i=0;
    	 SealPublishLog publishlog = sealPublishLogDao.findOne(id);
    	 try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("stampID", publishlog.getStampID());
				map.put("stampName", publishlog.getSealname());
				map.put("stampType", String.valueOf(publishlog.getSealtype()));
				map.put("version", publishlog.getVersion());
				map.put("stampState", SealStatus.toStandardMap().get(publishlog.getSealstatus()));
				map.put("revocationDate", sdf.format(publishlog.getQfapprovetime()));
				map.put("revocationReason", publishlog.getApplyreason());
				map.put("validStart", sdf.format(publishlog.getStarttime()));
				map.put("validEnd", sdf.format(publishlog.getStoptime()));
				map.put("createTime", sdf.format(publishlog.getQfapprovetime()));
				// 生成301xml文件
				String xml = domainUtil.genParam("301", publishlog.getId(), map);
				String reStr = ServiceClient.getService(publishlog.getTargetAddress() + "?wsdl").sendData(xml, "301");
				domainUtil.phase(reStr);
				genPublishLog2(publishlog,username, true);
				i++;
			} catch (Exception e) {
				logger.error("印章发布失败", e);
				logger.error("系统名称:{},印章名称:{}", publishlog.getTargetName(), publishlog.getSealname());
				genPublishLog2(publishlog,username, false);

			}
    	 return i;
    }
	private void genPublishLog2(SealPublishLog publishlog, String username,
			boolean success) {
		SealPublishLog sealPublishLog = new SealPublishLog();
		sealPublishLog.setId(BasicTool.getUUID());
		sealPublishLog.setApplyID(publishlog.getApplyID());
		sealPublishLog.setStampID(publishlog.getStampID());
		sealPublishLog.setSealname(publishlog.getSealname());
		sealPublishLog.setSealtype(publishlog.getSealtype());
		sealPublishLog.setVersion(publishlog.getVersion());
		sealPublishLog.setSealstatus(publishlog.getSealstatus());
		sealPublishLog.setQfapprovetime(publishlog.getQfapprovetime());
		sealPublishLog.setApplyreason(publishlog.getApplyreason());
		sealPublishLog.setStarttime(publishlog.getStarttime());
		sealPublishLog.setStoptime(publishlog.getStoptime());
		sealPublishLog.setQfapprovetime(publishlog.getQfapprovetime());
		sealPublishLog.setTargetId(publishlog.getTargetId());
		sealPublishLog.setTargetName(publishlog.getTargetName());
		sealPublishLog.setTargetAddress(publishlog.getTargetAddress());
		sealPublishLog.setSendTime(new Date());
		sealPublishLog.setStatus(success ? 0 : 1);
		sealPublishLog.setUsername(username);
		sealPublishLogDao.save(sealPublishLog);
	}

	@Override
	public int batchPublish(String[] ids, List<SealCenter> relcenters,String username) {
		int i = 0;
		List<SealApply> applys = sealApplyDao.findBySealidInAndAuditresult(ids,AuditResult.QFXT_YES.getKey());
		for (SealCenter center : relcenters) {
			if (publicDate(center, applys,username)) {
				i++;
			}
		}
		return i;
	}

	@Override
	public int autoPublish(SealApply apply, List<SealCenter> relcenters,String username) {
		int i = 0;
		List<SealApply> applys=new ArrayList<SealApply>();
		applys.add(apply);
		for (SealCenter center : relcenters) {
			if (publicDate(center,applys,username)) {
				i++;
			}
		}
		return i;
	}

}
