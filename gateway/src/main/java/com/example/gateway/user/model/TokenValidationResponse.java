package com.example.gateway.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationResponse {


    private Long userId;

    private String email;

    private UserRole role;


}
