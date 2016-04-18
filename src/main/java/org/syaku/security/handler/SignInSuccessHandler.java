package org.syaku.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.syaku.common.http.Requesting;
import org.syaku.security.enums.StatusCode;
import org.syaku.security.service.AuthenticationService;

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
 * @since 16. 4. 12.
 */
public class SignInSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private AuthenticationService authenticationService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		if(Requesting.isAjax(request)) {
			System.out.print("===============> SignInSuccessHandler: " + request.getHeader("accept"));

			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("X-Auth-Token", authenticationService.getConsumer(authentication).getAccessToken());

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(new SuccessHandler("로그인되었습니다.", false, StatusCode.OK));

			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();

		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
