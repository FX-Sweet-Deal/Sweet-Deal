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

    @NotNull
    private Long storeId;

    @Size(max = 1024)
    private String imageUrl;

    @Size(max = 255)
    private String originalName;

    @Size(max = 255)
    private String serverName;

    @Size(max = 20)
    private String extension;

    private ImageStatus status;
}
