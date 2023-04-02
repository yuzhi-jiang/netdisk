package com.yefeng.netdisk.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtils {

	private static final String AUTHORIZATION_TOKEN = "token";

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	public static String getTokenByHeader() {
		HttpServletRequest request = getHttpServletRequest();
		String rightToken = request.getHeader(AUTHORIZATION_TOKEN);
		JWTUtil.validateToken(rightToken);
		return rightToken;
	}



	public static String getHeader(String key) {
		HttpServletRequest request = getHttpServletRequest();
		String header = request.getHeader(key);
		return header;
	}


}