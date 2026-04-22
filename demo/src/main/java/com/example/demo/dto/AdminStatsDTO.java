package com.example.demo.dto;

public class AdminStatsDTO {
    private long totalUsers;
    private long totalAdmins;

    public AdminStatsDTO(long totalUsers, long totalAdmins) {
        this.totalUsers = totalUsers;
        this.totalAdmins = totalAdmins;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }
}