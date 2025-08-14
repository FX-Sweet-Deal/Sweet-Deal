package com.example.user.domain.user.controller.model.login;


import com.example.user.domain.user.repository.enums.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {


    private Long id;
    private String email;
    private String name;
    private LocalDate birth;
    private String address;
    private String phone;
    private UserRole role;


}
