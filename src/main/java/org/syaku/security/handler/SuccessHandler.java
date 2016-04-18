package org.syaku.security.handler;

import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
public class SuccessHandler {
	private final String message;
	private final boolean error;
	private final int statusCode;
	private Map<String, Object> data;

	public SuccessHandler(String message, int statusCode) {
		this(message, false, statusCode);
	}

	public SuccessHandler(String message, boolean error, int statusCode) {
		this.message = message;
		this.error = error;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return error;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
