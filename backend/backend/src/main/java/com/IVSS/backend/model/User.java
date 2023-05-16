package com.IVSS.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Table;

import java.util.List;
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
    @Column(name = "`USERNAME`", nullable = false,unique = true, length = 20)
    private String username;
    @Column(name = "`NAME`", nullable = false, length = 20)
    private String name;
    @Column(name = "`PASSWORD`" ,nullable = false, length = 64)
    private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Video> videos;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "`user_roles`",
        joinColumns = @JoinColumn(name = "`user_id`", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "`role_id`", referencedColumnName = "ID"))
    private Set<Role> roles;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}