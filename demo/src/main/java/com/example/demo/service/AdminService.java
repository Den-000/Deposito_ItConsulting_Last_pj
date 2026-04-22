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

    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }

    public MyUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public void promoteUser(String email) {
        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    public void promoteUserById(Long id) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}