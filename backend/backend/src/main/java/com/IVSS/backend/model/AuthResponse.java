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


public class AuthResponse {
	
	private String email;
    
    private String name;
    
    private String status;
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public AuthResponse(String status ,String email ,String name){
		this.email=email;
		this.name=name;
		this.status=status;
		
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

	
    
    
  

	
}