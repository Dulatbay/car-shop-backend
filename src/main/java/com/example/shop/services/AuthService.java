package com.example.shop.services;


import com.example.shop.dtos.request.AuthRequest;
import com.example.shop.dtos.request.RegisterUserRequest;
import com.example.shop.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    void registerUser(RegisterUserRequest user);

    AuthResponse authenticateUser(AuthRequest auth);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
