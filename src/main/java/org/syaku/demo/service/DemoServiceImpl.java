package org.syaku.demo.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syaku.demo.dao.DemoDAO;
import org.syaku.demo.domain.Demo;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 9.
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {
	@Autowired private SessionFactory sessionFactory;
	@Resource(name = "demoDAO") private DemoDAO demoDAO;

	@Override
	public List<Demo> getDemoList() {
		return demoDAO.findAll();
	}

	@Override
	public void crud() {
		demoDAO.deleteAll();

		Demo demo = new Demo("최석균", "syaku");
		demoDAO.insert(demo);
		System.out.println("===============>" + demo.getDemo_idx());

		demoDAO.delete(demo.getDemo_idx());
		sessionFactory.getCurrentSession().flush();
		if (true) throw new RuntimeException("error");

		Demo demo2 = new Demo("최석균", "syaku");
		demoDAO.insert(demo2);
		/*
		demo.setName("good");
		demoDAO.update(demo);
		sessionFactory.getCurrentSession().flush();
*/
		System.out.println("===============>" + demo2.getDemo_idx());

		//if (true) throw new RuntimeException("error");

		//demoDAO.delete(demo.getDemo_idx());
	}

	@Override
	public void transaction() {
		Demo demo = new Demo("최석균", "syaku");

		demoDAO.insert(demo);

		if (true) throw new RuntimeException("error");

		demoDAO.findAll();
	}
}
