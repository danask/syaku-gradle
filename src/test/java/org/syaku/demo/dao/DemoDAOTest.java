package org.syaku.demo.dao;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.syaku.demo.domain.Demo;

import javax.annotation.Resource;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:org/syaku/config/*-context.xml"
})
@Transactional
public class DemoDAOTest {
	@Autowired private SessionFactory sessionFactory;
	@Resource(name = "demoDAO") private DemoDAO demoDAO;

	private Session session;

	@Before
	public void before() {
		session = sessionFactory.getCurrentSession();
	}

	private void clear() {
		session.flush();
		session.clear();
	}

	@Test
	public void insertToDelete() {
		Demo demo = new Demo("최석균", "syaku");
		demoDAO.insert(demo);
		demoDAO.delete(demo.getDemo_idx());
		sessionFactory.getCurrentSession().flush();

		Demo demo2 = new Demo("최석균", "syaku");
		demoDAO.insert(demo2);
		demoDAO.delete(demo2);
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void update() {
		Demo demo1 = new Demo("최석균", "syaku");
		demoDAO.updateBySave(demo1);
		System.out.println(demo1.toString());
		System.out.println("updateBySave ========> " + demoDAO.findAll().size());
		demoDAO.updateBySave(demo1);
		System.out.println("updateBySave2 ========> " + demoDAO.findAll().size());
		sessionFactory.getCurrentSession().flush();

		Demo demo = demoDAO.findOne(demo1.getDemo_idx());
		demo.setName("변경됨");
		demo.setValue("change");
		demoDAO.update(demo);
		System.out.println("updateBySave ========> " + demo.toString());
		sessionFactory.getCurrentSession().flush();

		Demo demo2 = new Demo("최석균2", "syaku2");
		demoDAO.updateByMerge(demo2);
		System.out.println(demo2.toString());
		System.out.println("updateByMerge ========> " + demoDAO.findAll().size());
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void persistent() {
		Demo demo1 = new Demo("최석균", "syaku");
		demoDAO.insert(demo1);
		System.out.println(demo1.toString());
		demo1.setName("최석균2");
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		Demo demo = demoDAO.findOne(demo1.getDemo_idx());
		System.out.println(demo.toString());

		demo1.setName("최석균3");

		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		Demo demo3 = demoDAO.findOne(demo1.getDemo_idx());
		System.out.println(demo3.toString());
	}
}
