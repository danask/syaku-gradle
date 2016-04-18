package org.syaku.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.syaku.security.Consumer;
import org.syaku.security.service.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
@Controller
public class SecurityController {

	@Autowired private AuthenticationService authenticationService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String main() {
		return "main";
	}

	@PreAuthorize("authenticated")
	@RequestMapping(value="/mypage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Consumer> mypage(Authentication authentication) {
		return new ResponseEntity(authenticationService.getConsumer(authentication), HttpStatus.OK);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Map> index() {

		Map data = new HashMap();
		data.put("name", "syaku");

		return new ResponseEntity(data, HttpStatus.OK);
	}
}
