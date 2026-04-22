package com.example.demo.controller;

import com.example.demo.service.TicketService;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdminStatsDTO;
import com.example.demo.dto.TicketDTO;
import com.example.demo.model.MyUser;
import com.example.demo.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TicketService ticketService;
    private final AdminService adminService;

    public AdminController(AdminService adminService, TicketService ticketService) {
        this.adminService = adminService;
        this.ticketService = ticketService;
    }

    @GetMapping("/stats")
    public AdminStatsDTO getStats() {
        return adminService.getStats();
    }

    /* ================= USERS ================= */

    @GetMapping("/users")
    public List<MyUser> getUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public MyUser getUser(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }

    @PostMapping("/users/{id}/promote")
    public void promote(@PathVariable Long id) {
        adminService.promoteUserById(id);
    }

    @PostMapping("/promote")
    public void promoteByEmail(@RequestParam String email) {
        adminService.promoteUser(email);
    }

    /* ================= TICKETS ================= */

    @GetMapping("/users/{id}/tickets")
    public List<TicketDTO> getUserTickets(@PathVariable Long id) {
        return ticketService.getUserTickets(id);
    }
}