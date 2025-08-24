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

    @Size(max = 200)
    private String url;

    @Size(max = 100)
    private String originalName;

    @Size(max = 100)
    private String serverName;

    @Size(max = 20)
    private String extension;

    private ImageStatus status;
}
