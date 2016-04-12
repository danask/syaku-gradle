package org.syaku.demo.dao;

import org.syaku.demo.domain.Demo;

import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 8.
 */
public interface DemoDAO {
	List<Demo> findAll();
	Demo findOne(long demo_idx);
	void insert(Demo demo);
	void update(Demo demo);
	void updateBySave(Demo demo);
	void updateByMerge(Demo demo);
	void delete(long demo_idx);
	void delete(Demo demo);
	void deleteAll();
}
