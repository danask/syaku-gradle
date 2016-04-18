package org.syaku.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.syaku.common.http.Requesting;
import org.syaku.security.enums.StatusCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
public class UnauthorizedHandler implements AuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandler.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(Requesting.isAjax(request)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(new SuccessHandler("인증 정보가 없습니다.", true, StatusCode.UNAUTHENTICATION));

			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}