package org.syaku.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 4.
 */
@Controller
public class DemoController {

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String dispDemoView(Model model) {

		model.addAttribute("good", "좋아!!");

		return "demo";
	}
}
