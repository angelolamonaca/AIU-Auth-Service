package com.aiu.aiuauthservice;

import com.aiu.aiuauthservice.domain.Role;
import com.aiu.aiuauthservice.domain.RoleType;
import com.aiu.aiuauthservice.domain.User;
import com.aiu.aiuauthservice.service.RoleService;
import com.aiu.aiuauthservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.ArrayList;

@SpringBootApplication
public class AiuAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiuAuthServiceApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService) {
        return args -> {
            roleService.saveRole(new Role(null, RoleType.ROLE_USER.name()));
            // userService.saveRole(new Role(null, RoleType.ROLE_MANAGER.name()));
            // userService.saveRole(new Role(null, RoleType.ROLE_ADMIN.name()));
            roleService.saveRole(new Role(null, RoleType.ROLE_SUPER_ADMIN.name()));

            userService.saveUser(new User(null, "Itachi Uchiha", "itachi@test.com", "Aa1@aaaaa", new ArrayList<>()));
            userService.saveUser(new User(null, "Boruto Uzumaki", "boruto@test.com", "Aa1@aaaaa", new ArrayList<>()));
            // userService.saveUser(new User(null, "Naruto Uzumaki", "naruto", "1234", new ArrayList<>()));
            // userService.saveUser(new User(null, "Sasuke Uchiha", "sasuke", "1234", new ArrayList<>()));

            roleService.addRoleToUser("boruto@test.com", RoleType.ROLE_USER.name());
            roleService.addRoleToUser("itachi@test.com", RoleType.ROLE_USER.name());
            roleService.addRoleToUser("itachi@test.com", RoleType.ROLE_SUPER_ADMIN.name());
        };
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        return loggingFilter;
    }

}
