package com.haitai.seal.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	
	/**
	 * 签名和验签之前是否对报文去空格
	 */
	@Value("${delete_space:\"false\"}")
	private String deleteSpace;

	/**
	 * 调试模式
	 */
	@Value("${debug}")
	private String debug;
	
	@Value("${sys_version}")
	private String version;
	
	@Value("${privatekey_index}")
	private Integer privatekey_index;
	
	@Value("${sso.ssoLogoutUrl:\"/explatform/logout.action\"}")
	private String logoutUrl;
	
	
	
	public boolean isDebug(){
		return "true".equals(debug);
	}
	
	public String getDebug() {
		return debug;
	}
	
	public void setDebug(String debug) {
		this.debug = debug;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public Integer getPrivatekey_index() {
		return privatekey_index;
	}

	public void setPrivatekey_index(Integer privatekey_index) {
		this.privatekey_index = privatekey_index;
	}

	public String getDeleteSpace() {
		return deleteSpace;
	}

	public void setDeleteSpace(String deleteSpace) {
		this.deleteSpace = deleteSpace;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}


}
