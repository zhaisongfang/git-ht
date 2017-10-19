package com.haitai.seal.biz.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haitai.seal.bean.SealCenter;
import com.haitai.seal.bean.SealPushData;
import com.haitai.seal.biz.BaseBiz;
import com.haitai.seal.biz.CenterBiz;
import com.haitai.seal.biz.SealApplyBiz;
import com.haitai.seal.component.Config;
import com.haitai.seal.dao.SealCenterDao;
import com.haitai.seal.dao.SealPushDataDao;
import com.haitai.seal.domain.SealDataInfo;
import com.haitai.seal.domain.Signature;
import com.haitai.seal.domain.SqAppData;
import com.haitai.seal.domain.SqInfo;
import com.haitai.seal.enums.LogStatus;
import com.haitai.seal.enums.LogType;
import com.haitai.seal.enums.SystemType;
import com.haitai.seal.util.BasicTool;
import com.haitai.seal.util.DomainUtil;

@Service("centerBiz")
@Transactional(rollbackFor=Throwable.class)
public class CenterBizImpl extends BaseBiz implements CenterBiz{

	@Autowired
	private Config config;

	@Autowired
	private SealCenterDao sealCenterDao;
	
	@Autowired
    private SealPushDataDao sealPushDataDao;

	@Autowired
	private SealApplyBiz sealApplyBiz;

	@Autowired
	private DomainUtil domainUtil;

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Page<SealCenter> findPage(Pageable pageable) {
		return sealCenterDao.findAll(pageable);
	}
	
	@Override
	public void saveUpperSign(SealDataInfo sealDataInfo) throws ParseException {

		for(int i=0;i<sealDataInfo.getAppData().getDataInfo().size();i++){
			Map<String, String> dataInfo=sealDataInfo.getAppData().getDataInfo().get(i);
			SealCenter sealCenter=new SealCenter();
			//系统标识
			sealCenter.setSystemid(dataInfo.get("systemID"));
			//系统名称
			sealCenter.setSystemname(dataInfo.get("systemName"));
			//系统类型
			sealCenter.setSystemtype(Integer.parseInt(dataInfo.get("systemType")));
			//验证码
			sealCenter.setValidatecode(dataInfo.get("validateCode"));;
			//系统服务地址
			sealCenter.setServiceaddress(dataInfo.get("serviceAddress"));
			//部署地点
			sealCenter.setDeployaddress(dataInfo.get("deployAddress"));
			//联系电话
			sealCenter.setTel(dataInfo.get("tel"));
			//联系人
			sealCenter.setLinkman(dataInfo.get("linkMan"));
			//注册时间
			sealCenter.setRegistertime(("null").equals(dataInfo.get("registerTime"))||dataInfo.get("registerTime")==null || "".equals(dataInfo.get("registerTime")) ?null:sdf.parse(dataInfo.get("registerTime")));
			//承建单位
			sealCenter.setBuildorg(dataInfo.get("deployAddress"));
			//系统证书
			sealCenter.setSystemcertificate(dataInfo.get("deployAddress"));

			sealCenter.setEscenterid(dataInfo.get("systemID"));

			sealCenterDao.save(sealCenter);
		}
	}
	@Override
	public String downChildSystemFile(String id, String username) throws Exception {
		SealCenter regCenter=sealCenterDao.getOne(id);

		SqInfo info=new SqInfo();
		SqAppData appData=new SqAppData();
		appData.setType(SqAppData.TYPE_LEVEL_SAME);
		appData.setESCenterID(regCenter.getSystemid());
		appData.setESCenterName(regCenter.getSystemname());
		appData.setValidateCode(regCenter.getValidatecode());
		List<Map<String, String>> dataInfos =new ArrayList<Map<String,String>>();
		appData.setDataInfo(dataInfos);

		Map<String, String> dataInfo = new LinkedHashMap<String, String>();
		dataInfos.add(dataInfo);

		SealCenter valCenter=isSystemSearch();
		dataInfo.put("systemID", valCenter.getSystemid());
		dataInfo.put("systemName", valCenter.getSystemname());
		dataInfo.put("systemType", String.valueOf(valCenter.getSystemtype()));
		dataInfo.put("serviceAddress",valCenter.getServiceaddress());
		dataInfo.put("deployAddress",valCenter.getDeployaddress());
		dataInfo.put("tel",valCenter.getTel());
		dataInfo.put("linkMan",valCenter.getLinkman());
		dataInfo.put("buildOrg",valCenter.getBuildorg());
		dataInfo.put("certificate",valCenter.getSystemcertificate());	


		Signature signature=domainUtil.genSignature(appData);
		info.setSappData(appData);
		info.setSignature(signature);
		return info.toRegOrRelString();
	}
	@Override
	public String downCenterSystem(String id, String username) throws Exception {
		SealCenter regCenter=sealCenterDao.getOne(id);
		SqInfo info=new SqInfo();
		SqAppData appData=new SqAppData();
		appData.setType(SqAppData.TYPE_LEVEL_CHILD);
		appData.setESCenterID(regCenter.getSystemid());
		appData.setESCenterName(regCenter.getSystemname());
		appData.setValidateCode(regCenter.getValidatecode());
		List<Map<String, String>> dataInfos =new ArrayList<Map<String,String>>();
		appData.setDataInfo(dataInfos);

		Map<String, String> valData = new LinkedHashMap<String, String>();
		dataInfos.add(valData);

		SealCenter valCenter=isSystemSearch();
		valData.put("systemID", valCenter.getSystemid());
		valData.put("systemName", valCenter.getSystemname());
		valData.put("systemType", String.valueOf(valCenter.getSystemtype()));
		valData.put("serviceAddress",valCenter.getServiceaddress());
		valData.put("deployAddress",valCenter.getDeployaddress());
		valData.put("tel",valCenter.getTel());
		valData.put("linkMan",valCenter.getLinkman());
		valData.put("buildOrg",valCenter.getBuildorg());
		valData.put("certificate",valCenter.getSystemcertificate());	
        
		
		List<SealCenter> relList=searchList(1, 1);
		for(SealCenter relCenter1:relList){
			Map<String, String> relData = new LinkedHashMap<String, String>();
			relData.put("systemID", relCenter1.getSystemid());
			relData.put("systemName", relCenter1.getSystemname());
			relData.put("systemType", String.valueOf(relCenter1.getSystemtype()));
			relData.put("serviceAddress",relCenter1.getServiceaddress());
			relData.put("deployAddress",relCenter1.getDeployaddress());
			relData.put("tel",relCenter1.getTel());
			relData.put("linkMan",relCenter1.getLinkman());
			relData.put("buildOrg",relCenter1.getBuildorg());
			relData.put("certificate",relCenter1.getSystemcertificate());	
			dataInfos.add(relData);
		}

		Signature signature=domainUtil.genSignature(appData);

		info.setSappData(appData);
		info.setSignature(signature);

		return info.toString();
	}

	@Override
	public void regORrelToval(SealDataInfo sealDataInfo) throws ParseException {
		Map<String, String> dataInfo = sealDataInfo.getAppData().getDataInfo().get(0);

		SealCenter sealCenter= sealCenterDao.findBySystemid(dataInfo.get("systemID"));
		//系统标识
		sealCenter.setSystemid(dataInfo.get("systemID"));
		//系统名称
		sealCenter.setSystemname(dataInfo.get("systemName"));
		//系统类型
		sealCenter.setSystemtype(Integer.parseInt(dataInfo.get("systemType")));
		//验证码
		sealCenter.setValidatecode(dataInfo.get("validateCode"));;
		//系统服务地址
		sealCenter.setServiceaddress(dataInfo.get("serviceAddress"));
		//部署地点
		sealCenter.setDeployaddress(dataInfo.get("deployAddress"));
		//联系电话
		sealCenter.setTel(dataInfo.get("tel"));
		//联系人
		sealCenter.setLinkman(dataInfo.get("linkMan"));
		//注册时间
		sealCenter.setRegistertime(("null").equals(dataInfo.get("registerTime"))||dataInfo.get("registerTime")==null || "".equals(dataInfo.get("registerTime")) ?null:sdf.parse(dataInfo.get("registerTime")));
		//承建单位
		sealCenter.setBuildorg(dataInfo.get("buildOrg"));
		//系统证书
		sealCenter.setSystemcertificate(dataInfo.get("certificate"));

		sealCenterDao.save(sealCenter);
	}
	@Override
	public void syncData502(List<Map<String, String>> dataInfos) throws ParseException {
		for (Map<String, String> dataInfo : dataInfos) {
			SealCenter sealCenter=new SealCenter();
			sealCenter.setId(BasicTool.getUUID());
			//系统标识
			sealCenter.setSystemid(dataInfo.get("systemID"));
			//系统名称
			sealCenter.setSystemname(dataInfo.get("systemName"));
			//系统类型
			sealCenter.setSystemtype(Integer.parseInt(dataInfo.get("systemType")));
			//验证码
			sealCenter.setValidatecode(dataInfo.get("validateCode"));
			//系统服务地址
			sealCenter.setServiceaddress(dataInfo.get("serviceAddress"));
			//部署地点
			sealCenter.setDeployaddress(dataInfo.get("deployAddress"));
			//联系电话
			sealCenter.setTel(dataInfo.get("tel"));
			//联系人
			sealCenter.setLinkman(dataInfo.get("linkMan"));
			//注册时间
			sealCenter.setRegistertime(("null").equals(dataInfo.get("registerTime"))||dataInfo.get("registerTime")==null || "".equals(dataInfo.get("registerTime")) ?null:sdf.parse(dataInfo.get("registerTime")));
			//承建单位
			sealCenter.setBuildorg(dataInfo.get("deployAddress"));
			//系统证书
			sealCenter.setSystemcertificate(dataInfo.get("deployAddress"));
			sealCenterDao.save(sealCenter);
		}
	}
	@Override
	public List<SealCenter> searchList(int isCenter, String systemID, int type) {
		switch (isCenter) {
		case 1://同级中心
			return sealCenterDao.findByEscenteridAndSystemtypeAndServiceaddressIsNotNull(systemID,type);
		case 2://下级
			return sealCenterDao.findByParentid(systemID);
		default:
			break;
		}
		return null;
	}

    
	@Override
    public List<SealCenter> searchList(int isCenter, int type) {
    	return  searchList(isCenter,isSystemSearch().getEscenterid(),type);
    }
	
	@Override
	public SealCenter searchParent(String systemID) {//上级
		String upId = sealCenterDao.findByEscenteridAndSystemtypeAndServiceaddressIsNotNull(systemID, 2).get(0).getParentid();
		return sealCenterDao.findBySystemid(upId);
	}
	@Override
	public SealCenter searchParent(){
		return searchParent(isSystemSearch().getEscenterid());
	}
	@Override
	public SealCenter isSystemSearch() {
		return sealCenterDao.findByIssystem("1");
	}
	@Override
	public void saveSystem(SealCenter sealCenter,String userName) {
		sealCenterDao.save(sealCenter);
	}
	@Override
	public void delSystem(String id,String userName) {
		sealCenterDao.deleteByid(id);
	}
    @Override
    public SealCenter queryListByID(String systemid) {
        return sealCenterDao.findBySystemid(systemid);
    }
	@Override
	public SealCenter findSystemById(String id) {
		 return sealCenterDao.getOne(id);
	}
	@Override
	public void sendToVal(SealDataInfo requestInfo) throws Exception {
		//解析XML
    	SealCenter sealCenter = searchParent();
    	//保存数据到本地，并设置ispush为0------0:未推送;1:已推送
    	for (Map<String, String> dataInfo : requestInfo.getAppData().getDataInfo()) {
    		SealPushData sealPushData = new SealPushData();
    		sealPushData.setId(requestInfo.getAppData().getAppID());
    		sealPushData.setPublishsystemid(dataInfo.get("publishsystemid"));
    		sealPushData.setOriginsystemid(dataInfo.get("originsystemid"));
    		sealPushData.setStampID(dataInfo.get("stampID"));
    		sealPushData.setSealname(dataInfo.get("stampName"));
    		sealPushData.setSealtype(Integer.valueOf(dataInfo.get("stampType")));
    		sealPushData.setSealstatus(Integer.valueOf(dataInfo.get("stampState")));
    		sealPushData.setQfapprovetime(sdf.parse(dataInfo.get("revocationDate")));
    		sealPushData.setApplyreason(dataInfo.get("revocationReason"));
    		sealPushData.setVersion(dataInfo.get("version"));
    		sealPushData.setStarttime(sdf.parse(dataInfo.get("validStart")));
    		sealPushData.setStoptime(sdf.parse(dataInfo.get("validEnd")));
    		sealPushData.setApplytime(sdf.parse(dataInfo.get("createTime")));
    		sealPushData.setIspush("0");
    		if(sealCenter != null) {//下级往上级推送
    			sealPushData.setSealcenter("1");
    		}else{//上级往下级推送
    			sealPushData.setSealcenter("0");
    		}
			
			sealPushDataDao.save(sealPushData);
		}
	}

    @Override
    public SealCenter findSystemBySystemid(String systemid) {
        
      return  sealCenterDao.findBySystemid(systemid);
    
    }

    @Override
    public SealCenter findSystemBySystemname(String systemname) {
        
        return  sealCenterDao.findBySystemname(systemname);
        
    }

    @Override
    public boolean findNoRepatBySystemid(String id,String systemid) {
        
        return sealCenterDao.findByIdNotAndSystemid(id,systemid)==null?false:true;
    }

    @Override
    public boolean findNoRepatBySystemname(String id,String systemname) {
        
        return sealCenterDao.findByIdNotAndSystemname(id,systemname)==null?false:true;
    }

}
