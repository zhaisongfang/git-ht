package com.haitai.seal.webService;

import javax.jws.WebService;

@WebService(targetNamespace="http://www.oscca.gov.cn/eseal/")
public interface IESealService {

	String queryStatusData(String sealDataInfo, String formatType)
			throws Exception;

	String sendData(String sealDataInfo, String formatType) throws Exception;

}
