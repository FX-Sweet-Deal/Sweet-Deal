package com.example.image.domain.image.business;


import com.example.image.domain.image.controller.model.ImageCreateRequest;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.converter.ImageConverter;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.service.ImageService;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageBusiness {


    private final ImageConverter imageConverter;

    private final ImageService imageService;

    public ImageResponse upload(Long userId, Long itemId, Long storeId, MultipartFile file){

        ImageEntity imageEntity = imageService.upload(userId, itemId, storeId, file);

        return imageConverter.toResponse(imageEntity);
    }

    public ImageResponse updateMeta(
        Long userId,
        Long imageId,
        @Nullable String status,
        @Nullable Long itemId) {

        ImageEntity imageEntity = imageService.updateMeta(userId, imageId, status, itemId);

        return imageConverter.toResponse(imageEntity);

    }

    public ImageResponse replaceFile(Long userId, Long imageId, MultipartFile newFile) {

        ImageEntity imageEntity = imageService.replaceFile(userId, imageId, newFile);

        return imageConverter.toResponse(imageEntity);

    }


    public void delete(Long userId, Long imageId) {
        imageService.delete(userId, imageId);
    }

    public ImageResponse getById(Long id) {
        return imageConverter.toResponse(imageService.getById(id));
    }


    /** itemId로 이미지 조회 (Controller -> Business -> Service) */
    public List<ImageResponse> getByItem(Long itemId) {
        List<ImageEntity> entities = imageService.getByItem(itemId);
        return entities.stream().map(imageConverter::toResponse).toList();
    }

    /** storeId로 이미지 조회 (Controller -> Business -> Service) */
    public List<ImageResponse> getByStore(Long storeId) {
        List<ImageEntity> entities = imageService.getByStore(storeId);
        return entities.stream().map(imageConverter::toResponse).toList();
    }







}
