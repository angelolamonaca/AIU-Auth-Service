package com.aiu.aiuauthservice.service;

import com.aiu.aiuauthservice.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User getUser(String username);

    List<User> getUsers();
}
