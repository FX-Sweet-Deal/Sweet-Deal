package com.example.user.domain.user.controller.model.update;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordChangeRequest {

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9])(?!.*[\\\\{}()<>$%^&*_=|`]).{8,100}$",
        message = "대문자, 소문자, 특수문자를 포함하고 8자 이상이어야 합니다."
    )
    private String currentPassword;

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9])(?!.*[\\\\{}()<>$%^&*_=|`]).{8,100}$",
        message = "대문자, 소문자, 특수문자를 포함하고 8자 이상이어야 합니다."
    )
    private String newPassword;




}
