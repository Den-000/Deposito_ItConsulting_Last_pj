package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AdminStatsDTO;
import com.example.demo.model.Role;
import com.example.demo.model.MyUser;
import com.example.demo.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminStatsDTO getStats() {
        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.countByRole(Role.ADMIN);

        return new AdminStatsDTO(totalUsers, totalAdmins);
    }

    public void promoteUser(String email) {
        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }
}