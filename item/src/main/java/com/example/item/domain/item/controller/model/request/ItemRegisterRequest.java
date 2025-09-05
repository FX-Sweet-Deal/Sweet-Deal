package com.example.item.domain.item.controller.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemRegisterRequest {


    @NotNull
    @Pattern(
            regexp = "^[가-힣A-Za-z0-9 ]{1,100}$",
            message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
    )
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "지정된 일시는 현재 또는 미래여야 합니다.")
    private LocalDateTime expiredAt;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Long price;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private Long quantity;

    @NotNull
    private Long storeId;

    @NotNull
    private List<String> serverNames;


}
