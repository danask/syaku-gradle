package org.syaku.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;
import org.syaku.security.Consumer;
import org.syaku.security.AuthenticationToken;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
public class AuthenticationTokenFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	@Resource(name = "consumerService") private UserDetailsService userService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);

		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String username = AuthenticationToken.getUserName(authToken);

		logger.debug("==============> AuthenticationTokenFilter");
		logger.debug("authToken: " + authToken);
		logger.debug("username: " + username);

		if (username != null) {

			UserDetails user = userService.loadUserByUsername(username);

			if (!AuthenticationToken.validate(authToken, user)) {
				SecurityContextHolder.clearContext();
			} else {
				String accessToken = AuthenticationToken.create(user);
				logger.debug("===============> Access Token Create: " + accessToken);

				Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
				Consumer consumer = new Consumer(username, user.getPassword(), accessToken, authorities);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(consumer, consumer.getPassword(), authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}


	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}


	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		String authToken = httpRequest.getHeader("X-Auth-Token");
		return authToken;
	}
}
