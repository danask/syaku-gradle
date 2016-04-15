package org.syaku.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 12.
 */
public class ConsumerService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		// test 값을 암호화함.
		String password = "aabcb987e4b425751e210413562e78f776de6285";

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));

		User user = new User(username, password, roles);

		// 만약 데이터가 없을 경우 익셉션
		//if (user == null) throw new UsernameNotFoundException("접속자 정보를 찾을 수 없습니다.");

		return user;
	}
}