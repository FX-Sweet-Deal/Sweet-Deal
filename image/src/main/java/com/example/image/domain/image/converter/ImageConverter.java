package com.example.image.domain.image.converter;


import com.example.image.domain.image.controller.model.ImageCreateRequest;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.controller.model.ImageUpdateRequest;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageConverter {


    public static ImageEntity toEntity(ImageCreateRequest req, Long userId) {
        return ImageEntity.builder()
            .url(req.getUrl())
            .originalName(req.getOriginalName())
            .serverName(req.getServerName())
            .extension(req.getExtension())
            .status(req.getStatus())
            .itemId(req.getItemId())
            .storeId(req.getStoreId())
            .userId(userId)
            .deleted(false)
            .build();
    }

    public static void applyUpdate(ImageEntity e, ImageUpdateRequest req) {
        if (req.getUrl() != null)
            e.setUrl(req.getUrl());
        if (req.getOriginalName() != null)
            e.setOriginalName(req.getOriginalName());
        if (req.getServerName() != null)
            e.setServerName(req.getServerName());
        if (req.getExtension() != null)
            e.setExtension(req.getExtension());
        if (req.getStatus() != null)
            e.setStatus(req.getStatus());
    }

    public static ImageResponse toResponse(ImageEntity e) {
        return ImageResponse.builder()
            .id(e.getId())
            .url(e.getUrl())
            .originalName(e.getOriginalName())
            .serverName(e.getServerName())
            .extension(e.getExtension())
            .status(e.getStatus())
            .itemId(e.getItemId())
            .storeId(e.getStoreId())
            .userId(e.getUserId())
            .deleted(e.isDeleted())
            .registeredAt(e.getRegisteredAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
