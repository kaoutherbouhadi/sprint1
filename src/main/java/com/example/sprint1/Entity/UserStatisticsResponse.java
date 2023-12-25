package com.example.sprint1.Entity;

public class UserStatisticsResponse {
    private long totalUsers;
    private long activeUsers;

    public UserStatisticsResponse(long totalUsers, long activeUsers) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }
}
