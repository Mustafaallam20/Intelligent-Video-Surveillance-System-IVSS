package com.IVSS.backend.dao;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.IVSS.backend.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserDAO extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
