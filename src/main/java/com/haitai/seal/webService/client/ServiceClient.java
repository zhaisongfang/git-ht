package com.haitai.seal.webService.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.haitai.seal.webService.IESealService;

public class ServiceClient {

	public static IESealService getService(String wsdlLocation) throws MalformedURLException{
		URL url = new URL(wsdlLocation);  
        QName qname = new QName("http://www.oscca.gov.cn/eseal/", "IESealServiceService");  
        Service service = Service.create(url, qname);  
        return service.getPort(IESealService.class);  
	}

}
