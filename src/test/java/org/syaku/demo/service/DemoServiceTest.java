package org.syaku.demo.service;

import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:org/syaku/config/*-context.xml"
})
public class DemoServiceTest {
	@Resource(name = "demoService") private DemoService demoService;

	@Test
	public void crud() {
		demoService.crud();
		Assert.assertSame("should be same 0", demoService.getDemoList().size(), 0);
	}

	@Ignore
	public void transaction() {
		demoService.transaction();
		Assert.assertSame("should be same 0", demoService.getDemoList().size(), 0);
	}
}
