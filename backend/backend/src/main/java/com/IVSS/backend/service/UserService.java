package com.IVSS.backend.service;



import com.IVSS.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Long getUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }
}

