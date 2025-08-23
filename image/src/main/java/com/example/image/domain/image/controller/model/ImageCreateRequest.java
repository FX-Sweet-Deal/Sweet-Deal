package com.example.image.domain.image.controller.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageCreateRequest {

    @NotNull
    private Long storeId;

    @NotNull
    private Long itemId;

    @NotNull
    @Size(min = 1, max = 1024)
    private String imageUrl;

    @Size(max = 255)
    private String originalName;

    @Size(max = 255)
    private String serverName;

    @Size(max = 20)
    private String extension;


}
