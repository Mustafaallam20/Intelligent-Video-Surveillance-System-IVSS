package com.IVSS.backend.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.IVSS.backend.model.User;


public interface UserDAO extends JpaRepository<User, Long>{

}
