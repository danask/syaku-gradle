package org.syaku.security.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.syaku.security.Consumer;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
public class AuthenticationService {

	public Consumer getConsumer() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if ( !(authentication instanceof AnonymousAuthenticationToken) ) {
				return (Consumer) authentication.getPrincipal();
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Consumer getConsumer(Authentication authentication) {

		if ( !(authentication instanceof AnonymousAuthenticationToken) ) {
			return (Consumer) authentication.getPrincipal();
		}

		return null;
	}
}
