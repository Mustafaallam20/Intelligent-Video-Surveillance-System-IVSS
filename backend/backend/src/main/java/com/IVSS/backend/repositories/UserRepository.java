package com.IVSS.backend.repositories;

import com.IVSS.backend.model.User;
import com.IVSS.backend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT id FROM User WHERE email = :email")
    Long findUserIdByEmail(@Param("email") String email);
}

