package com.IVSS.backend.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.IVSS.backend.dao.RoleDAO;
import com.IVSS.backend.dao.UserDAO;
import com.IVSS.backend.model.AuthResponse;
import com.IVSS.backend.model.Role;
import com.IVSS.backend.model.User;
import com.IVSS.backend.security.jwt.JwtTokenUtil;
import com.IVSS.backend.security.models.CustomUserDetailsImpl;

import login_signupDTO.LoginDto;
import login_signupDTO.SignUpDto;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")

public class TestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil; 

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/test")
	public AuthResponse test( ) {
		try {
			return new AuthResponse("success", null, null,null);
		} catch (Exception e) {
			return new AuthResponse("fail", "", "", "");
		}
		
		
	}

	

}