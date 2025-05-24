package com.example.item.domain.item.controller.model.register;

import java.time.LocalDateTime;

public class ItemRegisterRequest {
    /**
     * 작성 중
     */
    @Pattern(regexp = "")
    private String name;

    private LocalDateTime expired_at;

    private float price;



}
