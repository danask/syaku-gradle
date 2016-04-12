package org.syaku.demo.dao;

import org.springframework.stereotype.Repository;
import org.syaku.demo.domain.Demo;

import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 8.
 */
@Repository("demoDAO")
public class DemoDAOImpl extends AbstractDAO implements DemoDAO {

	@Override
	public List<Demo> findAll() {
		return session().createCriteria(Demo.class).list();
	}

	@Override
	public Demo findOne(long demo_idx) {
		return session().get(Demo.class, demo_idx);
	}

	@Override
	public void insert(Demo demo) {
		session().persist(demo);
	}

	@Override
	public void update(Demo demo) {
		session().update(demo);
	}

	@Override
	public void updateBySave(Demo demo) {
		session().save(demo);
	}

	@Override
	public void updateByMerge(Demo demo) {
		session().merge(demo);
	}

	@Override
	public void delete(long demo_idx) {
		session().createQuery("delete from " + Demo.class.getName() + " d where d.demo_idx = :demo_idx").setParameter("demo_idx", demo_idx).executeUpdate();
		//session().createSQLQuery("delete from demo where demo_idx = :demo_idx").setParameter("demo_idx", demo_idx).executeUpdate();
	}

	@Override
	public void delete(Demo demo) {
		session().delete(demo);
	}

	@Override
	public void deleteAll() {
		session().createSQLQuery("delete from demo").executeUpdate();
	}
}
