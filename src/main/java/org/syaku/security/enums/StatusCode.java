package org.syaku.security.enums;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 18.
 */
public class StatusCode {
	public static final int OK = 100; // 요청성공
	public static final int UNAUTHENTICATION = 400; // 인증 정보 없음
	public static final int AUTHENTICATIONFAILURE = 410; // 로그인 실패
	public static final int ACCESSFAILURE = 440; // 접근 권한 없음
}
