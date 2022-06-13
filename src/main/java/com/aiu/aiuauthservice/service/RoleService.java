package com.aiu.aiuauthservice.service;

import com.aiu.aiuauthservice.domain.Role;

import java.util.List;

public interface RoleService {
    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    List<Role> getRoles();
}
