package com.example.item.domain.item.controller.model.register;

import com.example.global.domain.PositiveIntegerCount;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class ItemRegisterRequest {


    @NotNull
    @Pattern(
            regexp = "^[가-힣A-Za-z0-9 ]{1,100}$",
            message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
    )
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "지정된 일시는 현재 또는 미래여야 합니다.")
    private LocalDateTime expiredAt;

    @Pattern(regexp = "^[0-9]$", message="숫자만 입력할 수 있습니다.")
    private Long price;

    @Pattern(regexp = "^[0-9]$", message="숫자만 입력할 수 있습니다.")
    private Integer quantity;

}
