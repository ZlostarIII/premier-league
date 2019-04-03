package com.sapienza.premierleague.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/home")
    public String home() {
        return "home";
    }
	
	@RequestMapping("/goalscorers")
    public String goalscorers() {
        return "goalscorers";
    }
	
	@RequestMapping("/statistics")
    public String statistics() {
        return "statistics";
    }
	
}
