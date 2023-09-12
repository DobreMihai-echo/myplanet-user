package com.myplanet.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.domain.UsersBase;
import com.myplanet.userservice.payload.LoginRequest;
import com.myplanet.userservice.payload.LoginResponse;
import com.myplanet.userservice.payload.SignupRequest;
import com.myplanet.userservice.service.UsersService;
import com.myplanet.userservice.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final UsersService service;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (!service.existsByUsername(loginRequest.getUsername())) {
                return ResponseEntity.badRequest().body("Username doesn't exist");
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(LoginResponse.builder()
                            .users((UserDetails) authentication.getPrincipal())
                            .jwtToken(token)
                            .type("Bearer ")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) throws JsonProcessingException {
        try {
            UsersBase usersBase = service.registerUser(request);
            return ResponseEntity.ok(usersBase);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String token) {
        return ResponseEntity.ok(service.confirmToken(token));
    }


}
