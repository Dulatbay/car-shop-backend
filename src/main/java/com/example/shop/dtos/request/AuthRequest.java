package com.example.shop.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    @NotNull
    private String emailOrUsername;
    @NotNull
    private String password;
}
