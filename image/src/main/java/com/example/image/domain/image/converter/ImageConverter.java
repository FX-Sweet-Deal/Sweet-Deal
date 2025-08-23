package com.example.image.domain.image.converter;


import com.example.image.domain.image.controller.model.ImageCreateRequest;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.controller.model.ImageUpdateRequest;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.persistence.Converter;

@Converter
public class ImageConverter {


    public ImageEntity toEntity(ImageCreateRequest req, Long currentUserId){
        return ImageEntity.builder()
            .imageUrl(req.getImageUrl())
            .originalName(req.getOriginalName())
            .serverName(req.getServerName())
            .extension(req.getExtension())
            .status(ImageStatus.ACTIVE)
            .itemId(req.getItemId())
            .storeId(req.getStoreId())
            .userId(currentUserId)
            .deleted(false)
            .build();
    }

    public void applyUpdate(ImageEntity e, ImageUpdateRequest req) {
        if (req.getImageUrl() != null){
            e.setImageUrl(req.getImageUrl());
        }

        if (req.getOriginalName() != null) {
            e.setOriginalName(req.getOriginalName());
        }

        if (req.getServerName() != null) {
            e.setServerName(req.getServerName());
        }

        if (req.getExtension() != null){
            e.setExtension(req.getExtension());
        }

        if (req.getStoreId() != null) {
            e.setStatus(req.getStatus());
        }
    }

    public ImageResponse toResponse(ImageEntity e) {
        return ImageResponse.builder()
            .id(e.getId())
            .imageUrl(e.getImageUrl())
            .originalName(e.getOriginalName())
            .serverName(e.getServerName())
            .extension(e.getExtension())
            .itemId(e.getItemId())
            .storeId(e.getItemId())
            .userId(e.getUserId())
            .deleted(e.isDeleted())
            .status(e.getStatus())
            .registeredAt(e.getRegisteredAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
    
}
