package org.ilyes.crechegest.util;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUtil {

    public static Long getCurrentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    public static String getCurrentUserRole(HttpServletRequest request) {
        return (String) request.getAttribute("userRole");
    }

    public static String getCurrentUserFullName(HttpServletRequest request) {
        return (String) request.getAttribute("fullName");
    }

    public static String getCurrentUserEmail(HttpServletRequest request) {
        return (String) request.getAttribute("email");
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return "ADMIN".equals(getCurrentUserRole(request));
    }

    public static boolean isEducator(HttpServletRequest request) {
        return "EDUCATOR".equals(getCurrentUserRole(request));
    }

    public static boolean isParent(HttpServletRequest request) {
        return "PARENT".equals(getCurrentUserRole(request));
    }

    public static boolean isAdminOrEducator(HttpServletRequest request) {
        String role = getCurrentUserRole(request);
        return "ADMIN".equals(role) || "EDUCATOR".equals(role);
    }

    public static boolean canAccessUserData(HttpServletRequest request, Long targetUserId) {
        Long currentUserId = getCurrentUserId(request);
        String role = getCurrentUserRole(request);

        // Admin can access all data
        if ("ADMIN".equals(role)) {
            return true;
        }

        // Educators can access most data
        if ("EDUCATOR".equals(role)) {
            return true;
        }

        // Parents can only access their own data
        if ("PARENT".equals(role)) {
            return currentUserId != null && currentUserId.equals(targetUserId);
        }

        return false;
    }
}
