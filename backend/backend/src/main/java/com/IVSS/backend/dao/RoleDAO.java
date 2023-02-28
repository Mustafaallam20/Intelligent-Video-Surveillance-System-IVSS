package com.IVSS.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.IVSS.backend.model.Role;

public interface RoleDAO extends JpaRepository <Role, Long> {
    Optional<Role> findByName(String name);
}