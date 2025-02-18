package com.example.NotificationService.context;


public class UserContextHolder {
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static Long getUserId() {
        return currentUserId.get();
    }

    public static void setUserId(Long userId) {
        currentUserId.set(userId);
    }

    public static void  clear(){
        currentUserId.remove();
    }
}
