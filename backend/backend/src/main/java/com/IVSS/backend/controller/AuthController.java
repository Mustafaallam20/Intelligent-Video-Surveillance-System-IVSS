package com.IVSS.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.IVSS.backend.dao.RoleDAO;
import com.IVSS.backend.dao.UserDAO;
import com.IVSS.backend.model.*;

import login_signupDTO.LoginDto;
import login_signupDTO.SignUpDto;

import com.IVSS.backend.*;

import java.util.Collections;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    @PostMapping("/login")
    public AuthResponse authenticateUser(@RequestBody LoginDto loginDto){
       try { Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);}
       catch(Exception e )
       {
    	   return new AuthResponse("fail",loginDto.getUsernameOrEmail() ,"");
       }
       
        return new AuthResponse("success",loginDto.getUsernameOrEmail() ,"");
    }
    
    
    
    @PostMapping("/signup")
    public AuthResponse registerUser(@RequestBody SignUpDto signUpDto){

        // add check for username exists in a DB
        if(userDAO.existsByUsername(signUpDto.getUsername())){
        	 return new AuthResponse("dublicated",null,null);
        }

        // add check for email exists in DB
        if(userDAO.existsByEmail(signUpDto.getEmail())){
        	 return new AuthResponse("dublicated",null,null);
        }

        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleDAO.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
      
        userDAO.save(user);
        return new AuthResponse("success",null,null);
        

    }
    
    
}