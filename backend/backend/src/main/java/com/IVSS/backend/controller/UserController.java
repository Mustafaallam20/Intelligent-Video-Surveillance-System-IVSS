package com.IVSS.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.IVSS.backend.dao.UserDAO;

@Controller
public class UserController {
	
	  @Autowired
	    private UserDAO userDAO;
	     
	    @GetMapping("")
	    public String viewHomePage() {
	        return "index";
	    }
	    
	    
}
