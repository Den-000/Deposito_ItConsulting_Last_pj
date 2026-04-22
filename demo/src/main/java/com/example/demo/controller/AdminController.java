package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdminStatsDTO;
import com.example.demo.model.MyUser;
import com.example.demo.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public AdminStatsDTO getStats() {
        return adminService.getStats();
    }

    @GetMapping("/users")
    public List<MyUser> getUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/promote")
    public void promoteByEmail(@RequestParam String email) {
        adminService.promoteUser(email);
    }

    @PostMapping("/users/{email}/promote")
    public void promote(@PathVariable String email) {
        adminService.promoteUser(email);
    }
}
