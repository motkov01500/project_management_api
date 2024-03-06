package org.cbg.projectmanagement.project_management.auth;

public class UserContext {

    private static final ThreadLocal<UserDetails> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserDetails userDetails) {
        currentUser.set(userDetails);
    }

    public static UserDetails getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
