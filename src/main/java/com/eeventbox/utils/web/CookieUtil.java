package com.eeventbox.utils.web;
/**
 * ================================================
 * Contains methods to deal with web browser cookie
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ================================================
 */
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static void create(HttpServletResponse httpServletResponse, String name, String value, Boolean secure, Integer maxAge, String domain) {

		Cookie cookie = new Cookie(name, value);
		//secure=true => work on HTTPS only
		cookie.setSecure(secure);
		//invisible to javaScript
		cookie.setHttpOnly(true);
		//maxAge=0: expire cookie now, maxAge<0: expire cookie on browser exit.
		cookie.setMaxAge(maxAge);
		//visible to domain only
		cookie.setDomain(domain);
		//visible to all paths
		cookie.setPath("/");
		httpServletResponse.addCookie(cookie);
	}

	public static void clear(HttpServletResponse httpServletResponse, String name) {

		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		httpServletResponse.addCookie(cookie);
	}

	public static String getValue(HttpServletRequest httpServletRequest, String name) {

		Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
		return cookie != null ? cookie.getValue() : null;
	}
}
