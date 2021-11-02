package com.internet.shop.config;

import com.internet.shop.dmo.Role;
import com.internet.shop.dmo.User;
import com.internet.shop.repository.RoleRepository;
import com.internet.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Role adminRole = createRoleIfNotFound(Role.RoleName.ROLE_ADMIN);
        createRoleIfNotFound(Role.RoleName.ROLE_USER);

        createAdminIfNotExist(adminRole);

        alreadySetup = true;
    }

    private Role createRoleIfNotFound(Role.RoleName name) {
        Role role = roleRepository.findByName(name.name());
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

    private void createAdminIfNotExist(Role adminRole) {
        User admin = userRepository.findByUsername("admin");
        if (admin == null) {
            admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(adminRole));
            userRepository.save(admin);
        }
    }

}
