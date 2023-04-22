package com.IVSS.backend.security.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.IVSS.backend.model.User;

public class CustomUserDetailsImpl  implements UserDetails{

	private Long id;
    private String username;
    private String password;
    private String email;
    private Integer active;

    private List<GrantedAuthority> authorities;

    public CustomUserDetailsImpl(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.email = user.getEmail();
   // this.active = user.getActive();
    //this.authorities = Arrays.stream(user.getRoles().(","))
      //  .map(SimpleGrantedAuthority::new)
       // .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
    }

    public Long getId() {
    return id;
    }

    public String getEmail() {
    return email;
    }

    public Integer getActive() {
    return active;
    }

    @Override
    public String getPassword() {
    return this.password;
    }

    @Override
    public String getUsername() {
    return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
    return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
    return true;
    }

    @Override
    public boolean isEnabled() {
    return this.active == 1 ? true : false;
    }

}
