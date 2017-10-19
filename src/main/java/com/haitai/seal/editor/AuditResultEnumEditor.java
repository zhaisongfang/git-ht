package com.haitai.seal.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;

import com.haitai.seal.enums.AuditResult;


public class AuditResultEnumEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

    	if(StringUtils.isNotBlank(text)){
	        int val = Integer.parseInt(text);
	
	        boolean found = false;
	
	        for (AuditResult type : AuditResult.values()) {
	            if (val == type.ordinal()) {
	                this.setValue(type);
	                found = true;
	                break;
	            }
	        }
	
	        if (found == false) {
	            throw new IllegalArgumentException("非法的枚举值:" + text);
	        }
    	}

    }
}
