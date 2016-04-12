package org.syaku.demo.service;

import org.syaku.demo.domain.Demo;

import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 9.
 */
public interface DemoService {

	List<Demo> getDemoList();

	void crud();
	void transaction();
}
