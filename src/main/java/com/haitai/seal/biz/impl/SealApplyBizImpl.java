package com.haitai.seal.biz.impl;

import java.io.File;
import java.io.IOException;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.haitai.seal.bean.SealApply;
import com.haitai.seal.bean.SealCenter;
import com.haitai.seal.bean.SealLog;
import com.haitai.seal.bean.SealPushData;
import com.haitai.seal.bean.SealPushLog;
import com.haitai.seal.bean.SealRecord;
import com.haitai.seal.biz.CenterBiz;
import com.haitai.seal.biz.SealApplyBiz;
import com.haitai.seal.dao.SealApplyDao;
import com.haitai.seal.dao.SealCenterDao;
import com.haitai.seal.dao.SealLogDao;
import com.haitai.seal.dao.SealPushDataDao;
import com.haitai.seal.dao.SealPushLogDao;
import com.haitai.seal.dao.SealRecordDao;
import com.haitai.seal.domain.SealDataInfo;
import com.haitai.seal.domain.SqInfo;
import com.haitai.seal.enums.AuditResult;
import com.haitai.seal.enums.LogStatus;
import com.haitai.seal.enums.LogType;
import com.haitai.seal.enums.SealStatus;
import com.haitai.seal.enums.SystemType;
import com.haitai.seal.util.BasicTool;
import com.haitai.seal.util.CodecTool;
import com.haitai.seal.util.DomainUtil;
import com.haitai.seal.webService.client.ServiceClient;

@Service("sealApplyBiz")
@Transactional(rollbackFor=Throwable.class)
public class SealApplyBizImpl implements SealApplyBiz {

	@Autowired
	protected SealLogDao sealLogDao;
	@Autowired
	private SealApplyDao sealApplyDao;
	@Autowired
	private SealRecordDao sealRecordDao;
	@Autowired
	private SealCenterDao sealCenterDao;
	@Autowired
	private SealPushDataDao sealPushDataDao;
	@Autowired
	private CenterBiz centerBiz;
	@Autowired
	private SealPushLogDao sealPushLogDao;
	@Autowired
	private DomainUtil domainUtil;

	private static final String SUCCESS_RESULT = "ok";
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean saveFile(String path, String fileName, MultipartFile file) {
		// 判断文件是否为空  
		if (!file.isEmpty()) {  
			try {  
				file.transferTo(new File(path,fileName));  
				return true;  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
		return false;  
	}

	@Override
	public void saveOtherApply(SealApply apply) {
		SealRecord record=sealRecordDao.findOne(apply.getSealid());
		record.setApplystatus(false);
		sealRecordDao.save(record);
		apply.setId(BasicTool.getUUID());
		apply.setPicturedata(record.getPicturedata());
		apply.setPicturehigh(record.getPicturehigh());
		apply.setPicturewide(record.getPicturewide());
		apply.setPicturetype(record.getPicturetype());
		apply.setStarttime(record.getStarttime());
		apply.setStoptime(record.getStoptime());
		apply.setAuditresult(0);
		apply.setApplytime(new Date());
		apply.setSeallist(record.getSeallist());
		apply.setReceivetime(new Date());
		saveApply(apply);
	}

	@Override
	public Page<SealApply> findPage(SealApply apply, Pageable pageable) {
		return sealApplyDao.findAll(getWhereClause(apply),pageable);
	}
	private Specification<SealApply> getWhereClause(final SealApply apply){
		return new Specification<SealApply>() {
			@Override
			public Predicate toPredicate(Root<SealApply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if(apply.getAuditresult()!=null){
					predicate.add(cb.equal(root.get("auditresult").as(Integer.class), apply.getAuditresult()));
				}
				if(apply.getApplyperson()!=null){
					predicate.add(cb.equal(root.get("applyperson"),apply.getApplyperson()));
				}
				if(apply.getSealtype()!=null){
					predicate.add(cb.equal(root.get("sealtype").as(Integer.class), apply.getSealtype()));
				}
				if(StringUtils.isNotBlank(apply.getSealname())){
					predicate.add(cb.like(root.get("sealname").as(String.class), "%"+apply.getSealname()+"%"));
				}
				if(apply.getStarttime()!=null){
					predicate.add(cb.greaterThan(root.get("receivetime").as(Date.class),apply.getStarttime()));
				}
				if(apply.getStoptime()!=null){
					predicate.add(cb.lessThan(root.get("receivetime").as(Date.class),apply.getStoptime()));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
	}

	@Override
	public SealApply get(String id) {
		return sealApplyDao.findOne(id);
	}

	@Override
	public String qfApprove(SealApply sealApply, String username) throws Exception {
		//审核结果
		String result;
		Date now=new Date();
		//印章申请状态结束
		SealRecord record=sealRecordDao.findOne(sealApply.getSealid());
		if(null!=record){
			record.setApplystatus(true);
			sealRecordDao.save(record);
		}
		// 判断是否审核通过
		if (sealApply.getAuditresult() != null
				&& sealApply.getAuditresult().equals(AuditResult.QFXT_YES.getKey())) {
			//返回结果
			result="success";
			//保存申请记录
			sealApply.setAuditresult(AuditResult.QFXT_YES.getKey());
			sealApplyDao.save(sealApply);
			if(sealApply.getSealstatus() != null && sealApply.getSealstatus().equals(SealStatus.MAKE_APPLY)){
				//保存印章记录
				SealRecord sealRecord = new SealRecord();
				BeanUtils.copyProperties(sealApply, sealRecord);
				sealRecord.setId(sealApply.getSealid());
				sealRecord.setSealstatus(SealStatus.MAKE);
				sealRecord.setSealcreatetime(now);
				sealRecord.setLastupdattime(now);
				sealRecord.setReceivetime(now);
				sealRecordDao.save(sealRecord);
			}else{
				SealRecord sealRecord = sealRecordDao.findOne(sealApply.getSealid());
				if (SealStatus.FROZEN_APPLY.equals(sealApply.getSealstatus())) {
					sealRecord.setReceivetime(new Date());
					sealRecord.setSealstatus(SealStatus.FROZEN);
				} else if (SealStatus.UNFROZEN_APPLY.equals(sealApply.getSealstatus())) {
					sealRecord.setReceivetime(new Date());
					sealRecord.setSealstatus(SealStatus.MAKE);
				} else if (SealStatus.CANCEL_APPLY.equals(sealApply.getSealstatus())) {
					sealRecord.setReceivetime(new Date());
					sealRecord.setSealstatus(SealStatus.CANCEL);
				} else {
					throw new Exception("页面传入了错误的状态!");
				}
				sealRecord.setLastupdattime(now);
				sealRecordDao.save(sealRecord);
			}

		} else {
			//返回结果
			// 审核不通过
			//保存申请记录
			result="fail";
			sealApply.setAuditresult(AuditResult.QFXT_NO.getKey());
			sealApplyDao.save(sealApply);
		}
		//返回注册系统的199应答 
		//拼199.xml
		HashMap<String, String> map = new HashMap<String, String>();
		if(AuditResult.QFXT_YES.getKey()==sealApply.getAuditresult()){
			map.put("auditOpinion","1");
		}else{
			map.put("auditOpinion","0");
		}
		map.put("auditContent", sealApply.getQfapprovecontent());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("auditTime", sdf.format(sealApply.getQfapprovetime()));
		String xml = domainUtil.genParam("199", sealApply.getId(),map);
		SealCenter center= centerBiz.findSystemBySystemid(sealApply.getOriginsystemid());
		String reStr = ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(xml, "199");// 998应答
		domainUtil.phase(reStr);
		return result;
	}

	@Override
	public String makeSeal(String stampID) throws Exception {
		// 调用签发系统的接口，提交申请
		Map<String, String> info = new LinkedHashMap<String, String>();
		info.put("stampID", stampID);
		String xml = domainUtil.genParam("105", stampID, info);

		SealCenter center= centerBiz.searchList(1, 2).get(0);
		String seal = ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(xml, "105");
		//解析xml
		SealDataInfo dataInfo = domainUtil.phase(seal);
		String sealStr = dataInfo.getAppData().getDataInfo().get(0).get("stampData");
		return sealStr;
	}

	//向同级签发系统注册
	@Override
	public String reg(String fileStr, String username) throws Exception {
		SealCenter sealcenter= centerBiz.isSystemSearch();
		SqInfo fileStrInfo = domainUtil.zsqphase(fileStr);
		String result=SUCCESS_RESULT;
		//校验和本系统信息是否一致，确认是否是本系统授权文件
		if(!fileStrInfo.getSappData().getESCenterID().equals(sealcenter.getSystemid())){
			result=", 系统标识不一致";
		}
		if(!fileStrInfo.getSappData().getESCenterName().equals(sealcenter.getSystemname())){
			if(result.equals(SUCCESS_RESULT)){
				result="";
			}
			result+=", 系统名称不一致";
		}
		if(!fileStrInfo.getSappData().getValidateCode().equals(sealcenter.getValidatecode())){
			if(result.equals(SUCCESS_RESULT)){
				result="";
			}
			result+=", 注册码不一致";
		}
		if(!(result.equals(SUCCESS_RESULT))){
			addLog(LogType.SEAL_QFSNGN_NO.getKey(), LogStatus.SYSTEM.getKey(), sealcenter.getSystemid(), sealcenter.getSystemname(), username);	
			result="操作失败"+result;
			return result;
		}
		Map<String, String> info = new LinkedHashMap<String, String>();
		info.put("systemID",fileStrInfo.getSappData().getESCenterID());
		info.put("systemName",fileStrInfo.getSappData().getESCenterName());
		info.put("validateCode",fileStrInfo.getSappData().getValidateCode());
		info.put("systemType",String.valueOf(sealcenter.getSystemtype()));
		info.put("serviceAddress",sealcenter.getServiceaddress());
		info.put("deployAddress", sealcenter.getDeployaddress());
		info.put("tel",sealcenter.getTel());
		info.put("linkMan",sealcenter.getLinkman());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		info.put("registerTime",(("null").equals(sealcenter.getRegistertime()) || ("").equals(sealcenter.getRegistertime()) || null == sealcenter.getRegistertime())?null:sdf.format(sealcenter.getRegistertime()));
		info.put("buildOrg",sealcenter.getBuildorg());
		String xml = domainUtil.genParam("501", BasicTool.getUUID(), info);
		//编译子系统授权文件
		List<Map<String,String>> reslist=fileStrInfo.getSappData().getDataInfo();
		String qfServiceAddress=reslist.get(0).get("serviceAddress")+"?wsdl";
		logger.debug("向签发系统注册,签发webservice地址为:{}",qfServiceAddress);
		String seal = ServiceClient.getService(qfServiceAddress).sendData(xml, "501");
		//解析998.xml

		SealDataInfo dataInfo = domainUtil.phase(seal);

		if(dataInfo.getAppData().getDataInfo().get(0).get("status").equals("0")){
			//返回998 更新注册信息
			sealcenter.setSystemid(fileStrInfo.getSappData().getESCenterID()); //更新本系统信息
			sealcenter.setEscenterid(fileStrInfo.getSappData().getDataInfo().get(0).get("systemID"));
			sealcenter.setSystemname(fileStrInfo.getSappData().getESCenterName());
			sealcenter.setValidatecode(fileStrInfo.getSappData().getValidateCode());
			sealCenterDao.save(sealcenter);
			//查询本系统是否有注册过授权文件中的签发系统,将子系统授权书导入系统
			List <SealCenter> valcenters=centerBiz.searchList(1, 2);
			if(!valcenters.isEmpty()){
				//如果当前系统注册过签发系统，就修改记录
				saveSystemMessage(reslist.get(0), valcenters.get(0));
			}else{
				//如果当前系统没有注册签发系统，就新建一条记录
				SealCenter sealCenter=new SealCenter();
				sealCenter.setId(BasicTool.getUUID());
				saveSystemMessage(reslist.get(0), sealCenter);
			}
			addLog(LogType.SEAL_QFSNGN_YES.getKey(), LogStatus.SYSTEM.getKey(), sealcenter.getSystemid(), sealcenter.getSystemname(), username);
		}else{
			addLog(LogType.SEAL_QFSNGN_NO.getKey(), LogStatus.SYSTEM.getKey(), sealcenter.getSystemid(), sealcenter.getSystemname(), username);
		}
		return result;
	}
	private void saveSystemMessage(Map<String, String> dataInfo1, SealCenter sealCenter) {
		//系统标识
		sealCenter.setSystemid(dataInfo1.get("systemID"));
		//系统名称
		sealCenter.setSystemname(dataInfo1.get("systemName"));
		//系统类型
		sealCenter.setSystemtype(Integer.parseInt(dataInfo1.get("systemType")));
		//验证码
		sealCenter.setValidatecode(dataInfo1.get("validateCode"));
		//系统服务地址
		sealCenter.setServiceaddress(dataInfo1.get("serviceAddress"));
		//部署地点
		sealCenter.setDeployaddress(dataInfo1.get("deployAddress"));
		//联系电话
		sealCenter.setTel(dataInfo1.get("tel"));
		//联系人
		sealCenter.setLinkman(dataInfo1.get("linkMan"));
		//注册时间
		sealCenter.setRegistertime(new Date());
		//承建单位
		sealCenter.setBuildorg(dataInfo1.get("buildOrg"));
		//系统证书
		sealCenter.setSystemcertificate(dataInfo1.get("certificate"));
		sealCenter.setEscenterid(dataInfo1.get("systemID"));
		sealCenter.setIssystem("0");
		sealCenterDao.save(sealCenter);
	}
	//向上级中心注册
	@Override
	public void upRegSubmit(String fileStr, String username) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SealCenter> list = centerBiz.searchList(1, 1);
		//构造502.xml
		List<Map<String, String>> dataInfos = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> info = new LinkedHashMap<String, String>();
			info.put("systemName", list.get(i).getSystemname());
			info.put("validateCode", list.get(i).getValidatecode());
			info.put("serviceAddress", list.get(i).getServiceaddress());
			info.put("deployAddress", list.get(i).getDeployaddress());
			info.put("tel", list.get(i).getTel());
			info.put("linkMan", list.get(i).getLinkman());
			info.put("registerTime", (("null").equals(list.get(i).getRegistertime()) || ("").equals(list.get(i).getRegistertime()) || null == list.get(i).getRegistertime()) ? null : sdf.format(list.get(i).getRegistertime()));
			info.put("buildOrg", list.get(i).getBuildorg());
			info.put("systemID", list.get(i).getSystemid());
			info.put("systemType", list.get(i).getSystemtype() + "");
			info.put("ESCenterID", list.get(i).getEscenterid());
			info.put("ESCenterName", list.get(i).getEscentername());
			dataInfos.add(info);
		}

		String xml = domainUtil.genParam("502", BasicTool.getUUID(), dataInfos);

		// 解析导入的中心授权文件
		SqInfo fileStrInfo = domainUtil.zsqphase(fileStr);
		List<Map<String, String>> reslist = fileStrInfo.getSappData().getDataInfo();
		String serviceaddress = "";
		for (Map<String, String> dataInfo : reslist) {// 解析授权文件中的签发系统中的服务器路径
			if (dataInfo.get("systemType").equals("2")) {
				serviceaddress = dataInfo.get("serviceAddress");
				break;
			}
		}
		// 向上级发送502请求,返回998
		String reStr = ServiceClient.getService(serviceaddress+"?wsdl").sendData(xml, "502");// 返回998应答
		// 解析998.xml
		SealDataInfo requestInfo = domainUtil.phase(reStr);
		// 返回值为0，说明接口调用成功，将本级信息成功注册到上级中心
		if ((requestInfo.getAppData().getDataInfo().get(0).get("status")).equals("0")) {
			// 本级签发入库上级 签发、发布系统信息
			for (Map<String, String> dataInfo : reslist) {//将中心文件中信息插入到本系统中
				SealCenter sealCenter = new SealCenter();
				// 系统标识
				sealCenter.setSystemid(dataInfo.get("systemID"));
				// 系统名称
				sealCenter.setSystemname(dataInfo.get("systemName"));
				// 系统类型
				sealCenter.setSystemtype(Integer.parseInt(dataInfo.get("systemType")));
				// 验证码
				sealCenter.setValidatecode(dataInfo.get("validateCode"));
				// 系统服务地址
				sealCenter.setServiceaddress(dataInfo.get("serviceAddress"));
				// 部署地点
				sealCenter.setDeployaddress(dataInfo.get("deployAddress"));
				// 联系电话
				sealCenter.setTel(dataInfo.get("tel"));
				// 联系人
				sealCenter.setLinkman(dataInfo.get("linkMan"));
				// 注册时间
				sealCenter.setRegistertime(("null").equals(dataInfo.get("registerTime")) || dataInfo.get("registerTime") == null || "".equals(dataInfo.get("registerTime")) ? null : sdf.parse(dataInfo.get("registerTime")));
				// 承建单位
				sealCenter.setBuildorg(dataInfo.get("buildOrg"));
				// 系统证书
				sealCenter.setSystemcertificate(dataInfo.get("certificate"));

				sealCenter.setEscenterid(dataInfo.get("systemID"));

				sealCenterDao.save(sealCenter);
			}
			String systemIds = "";
			String[] systemIdArray;
			for (int i = 0; i < reslist.size(); i++) {
				systemIds += reslist.get(i).get("systemID") + ",";
			}
			systemIds = systemIds.substring(0, systemIds.length() - 1);
			systemIdArray = systemIds.split(",");
			// 给本级发布系统发送503.xml
			String reStr1 = create503(systemIdArray);
			// 解析998.xml
			domainUtil.phase(reStr1);
			addLog(LogType.SEAL_ZXSIGN_YES.getKey(), LogStatus.SYSTEM.getKey(), reslist.get(0).get("systemID"), reslist.get(0).get("systemName"), username);
		}else{
			addLog(LogType.SEAL_ZXSIGN_NO.getKey(), LogStatus.SYSTEM.getKey(), reslist.get(0).get("systemID"), reslist.get(0).get("systemName"), username);
		}
	}

	private String create503(String[] systemIdArray) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < systemIdArray.length; i++) {
			values.add(systemIdArray[i]);
		}
		SealCenter center = centerBiz.isSystemSearch();
		Map<String, String> info = new LinkedHashMap<String, String>();
		info.put("systemName", center.getSystemname());
		info.put("validateCode", center.getValidatecode());
		info.put("serviceAddress", center.getServiceaddress());
		info.put("deployAddress", center.getDeployaddress());
		info.put("tel", center.getTel());
		info.put("linkMan", center.getLinkman());
		info.put("registerTime", (("null").equals(center.getRegistertime()) || ("").equals(center.getRegistertime()) || null == center.getRegistertime()) ? null : sdf.format(center.getRegistertime()));
		info.put("buildOrg", center.getBuildorg());
		info.put("systemID", center.getSystemid());
		info.put("systemType", SystemType.SIGN.getLabel() + "");

		String xml = domainUtil.genParam("503", BasicTool.getUUID(), info);
		//查询签章中心中所有的发布、注册系统
		List<SealCenter> resultlist = centerBiz.searchList(1, 1);
		String reStr = "";
		for (SealCenter sealCenter : resultlist) {
			reStr = ServiceClient.getService(sealCenter.getServiceaddress()+"?wsdl").sendData(xml, "503");// 返回998应答
		}

		return reStr;
	}

	@Override
	public String approve(SealApply sealApply, String username) throws Exception {

		String result="";

		if (sealApply.getAuditresult() != null && sealApply.getAuditresult().equals(AuditResult.QFXT_AUDIT.getKey())) {

			result="success";
			// 从注册系统转到签发系统再审批通过101 返回199结果
			if (SealStatus.MAKE_APPLY.equals(sealApply.getSealstatus())) {
				ToQfxtBy101(sealApply.getId(), username);
			} else if (SealStatus.FROZEN_APPLY.equals(sealApply.getSealstatus())) { // 102//
				// 冻结
				ToQfxt(sealApply.getId(), "102",sealApply.getSealid(),username);
			} else if (SealStatus.UNFROZEN_APPLY.equals(sealApply.getSealstatus())) { // 103
				// //
				// 解冻
				ToQfxt(sealApply.getId(), "103",sealApply.getSealid(),username);
			} else if (SealStatus.CANCEL_APPLY.equals(sealApply.getSealstatus())) { // 104//
				// 撤消
				ToQfxt(sealApply.getId(), "104",sealApply.getSealid(),username);
			}
			sealApply.setAuditresult(AuditResult.QFXT_AUDIT.getKey());// 待签发审批
			sealApply.setApproveperson(username);
			sealApply.setAudittime(new Date());
			sealApplyDao.save(sealApply);
		} else { 
			// 不通过
			result="fail";
			sealApply.setAuditresult(AuditResult.REG_NO.getKey());
			sealApply.setApproveperson(username);
			sealApplyDao.save(sealApply);
			//如果不通过，代表印章的申请状态结束
			SealRecord record=sealRecordDao.findOne(sealApply.getSealid());
			if(null!=record){
				record.setApplystatus(true);
				sealRecordDao.save(record);
			}
		}
		return result;
	}

	private void ToQfxt(String id, String type, String sealid,String username) throws Exception {
		//如果通过,代表印章的申请状态未完结
		SealRecord record=sealRecordDao.findOne(sealid);
		if(null!=record){
			record.setApplystatus(false);
			sealRecordDao.save(record);
		}
		String xml = domainUtil.genParam(type,BasicTool.getUUID(), updataSealStatus(id,username));
		logger.debug("请求{}",type);
		// 保存到签发系统中 通过签发系统审批
		// 处理101xml 并把数据保存到签发系统中
		SealCenter center = centerBiz.searchList(1, 2).get(0);
		String re998 = ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(xml, type);
		logger.debug("应答{}",type);
		domainUtil.phase(re998);
	}

	private Map<String, String> updataSealStatus(String id,String username) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SealApply tempSealRecord = sealApplyDao.getOne(id);
		Map<String, String> map = new HashMap<String, String>();
		map.put("publishSystemID", "");
		map.put("originSystemID", "");
		map.put("stampID", tempSealRecord.getSealid());
		map.put("stampName", tempSealRecord.getSealname());
		map.put("certList", tempSealRecord.getSeallist());
		map.put("stampOrg", tempSealRecord.getSealdept());
		map.put("applyReason", tempSealRecord.getApplyreason());
		map.put("applyPerson", tempSealRecord.getApplyperson());
		map.put("tel", tempSealRecord.getTelephone());
		map.put("applyDate", (("null").equals(tempSealRecord.getApplytime()) || ("").equals(tempSealRecord.getApplytime()) || null == tempSealRecord.getApplytime()) ? null : sdf.format(tempSealRecord.getApplytime()));
		map.put("applyNote", tempSealRecord.getRemark());
		map.put("applyAttach", tempSealRecord.getApplyfile());
		map.put("operator", username); // 承办人
		map.put("appID", tempSealRecord.getId());
		return map;
	}

	private void ToQfxtBy101(String id, String username) throws Exception {

		String xml = domainUtil.genParam("101",BasicTool.getUUID(), get101List(id, username));

		logger.debug("请求101");
		// 保存到签发系统中 通过签发系统审批
		// 处理101xml 并把数据保存到签发系统中
		SealCenter center = centerBiz.searchList(1, 2).get(0);
		String re998 = ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(xml, "101");
		logger.debug("应答101");
		domainUtil.phase(re998);
	}

	private Map<String, String> get101List(String id, String username) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SealApply tempSealRecord = sealApplyDao.getOne(id);
		Map<String, String> map = new HashMap<String, String>();
		map.put("publishSystemID", "");
		map.put("originSystemID", "");
		map.put("stampID", tempSealRecord.getSealid());
		map.put("stampName", tempSealRecord.getSealname());
		map.put("stampType", String.valueOf(tempSealRecord.getSealtype()));
		map.put("pictureType", String.valueOf(tempSealRecord.getPicturetype()));
		map.put("pictureData", tempSealRecord.getPicturedata());
		map.put("pictureHeight", String.valueOf(tempSealRecord.getPicturehigh()));
		map.put("pictureWidth", String.valueOf(tempSealRecord.getPicturewide()));
		map.put("validStart", (("null").equals(tempSealRecord.getStarttime()) || ("").equals(tempSealRecord.getStarttime()) || null == tempSealRecord.getStarttime()) ? null : sdf.format(tempSealRecord.getStarttime()));
		map.put("validEnd", (("null").equals(tempSealRecord.getStoptime()) || ("").equals(tempSealRecord.getStoptime()) || null == tempSealRecord.getStoptime()) ? null : sdf.format(tempSealRecord.getStoptime()));
		map.put("certList", tempSealRecord.getSeallist());
		map.put("stampOrg", tempSealRecord.getSealdept());
		map.put("applyReason", tempSealRecord.getApplyreason());
		map.put("applyPerson", tempSealRecord.getApplyperson());
		map.put("tel", tempSealRecord.getTelephone());
		map.put("applyDate", (("null").equals(tempSealRecord.getApplytime()) || ("").equals(tempSealRecord.getApplytime()) || null == tempSealRecord.getApplytime()) ? null : sdf.format(tempSealRecord.getApplytime()));
		map.put("applyNote", tempSealRecord.getRemark());
		map.put("applyAttach",tempSealRecord.getApplyfile());
		map.put("operator", username);
		map.put("appID", tempSealRecord.getId());
		return map;
	}
	/*对附件编码*/
	protected String getAttachEncodeBufferBase64(String fName, String id, String path) throws IOException {
		String suffixName = "." + fName.substring(fName.lastIndexOf(".") + 1);
		String AttachName = id + suffixName;
		File file = new File(path, AttachName);
		byte[] bytes = FileUtils.readFileToByteArray(file);
		return  CodecTool.encodeBufferBase64(bytes)+suffixName;
	}

	@Override
	public void process199(SealDataInfo sealDataInfo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> data = sealDataInfo.getAppData().getDataInfo().get(0);
		//印章申请状态结束
		String sealid=sealApplyDao.getOne(sealDataInfo.getAppData().getAppID()).getSealid();
		SealRecord record = sealRecordDao.findOne(sealid);
		if(null!=record){
			record.setApplystatus(true);
			sealRecordDao.save(record);
		}
		if (data.get("auditOpinion").equals("1")) {
			SealApply sealapp = sealApplyDao.getOne(sealDataInfo.getAppData().getAppID());
			// 同意 更新状态
			sealapp.setAuditresult(4);// 审批完成
			sealapp.setQfapprovecontent(data.get("auditContent"));
			sealapp.setQfapprovetime(sdf.parse(data.get("auditTime")));
			sealapp.setReceivetime(new Date());
			// 判断是否为其他申请
			if (SealStatus.MAKE_APPLY.equals(sealapp.getSealstatus())) { // 印章申请
				savaAppAndRecord(  sealapp, SealStatus.MAKE);
			} else if (SealStatus.UNFROZEN_APPLY.equals(sealapp.getSealstatus())) { // 解冻
				updataStatus(sealapp, SealStatus.MAKE);
			} else if (SealStatus.CANCEL_APPLY.equals(sealapp.getSealstatus())) { // 撤消
				updataStatus(sealapp, SealStatus.CANCEL);
			} else { // 冻结
				updataStatus(sealapp, SealStatus.FROZEN);
			}
		} else {
			// 不同意 更新状态
			SealApply sealapp = sealApplyDao.getOne(sealDataInfo.getAppData().getAppID());
			sealapp.setAuditresult(AuditResult.QFXT_NO.getKey());// 审批完成
			sealapp.setQfapprovecontent(data.get("auditContent"));
			sealapp.setQfapprovetime(sdf.parse(data.get("auditTime")));
			sealapp.setReceivetime(new Date());
			sealApplyDao.save(sealapp);
		}

	}

	private void savaAppAndRecord(SealApply sealapp, SealStatus status) throws ParseException {
		sealApplyDao.save(sealapp);
		SealRecord sealRecord = new SealRecord();
		BeanUtils.copyProperties(sealapp, sealRecord);
		sealRecord.setId(sealapp.getId());
		sealRecord.setSealstatus(status);
		sealRecord.setSealcreatetime(sealapp.getQfapprovetime());
		sealRecord.setLastupdattime(sealapp.getQfapprovetime());
		sealRecord.setApplystatus(true);
		sealRecordDao.save(sealRecord);
	}

	private void updataStatus( SealApply sealapp, SealStatus status) throws ParseException {
		sealApplyDao.save(sealapp);
		SealRecord record = sealRecordDao.getOne(sealapp.getSealid());
		record.setSealstatus(status);
		record.setLastupdattime(sealapp.getQfapprovetime());
		sealRecordDao.save(record);
	}


	/**
	 * 系统操作日志(记录在数据库中)
	 * 
	 * @param type
	 *            LogType枚举类型
	 * @param status
	 *            系统或印章
	 * @param objid
	 *            操作记录的id
	 * @param objname
	 *            操作记录名称
	 * @param userName
	 *            操作用户登录用户名
	 */
	public void addLog(int status, int type, String objid, String objname, String userName) {
		SealLog log = new SealLog();
		log.setId(BasicTool.getUUID());
		log.setType(type);
		log.setObjectid(objid);
		log.setCreatetime(new Date());
		log.setUsername(userName);
		log.setObjectname(objname);
		log.setStatus(status);
		sealLogDao.save(log);
	}
    public List<SealApply> findByStartAndStop(String start, final String stop) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date starttime = sdf.parse(start);
        Specification<SealApply> param=new Specification<SealApply>() {
            @Override
            public Predicate toPredicate(Root<SealApply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

				predicate.add(
						cb.and(
								cb.greaterThanOrEqualTo(root.get("starttime").as(Date.class), starttime),
								cb.lessThanOrEqualTo(root.get("stoptime").as(Date.class), starttime)
								)
						);

				if(StringUtils.isBlank(stop)){
					Date stoptime;
					try {
						stoptime = sdf.parse(stop);
						predicate.add(cb.lessThanOrEqualTo(root.get("qfapprovetime").as(Date.class), stoptime));
						predicate.add(
								cb.and(
										cb.greaterThanOrEqualTo(root.get("starttime").as(Date.class), stoptime),
										cb.lessThanOrEqualTo(root.get("stoptime").as(Date.class), stoptime)
										)
								);
					} catch (ParseException e) {
						logger.warn("结束时间参数转换错误",e);
					}
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return sealApplyDao.findAll(param);
	}

	@Override
	public Page<SealLog> findPageBylog(Pageable pageable) {
		return sealLogDao.findAll(pageable);
	}

	@Override
	public void saveApply(SealApply sealApply) {
		sealApplyDao.save(sealApply);
	}

	@Override
	public List<SealApply> findBySealid(String id) {
		return sealApplyDao.findBySealidOrderByQfapprovetimeDesc(id);
	}


	//定时推送印章
	@Override
	public void timerTask() throws Exception {
		//下级往上级推送
		List<SealPushData> upPushDataList = sealPushDataDao.findByispushAndSealcenter("0", "1");
		for (SealPushData sealPushData : upPushDataList) {

			String upPushDataXML = domainUtil.genParam("401", sealPushData.getId(), gen401Param(sealPushData));
			SealCenter center = centerBiz.searchParent();
			if(center == null) {
				pushDown(sealPushData,upPushDataXML);
			} else {
				push(sealPushData,upPushDataXML, center,"上级签发");
			}
			sealPushData.setIspush("1");
			sealPushDataDao.save(sealPushData);
		}
		//上级往下级推送
		List<SealPushData> downPushDataList = sealPushDataDao.findByispushAndSealcenter("0", "0");
		for (SealPushData sealPushData : downPushDataList) {
			String downPushDataXML = domainUtil.genParam("401", sealPushData.getId(), gen401Param(sealPushData));
			pushDown(sealPushData,downPushDataXML);
			//推送完之后设置ispush为1------0:未推送;1:已推送
			sealPushData.setIspush("1");
			sealPushDataDao.save(sealPushData);
		}
	}

	/**
	 * 推
	 * @param pushData
	 * @param pushDataXML
	 * @param center
	 * @param target 推送目标
	 */
	private void push(SealPushData pushData,String pushDataXML, SealCenter center,String target) {
		try {
			ServiceClient.getService(center.getServiceaddress()+"?wsdl").sendData(pushDataXML, "401");
		} catch (Exception e) {
			SealPushLog log=new SealPushLog();
			log.setId(BasicTool.getUUID());
			log.setStatus(1);
			log.setTarget(target);
			log.setTargetName(center.getSystemname());
			log.setTargetAddress(center.getServiceaddress());
			log.setCreatetime(new Date());
			log.getSealPushDatas().add(pushData);
			sealPushLogDao.save(log);
			logger.debug("向上级签发推送失败！");
		}
	}

	/**
	 * 向下推
	 * @param pushData
	 * @param upPushDataXML
	 */
	private void pushDown(SealPushData pushData,String upPushDataXML) {
		List<SealCenter> relCenter = centerBiz.searchList(1, 1);
		for (SealCenter sealCenter : relCenter) {
			push(pushData,upPushDataXML,sealCenter,"本级发布");
		}
		List<SealCenter> valCenterList = centerBiz.searchList(2, 2);
		for (SealCenter sealCenter : valCenterList) {
			push(pushData,upPushDataXML,sealCenter,"下级签发");
		}
	}

	/**
	 * 生成要推送的数据
	 * @param upPushData
	 * @return
	 */
	private Map<String, String> gen401Param(SealPushData upPushData) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, String> info = new LinkedHashMap<String, String>();
		info.put("publishSystemID", upPushData.getPublishsystemid());
		info.put("originSystemID", upPushData.getOriginsystemid());
		info.put("stampID", upPushData.getStampID());
		info.put("stampName", upPushData.getSealname());
		info.put("stampType", String.valueOf(upPushData.getSealtype()));
		info.put("stampState", String.valueOf(upPushData.getSealstatus()));
		info.put("revocationDate", (("null").equals(upPushData.getQfapprovetime()) || ("").equals(upPushData.getQfapprovetime()) || null == upPushData.getQfapprovetime()) ? null : sdf.format(upPushData.getQfapprovetime()));
		info.put("revocationReason", upPushData.getApplyreason());
		info.put("version", upPushData.getVersion());
		info.put("validStart", (("null").equals(upPushData.getStarttime()) || ("").equals(upPushData.getStarttime()) || null == upPushData.getStarttime()) ? null : sdf.format(upPushData.getStarttime()));
		info.put("validEnd", (("null").equals(upPushData.getStoptime()) || ("").equals(upPushData.getStoptime()) || null == upPushData.getStoptime()) ? null : sdf.format(upPushData.getStoptime()));
		info.put("createTime", (("null").equals(upPushData.getApplytime()) || ("").equals(upPushData.getApplytime()) || null == upPushData.getApplytime()) ? null : sdf.format(upPushData.getApplytime()));
		return info;
	}

    @Override
    public Page<SealPushLog> findPushFailLog(final SealPushLog pushLog, Pageable pageable) {
        Specification<SealPushLog> param=new Specification<SealPushLog>() {
            @Override
            public Predicate toPredicate(Root<SealPushLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if(pushLog.getStatus()!=null){
                    predicate.add(cb.equal(root.get("status").as(Integer.class), pushLog.getStatus()));
                }
                if(StringUtils.isNotBlank(pushLog.getTarget())){
                    predicate.add(cb.equal(root.get("target"), pushLog.getTarget()));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
        return sealPushLogDao.findAll(param, pageable);
    }

    @Override
    public Page<SealPushData> findPushFailDetailLog(final String pushLogId, final SealPushData pushData, Pageable pageable) {
        Specification<SealPushData> param=new Specification<SealPushData>() {
            @Override
            public Predicate toPredicate(Root<SealPushData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                Join<SealPushData,SealPushLog> join =  root.join(root.getModel().getSet("sealPushLogs",SealPushLog.class),JoinType.LEFT);
                predicate.add(cb.equal(join.get("id"), pushLogId));
                if(pushData.getSealtype()!=null){
                    predicate.add(cb.equal(root.get("sealtype").as(Integer.class), pushData.getSealtype()));
                }
                if(pushData.getSealtype()!=null){
                    predicate.add(cb.equal(root.get("sealtype").as(Integer.class), pushData.getSealtype()));
                }
                if(StringUtils.isNotBlank(pushData.getSealname())){
                    predicate.add(cb.like(root.get("sealname").as(String.class), "%"+pushData.getSealname()+"%"));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
        return sealPushDataDao.findAll(param, pageable);
    }

	@Override
	public boolean hasSealName(String sealname) {
		List<SealApply> applys=sealApplyDao.findBySealname(sealname);
		return applys.isEmpty()?false:true;
	}

}
