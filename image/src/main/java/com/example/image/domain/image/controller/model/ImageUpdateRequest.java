package com.example.image.domain.image.controller.model;


import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateRequest {

    private Long itemId;

    private Long storeId;

}
