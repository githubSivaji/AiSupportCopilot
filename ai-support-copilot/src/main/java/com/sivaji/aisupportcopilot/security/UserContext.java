package com.sivaji.aisupportcopilot.security;

import java.util.UUID;

public class UserContext {
    private static final ThreadLocal<UUID> USER =
            new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        USER.set(userId);
    }

    public static UUID getUserId() {
        return USER.get();
    }

    public static void clear() {
        USER.remove();
    }

}
