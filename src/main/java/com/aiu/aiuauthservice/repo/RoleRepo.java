package com.aiu.aiuauthservice.repo;

import com.aiu.aiuauthservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
