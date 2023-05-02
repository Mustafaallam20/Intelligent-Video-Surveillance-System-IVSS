package com.IVSS.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IVSS.backend.dao.UserDAO;
import com.IVSS.backend.model.User;

@Service
public class UserDetailsService  {
	@Autowired
	private UserDAO userDAO;

	public UserDetailsService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public User getUserByEmail(String email) {
		return this.userDAO.findByEmail(email).get();
	}

	
}
