package org.syaku.common.http;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */

public class Requesting {
	private Requesting() {}

	public static boolean isAjax(HttpServletRequest request) {
		String accept = StringUtils.lowerCase(request.getHeader("accept"));
		String ajax = request.getHeader("X-Requested-With");

		return ( StringUtils.indexOf(accept, "json") > -1 || StringUtils.isNotEmpty(ajax));
	}
}
