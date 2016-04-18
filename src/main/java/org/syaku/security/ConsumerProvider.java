package org.syaku.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 12.
 */
public class ConsumerProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(ConsumerProvider.class);

	@Autowired private PasswordEncoder passwordEncoder;
	@Resource(name = "consumerService") private UserDetailsService consumerService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		logger.debug("==============> ConsumerProvider");
		logger.debug("username: " + username);
		logger.debug("password: " + password);
		logger.debug("encoding: " + passwordEncoder.encode(password));

		UserDetails user = consumerService.loadUserByUsername(username);

		if ( !passwordEncoder.matches(password, user.getPassword()) ) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		String accessToken = AuthenticationToken.create(user);

		logger.debug("===============> Access Token Create: " + accessToken);

		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		Consumer consumer = new Consumer(username, passwordEncoder.encode(password), accessToken, authorities);

		return new UsernamePasswordAuthenticationToken(consumer, consumer.getPassword(), authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}