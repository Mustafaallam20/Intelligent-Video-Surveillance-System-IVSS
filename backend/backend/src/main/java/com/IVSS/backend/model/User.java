package com.IVSS.backend.model;
import org.hibernate.annotations.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
	
import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(appliesTo = "`User`")
public class User {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`")
    private Long id;
     
    @Column(name = "`EMAIL`" , nullable = false, unique = true, length = 45)
    private String email;
     
     
    @Column(name = "`NAME`", nullable = false, length = 20)
    private String name;
    
    
    @Column(name = "`USERNAME`", nullable = false,unique = true, length = 20)
    private String username;
     
    @Column(name = "`PASSWORD`" ,nullable = false, length = 64)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "`user_roles`",
        joinColumns = @JoinColumn(name = "`user_id`", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "`role_id`", referencedColumnName = "ID"))
    private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

    

	
}