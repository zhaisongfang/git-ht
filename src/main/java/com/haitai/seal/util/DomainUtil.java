package com.haitai.seal.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haitai.seal.biz.CenterBiz;
import com.haitai.seal.component.Config;
import com.haitai.seal.domain.AppData;
import com.haitai.seal.domain.SealDataInfo;
import com.haitai.seal.domain.Signature;
import com.haitai.seal.domain.SqAppData;
import com.haitai.seal.domain.SqInfo;
import com.haitai.seal.sign.service.SignatureService;

@Component
public class DomainUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Config config;

	@Autowired
	private CenterBiz centerbiz;

	@Autowired
	private SignatureService signatureService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 通过字符串解析出请求对象,在过程中进行验签
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public SealDataInfo phase(String str) throws Exception {

		// 解码
		String xml = CodecTool.decodeStr(str);

		SealDataInfo info = new SealDataInfo();
		// dataType
		info.setDataType(XmlPhaser.getProperty(xml, "dataType"));
		logger.debug("dataType:{}", info.getDataType());

		// appData
		AppData appData = new AppData();
		String appDataStr = XmlPhaser.getSubString(xml, "appData");
		appData.setAppID(XmlPhaser.getProperty(appDataStr, "appID"));
		appData.setDataTime(XmlPhaser.getProperty(appDataStr, "dataTime"));
		appData.setVersion(XmlPhaser.getProperty(appDataStr, "version"));
		appData.setSystemID(XmlPhaser.getProperty(appDataStr, "systemID"));
		List<Map<String, String>> dataInfo = getDataInfos(info.getDataType(), appDataStr);
		appData.setDataInfo(dataInfo);
		info.setAppData(appData);

		// signature
		Signature signature = new Signature();
		String signatureStr = XmlPhaser.getSubString(xml, "signature");
		signature.setCertificate(XmlPhaser.getProperty(signatureStr, "certificate"));
		signature.setOID(XmlPhaser.getProperty(signatureStr, "OID"));
		signature.setSignatureValue(XmlPhaser.getProperty(signatureStr, "signatureValue"));

		info.setSignature(signature);

		// 验签
		if (!validate(info.getAppData().toString(),info.getSignature())) {
			throw new Exception("验签失败");
		}

		return info;
	}

	/**
	 * 验签(debug模式不验)
	 * 
	 * @param info
	 * @return
	 * @throws Exception
	 */
	protected boolean validate(String source,Signature signature) throws Exception {
		if (config.isDebug()) {
			return true;
		}
		String certificate = signature.getCertificate();
		String signatureValue = signature.getSignatureValue();
		if ("true".equals(config.getDeleteSpace())) {
			source = source.replaceAll("\\s*", "");
		}
		logger.debug("验签-明文:{}",source);
		logger.debug("验签-证书:{}",certificate);
		logger.debug("验签-签名值:{}",signatureValue);
		return signatureService.verify(source,certificate,signatureValue);
	}

	/**
	 * 签名
	 * 
	 * @param info
	 * @throws Exception
	 */
	protected void digest(SealDataInfo info) throws Exception {
		String source = info.getAppData().toString();
		logger.debug("签名-明文:{}",source);
		String signValue = digest(source);
		logger.debug("签名-签名值:{}",signValue);
		info.getSignature().setSignatureValue(signValue);
	//	boolean x=signatureService.verify(source,"MIIBajCCARGgAwIBAgIFAIu3iJ8wCgYIKoEcz1UBg3UwQTELMAkGA1UEAwwCQ0ExFTATBgNVBAsMDOa1t+azsOaWueWchjEbMBkGA1UECgwS5a6J5YWo56CU5Y+R5Lit5b+DMB4XDTE1MTEwMzA4NTY0MFoXDTE2MTEwMzA4NTY0MFowQzENMAsGA1UEAwwEdXNlcjEVMBMGA1UECwwM5rW35rOw5pa55ZyGMRswGQYDVQQKDBLlronlhajnoJTlj5HkuK3lv4MwTzAJBgcqhkjOPQIBA0IABGkbcmDX28pP6WueKH/qe2GC00Qhh/nslI5fSHrp8fstS9jpah8a/urc3/RKp/JyePsapI6WWmS5CHbAZukKriwwCgYIKoEcz1UBg3UDRwAwRAIgZwt4BfOQXWfD4AGQ7d40YhMWaU2fhN6whi9gJ6B5H1ACIAEM0G3+VZUlxANtsUf3X+Yf22piZvNVFhJJwtKn6HJF",signValue);
	}

	private String digest(String source) throws Exception {
		if ("true".equals(config.getDeleteSpace())) {
			source = source.replaceAll("\\s*", "");
		}
		return signatureService.sign(source);
	}

	/**
	 * 生成webservice参数
	 * 
	 * @param dataType
	 * @param appId
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public String genParam(String dataType, String appId, Map<String, String> info) throws Exception {

		List<Map<String, String>> dataInfos = new ArrayList<Map<String, String>>();
		dataInfos.add(info);

		return genParam(dataType, appId, dataInfos);
	}

	/**
	 * 生成webservice参数
	 * 
	 * @param dataType
	 * @param appId
	 * @param dataInfos
	 * @return
	 * @throws Exception
	 */
	public String genParam(String dataType, String appId, List<Map<String, String>> dataInfos) throws Exception {

		SealDataInfo responseInfo = new SealDataInfo();

		responseInfo.setDataType(dataType);
		responseInfo.setAppData(genAppData(appId, dataInfos));
		responseInfo.setSignature(genSignature());

		// 签名
		if (!config.isDebug()) {
			digest(responseInfo);
		}
		// 编码
		return CodecTool.encodeStr(responseInfo.toString());
	}

	/**
	 * 生成appData
	 * 
	 * @param dataInfo
	 * @return
	 */
	protected AppData genAppData(String appId, List<Map<String, String>> dataInfo) {
		AppData result = new AppData();
		result.setAppID(appId);
		result.setDataTime(sdf.format(new Date()));
		result.setVersion(config.getVersion());
		result.setSystemID(centerbiz.isSystemSearch().getSystemid());
		result.setDataInfo(dataInfo);
		return result;
	}

	public Signature genSignature() {
		Signature signature = new Signature();
		signature.setCertificate(centerbiz.isSystemSearch().getSystemcertificate());
		signature.setOID(centerbiz.isSystemSearch().getOid());
		return signature;
	}

	public Signature genSignature(SqAppData appData) throws Exception {
		Signature signature = genSignature();
		signature.setSignatureValue(digest(appData.toString()));
		logger.debug("签名值"+signature.getSignatureValue());
		return signature;
	}

	/**
	 * @param dataType
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	protected List<Map<String, String>> getDataInfos(String dataType, String xmlStr) throws DocumentException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Document doc = (Document) DocumentHelper.parseText(xmlStr);
		Element elems = doc.getRootElement();
		Iterator<Element> Elements = elems.elementIterator();
		while (Elements.hasNext()) {
			Element ele = Elements.next();
			String sign = ele.getName();
			if ("dataInfo".equals(sign)) {
				Map<String, String> dataInfo = new LinkedHashMap<String, String>();
				Iterator<Element> elem = ele.elementIterator();
				while (elem.hasNext()) {
					Element subEle = elem.next();
					dataInfo.put(subEle.getName(), StringEscapeUtils.unescapeXml(subEle.getText()));
				}
				list.add(dataInfo);
			}
		}
		return list;
	}

	public SqInfo zsqphase(String xml) throws Exception {

		// 解析
		SqInfo info = new SqInfo();
		SqAppData appData = new SqAppData();
		String appDataStr = XmlPhaser.getSubString(xml, "appData");
		try {
			String systemId = XmlPhaser.getProperty(appDataStr, "systemID");
			appData.setESCenterID(systemId);
			appData.setESCenterName(XmlPhaser.getProperty(appDataStr, "systemName"));
		} catch (Exception e) {
			appData.setESCenterID(XmlPhaser.getProperty(appDataStr, "ESCenterID"));
			appData.setESCenterName(XmlPhaser.getProperty(appDataStr, "ESCenterName"));
		}
		appData.setValidateCode(XmlPhaser.getProperty(appDataStr, "validateCode"));
		List<Map<String, String>> dataInfo = getSqDataInfos(appDataStr);
		appData.setDataInfo(dataInfo);
		String signatureStr = XmlPhaser.getSubString(xml, "signature");
		Signature signature = new Signature();
		signature.setCertificate(XmlPhaser.getProperty(signatureStr, "certificate"));
		signature.setOID(XmlPhaser.getProperty(signatureStr, "OID"));
		signature.setSignatureValue(XmlPhaser.getProperty(signatureStr, "signatureValue"));

		info.setSappData(appData);
		info.setSignature(signature);

		// 验签
		if (!validate(info.getSappData().toString(),info.getSignature())) {
			throw new Exception("验签失败");
		}
		return info;
	}

	/**
	 * @param dataType
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static List<Map<String, String>> getSqDataInfos(String xmlStr) throws DocumentException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Document doc = (Document) DocumentHelper.parseText(xmlStr);
		Element elems = doc.getRootElement();
		Iterator<Element> Elements = elems.elementIterator();
		while (Elements.hasNext()) {

			Element ele = Elements.next();
			String sign = ele.getName();
			if ("parentCenter".equals(sign)) {

				Iterator<Element> elem = ele.elementIterator();
				while (elem.hasNext()) {
					Map<String, String> dataInfo = new LinkedHashMap<String, String>();
					Element subEle = elem.next();
					Iterator<Element> elem1 = subEle.elementIterator();
					while (elem1.hasNext()) {
						Element subEle1 = elem1.next();
						dataInfo.put(subEle1.getName(), subEle1.getText());
					}
					list.add(dataInfo);
				}

			} else if ("parent_ESSM".equals(sign)) {
				Map<String, String> dataInfo = new LinkedHashMap<String, String>();
				Iterator<Element> elem = ele.elementIterator();
				while (elem.hasNext()) {
					Element subEle = elem.next();
					dataInfo.put(subEle.getName(), subEle.getText());
				}
				list.add(dataInfo);
			}
		}
		return list;
	}

	@Autowired
	public void setConfig(Config config) {
		this.config = config;
	}

}
