package com.example.user.domain.user.controller.model.register;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @Pattern(
        regexp = "^[0-9a-zA-Z]{1,50}@[0-9a-zA-Z]{1,24}+(\\.[0-9a-zA-Z]+){1,24}$",
        message = "올바른 이메일 형식을 입력하세요 (예: example@domain.com)"
    )
    private String email;

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9])(?!.*[\\\\{}()<>$%^&*_=|`]).{8,100}$",
        message = "대문자, 소문자, 특수문자를 포함하고 8자 이상이어야 합니다."
    )
    private String password;

    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9]{2,50}$",
        message = "한글, 영어 또는 숫자로 2자 이상 50자 이하로 입력해주세요."
    )
    private String name;

    private LocalDate birth;

    @NotBlank(message = "필수 입력 사항입니다.")
    @Size(max = 200, message = "최대 200자까지 입력 가능합니다.")
    private String address;

    @Pattern(
        regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
        message = "올바른 전화번호 형식을 입력하세요 (예: 010-1234-5678)"
    )
    private String phone;
}
