package com.IVSS.backend.controller;

import com.IVSS.backend.dao.RoleDAO;
import com.IVSS.backend.dao.UserDAO;
import com.IVSS.backend.model.AuthResponse;
import com.IVSS.backend.model.User;
import com.IVSS.backend.security.jwt.JwtTokenUtil;
import com.IVSS.backend.security.models.CustomUserDetailsImpl;
import com.IVSS.backend.service.UserDetailsService;
import login_signupDTO.LoginDto;
import login_signupDTO.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/login")
	public AuthResponse authenticateUser(@RequestBody LoginDto loginDto) {
		final CustomUserDetailsImpl customUserDetailsImpl;
		final UserDetails m;
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
			m = (UserDetails) authentication.getPrincipal();
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			return new AuthResponse(loginDto.getUsernameOrEmail(), "", "", "Fail", "");
		}
		final String jwt = jwtTokenUtil.generateToken(m);
		User user = userDetailsService.getUserByEmail(m.getUsername());
		return new AuthResponse(loginDto.getUsernameOrEmail(), user.getUsername(), user.getName(), "Success", jwt);
	}

	@PostMapping("/signup")
	public AuthResponse registerUser(@RequestBody SignUpDto signUpDto) {
		// create user object
		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());

		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		// add check for username exists in a DB
		if (userDAO.existsByUsername(signUpDto.getUsername())) {

			return new AuthResponse(user.getEmail(), user.getUsername(), user.getName(), "Dublicated Username", null);
		}

		// add check for email exists in DB
		if (userDAO.existsByEmail(signUpDto.getEmail())) {
			return new AuthResponse(user.getEmail(), user.getUsername(),user.getName(),"Dublicated Email", null);
		}

//		Role roles = roleDAO.findByName("ROLE_ADMIN").get();
//		user.setRoles(Collections.singleton(roles));

		userDAO.save(user);

		return new AuthResponse(user.getEmail(), user.getUsername(),user.getName(),"Success", null);

	}


}