package com.example.user.domain.user.controller.model.update;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9]{2,50}$",
        message = "한글, 영어 또는 숫자로 2자 이상 50자 이하로 입력해주세요."
    )
    private String name;

    @Schema(description = "생년월일", example = "1999-12-31")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
