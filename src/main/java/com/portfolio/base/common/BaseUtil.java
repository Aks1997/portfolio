package com.portfolio.base.common;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BaseUtil {

	public static String resolveBase64ToString(String base64String) {
		base64String = base64String.trim();
		byte[] bytes = Base64.getDecoder().decode(base64String);
	    String resolvedString = new String(bytes, StandardCharsets.UTF_8);
	    return resolvedString;
	}
}
