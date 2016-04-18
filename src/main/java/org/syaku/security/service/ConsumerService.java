package org.syaku.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 12.
 */
public class ConsumerService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

	private Map<String, String> userData;

	public void setUserData(Map<String, String> userData) {
		this.userData = userData;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		String password = userData.get(username);

		if (!userData.containsKey(username)) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

		logger.debug("==============> ConsumerService");
		logger.debug("username: " + username);
		logger.debug("password: " + password);

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));

		User user = new User(username, password, roles);
		return user;
	}
}