package com.aiu.aiuauthservice.api;

import com.aiu.aiuauthservice.domain.RoleType;
import com.aiu.aiuauthservice.domain.User;
import com.aiu.aiuauthservice.service.RoleService;
import com.aiu.aiuauthservice.service.UserService;
import com.aiu.aiuauthservice.utility.PasswordValidator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.aiu.aiuauthservice.utility.JWTokenProvider.decodeJWT;
import static com.aiu.aiuauthservice.utility.JWTokenProvider.generateTokens;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            List<String> passwordValidationResult = new PasswordValidator().validate(user.getPassword());
            if (!passwordValidationResult.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(passwordValidationResult);
            }
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
            User newUser = userService.saveUser(user);
            roleService.addRoleToUser(user.getUsername(), RoleType.ROLE_USER.name());
            return ResponseEntity
                    .created(uri)
                    .body(newUser);

        } catch (Exception e) {
            String errorMessage = getRootCauseMessage(e);
            log.error("Error registering: {}", errorMessage);
            return ResponseEntity
                    .badRequest()
                    .body(errorMessage);
        }
    }

    @GetMapping("/validatetoken")
    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT jwt = decodeJWT(token);
                String username = jwt.getSubject();
                response.setHeader("email", username);
                response.setStatus(ACCEPTED.value());
            } catch (Exception exception) {
                log.error("Error validating tokens in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.sendError(FORBIDDEN.value());
            }
        } else {
            throw new RuntimeException("Error on token validating: Token is missing or malformed");
        }

    }

    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = decodeJWT(token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                Map<String, String> tokens = generateTokens(user, request.getRequestURL().toString());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                log.error("Error refreshing tokens in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.sendError(FORBIDDEN.value());
            }
        } else {
            throw new RuntimeException("Refresh token is missing or malformed");
        }

    }
}
