package com.example.image.domain.image.controller.model;


import com.example.image.domain.image.repository.enums.ImageStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {

    private Long id;

    private String imageUrl;

    private String originalName;

    private String serverName;

    private String extension;

    private Long itemId;

    private Long storeId;

    private Long userId;

    private boolean deleted;

    public ImageStatus status;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

}
