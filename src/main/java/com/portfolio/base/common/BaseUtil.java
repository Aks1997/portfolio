package com.portfolio.base.common;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseUtil {

	public static String resolveBase64ToString(String base64String) {
		base64String = base64String.trim();
		byte[] bytes = Base64.getDecoder().decode(base64String);
	    String resolvedString = new String(bytes, StandardCharsets.UTF_8);
	    return resolvedString;
	}
	
	public static Map<String, Object> createAttribute(String key, String label, String value,
			String type, boolean required, boolean viewmode, List<Map<String, Object>> options,
			boolean isLink){
		Map<String, Object> attr=new HashMap<>();
		
		attr.put(CommonConstants.KEY, key);
		attr.put(CommonConstants.LABEL, label);
		attr.put(CommonConstants.VALUE, value!=null?value:"");
		attr.put(CommonConstants.TYPE, type);
		attr.put(CommonConstants.REQUIRED, required);
		attr.put(CommonConstants.VIEWMODE, viewmode);
		attr.put(CommonConstants.ISLINK, isLink);
		
		if(CommonConstants.TYPE_DROPDOWN.equals(type) || CommonConstants.TYPE_AUTOCOMPLETE.equals(type)) {
			attr.put(CommonConstants.OPTIONS, options);
		}
		return attr;
	}
}
