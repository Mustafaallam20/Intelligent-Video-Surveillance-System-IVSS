package com.IVSS.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "`roles`")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "`NAME`", nullable = false, length = 20)
	private String name;
}