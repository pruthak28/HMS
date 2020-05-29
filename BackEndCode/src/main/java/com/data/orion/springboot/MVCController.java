package com.data.orion.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MVCController {
    @RequestMapping("/home")
    public String helloToIndex(Model model) {
        //model.addAttribute("name", name);
		//TSAUtil.startScheduler();
		System.out.println("HelloToIndex>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return "index";
    }
}
