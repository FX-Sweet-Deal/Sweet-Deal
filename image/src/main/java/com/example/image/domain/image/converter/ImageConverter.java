package com.example.image.domain.image.converter;


import com.example.image.domain.image.controller.model.ImageCreateRequest;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.controller.model.ImageUpdateRequest;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageConverter {



    public ImageResponse toResponse(ImageEntity imageEntity) {
        return ImageResponse.builder()
            .id(imageEntity.getId())
            .url(imageEntity.getUrl())
            .originalName(imageEntity.getOriginalName())
            .serverName(imageEntity.getServerName())
            .extension(imageEntity.getExtension())
            .status(imageEntity.getStatus())
            .itemId(imageEntity.getItemId())
            .storeId(imageEntity.getStoreId())
            .userId(imageEntity.getUserId())
            .deleted(imageEntity.isDeleted())
            .registeredAt(imageEntity.getRegisteredAt())
            .updatedAt(imageEntity.getUpdatedAt())
            .build();
    }

}
