package org.syaku.demo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 10.
 */
public abstract class AbstractDAO {

	@Autowired protected SessionFactory sessionFactory;

	protected Session session() {
		return sessionFactory.getCurrentSession();
	}
}
