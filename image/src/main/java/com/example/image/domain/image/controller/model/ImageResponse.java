package com.example.image.domain.image.controller.model;


import com.example.image.domain.image.repository.enums.ImageKind;
import com.example.image.domain.image.repository.enums.ImageStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private Long id;

    private String url;

    private String originalName;

    private String serverName;

    private String extension;

    private ImageStatus status;

    private ImageKind imageKind;

    private Long itemId;

    private Long storeId;

    private Long userId;

    private boolean deleted;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;
}


