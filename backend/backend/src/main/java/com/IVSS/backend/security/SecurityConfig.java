package com.IVSS.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.IVSS.backend.security.jwt.JwtTokenFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable().authorizeHttpRequests((authorize) ->

		authorize

				.requestMatchers("/api/auth/**").permitAll()

				.anyRequest().authenticated()

		);

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	/*
	 * @Override public void configure(HttpSecurity http) throws Exception {
	 * 
	 * http.csrf().disable().sessionManagement().sessionCreationPolicy(
	 * SessionCreationPolicy.STATELESS).and() .authorizeRequests()
	 * .antMatchers("/api/auth/**").permitAll()
	 * .antMatchers("/api/config").hasRole("ADMIN")
	 * 
	 * .antMatchers("/api/**").authenticated().anyRequest().permitAll().and().logout
	 * ().disable().formLogin() .disable();
	 * 
	 * http.addFilterBefore( new JwtTokenFilter(),
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * }
	 */
}