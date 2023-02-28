package com.IVSS.backend.model;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "`roles`")
public class Role {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "`ID`")
	    private Long id;

    @Column(name = "`NAME`", nullable = false, length = 20)
    private String name;
}