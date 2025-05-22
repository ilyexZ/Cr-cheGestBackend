package org.ilyes.crechegest.config;

import org.ilyes.crechegest.model.Role;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.service.RoleService;
import org.ilyes.crechegest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create default roles if they don't exist
        if (!roleService.existsByName("ADMIN")) {
            Role adminRole = new Role("ADMIN", "Administrator with full access");
            roleService.save(adminRole);
        }

        if (!roleService.existsByName("EDUCATOR")) {
            Role educatorRole = new Role("EDUCATOR", "Educator managing children and activities");
            roleService.save(educatorRole);
        }

        if (!roleService.existsByName("PARENT")) {
            Role parentRole = new Role("PARENT", "Parent accessing child information");
            roleService.save(parentRole);
        }

        if (!roleService.existsByName("KITCHEN")) {
            Role kitchenRole = new Role("KITCHEN", "Kitchen staff managing meals");
            roleService.save(kitchenRole);
        }

        // Create default admin user if not exists
        if (!userService.existsByUsername("admin")) {
            Role adminRole = roleService.findByName("ADMIN").orElse(null);
            if (adminRole != null) {
                User adminUser = new User("admin", "admin123", "Administrateur",
                        "admin@crechegest.com", "0123456789", adminRole);
                userService.save(adminUser);
            }
        }
    }
}