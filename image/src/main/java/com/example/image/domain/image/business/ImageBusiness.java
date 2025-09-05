package com.example.image.domain.image.business;


import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.controller.model.RegisterImageRequest;
import com.example.image.domain.image.controller.model.UpdateImageRequest;
import com.example.image.domain.image.converter.ImageConverter;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.enums.ImageKind;
import com.example.image.domain.image.service.ImageService;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageBusiness {


    private final ImageConverter imageConverter;

    private final ImageService imageService;

    public ImageResponse upload(Long userId, Long itemId, Long storeId, MultipartFile file, ImageKind imageKind){

        ImageEntity imageEntity = imageService.upload(userId, itemId, storeId, file, imageKind);

        return imageConverter.toResponse(imageEntity);
    }

    public ImageResponse updateMeta(
        Long userId,
        Long imageId,
        ImageKind imageKind,
        @Nullable String status,
        @Nullable Long itemId) {

        ImageEntity imageEntity = imageService.updateMeta(userId, imageId, imageKind, status, itemId);

        return imageConverter.toResponse(imageEntity);

    }

    public ImageResponse replaceFile(Long userId, Long imageId, MultipartFile newFile, ImageKind imageKind) {

        ImageEntity imageEntity = imageService.replaceFile(userId, imageId, newFile, imageKind);

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

    @Transactional
    @KafkaListener(topics = "image.register", groupId = "image-groupId")
    public void handlerRegisterImage(@Payload RegisterImageRequest req) {

        List<String> serverNames = req.getServerNames();
        if(req.getImageKind() == ImageKind.ITEM) {
            registerItemImages(req.getItemId(), serverNames);

        } else if(req.getImageKind() == ImageKind.STORE) {
            registerStoreImages(req.getStoreId(), serverNames);
        }
    }

    @Transactional
    @KafkaListener(topics = "image.update", groupId = "image-groupId")
    public void handlerUpdateImage(@Payload UpdateImageRequest req) {

        List<String> serverNames = req.getServerNames();

        if (req.getImageKind() == ImageKind.ITEM) {
            updateItemImage(req.getItemId(), serverNames);
        }

        if (req.getImageKind() == ImageKind.STORE) {
            updateStoreImage(req.getStoreId(), serverNames);
        }

    }

    private void registerItemImages(Long itemId, List<String> serverNames) {
        serverNames.stream().forEach(serverName -> {
            ImageEntity itemImage = imageService.getByServerName(serverName);
            itemImage.setItemId(itemId);
            itemImage.setImageKind(ImageKind.ITEM);
            imageService.save(itemImage);
        });
    }

    private void registerStoreImages(Long storeId, List<String> serverNames) {
        serverNames.stream().forEach(serverName -> {
            ImageEntity storeImage = imageService.getByServerName(serverName);
            storeImage.setStoreId(storeId);
            storeImage.setImageKind(ImageKind.STORE);
            imageService.save(storeImage);
        });
    }

    private void updateItemImage(Long itemId, List<String> updateServerNames) {
        List<ImageEntity> existsItemImages = imageService.getByItem(itemId);

        existsItemImages.stream().forEach(existsItemImage -> {
            if (!updateServerNames.contains(existsItemImage.getServerName())) {
                imageService.delete(existsItemImage.getUserId(), existsItemImage.getId());
            } else {
                updateServerNames.remove(existsItemImage.getServerName());
            }

        });

        updateServerNames.stream().forEach(updateServerName -> {
            ImageEntity image = imageService.getByServerName(updateServerName);
            image.setImageKind(ImageKind.ITEM);
            image.setItemId(itemId);
            imageService.save(image);
        });
    }

    private void updateStoreImage(Long storeId, List<String> updateServerNames) {
        List<ImageEntity> existsStoreImages = imageService.getByStore(storeId);

        existsStoreImages.stream().forEach(existsStoreImage -> {
            if (!updateServerNames.contains(existsStoreImage.getServerName())) {
                imageService.delete(existsStoreImage.getUserId(), existsStoreImage.getId());
            } else {
                updateServerNames.remove(existsStoreImage.getServerName());
            }

        });

        updateServerNames.stream().forEach(updateServerName -> {
            ImageEntity image = imageService.getByServerName(updateServerName);
            image.setImageKind(ImageKind.STORE);
            image.setStoreId(storeId);
            imageService.save(image);
        });
    }


}
