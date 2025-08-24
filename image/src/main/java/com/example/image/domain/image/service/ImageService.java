package com.example.image.domain.image.service;

import com.example.image.domain.image.LocalFileStorage;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.ImageRepository;
import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final StoreOwnershipPort ownershipPort;
    private final LocalFileStorage storage;

    @Value("${file.public-base-url}")
    private String publicBaseUrl;

    /** 업로드 (form-data: file, itemId, storeId) */
    @Transactional
    public ImageResponse upload(Long userId, Long itemId, Long storeId, MultipartFile file) {
        requireManager(userId, storeId);
        validateImage(file);

        // 저장 (서버 파일명/확장자/원본명)
        var saved = saveFile(file);
        String url = publicUrl(saved.serverName());

        // 엔티티 저장
        ImageEntity entity = ImageEntity.builder()
            .url(url)
            .originalName(saved.originalName())
            .serverName(saved.serverName())
            .extension(saved.extension())
            .status(ImageStatus.REGISTERED)
            .itemId(itemId)
            .storeId(storeId)
            .userId(userId)
            .deleted(false)
            .build();

        return toResponse(imageRepository.save(entity));
    }

    /** 메타데이터만 수정 (status, itemId 등) */
    @Transactional
    public ImageResponse updateMeta(Long userId, Long imageId,
        @Nullable String status, @Nullable Long itemId) {
        ImageEntity e = getAlive(imageId);
        requireManager(userId, e.getStoreId());

        if (status != null) e.setStatus(ImageStatus.valueOf(status));
        if (itemId != null)  e.setItemId(itemId);

        return toResponse(e);
    }

    @Transactional
    public ImageResponse replaceFile(Long userId, Long imageId, MultipartFile newFile) {
        ImageEntity e = getAlive(imageId);
        requireManager(userId, e.getStoreId());
        validateImage(newFile);

        // 기존 파일 삭제 → 새 파일 저장
        storage.deleteIfExists(e.getServerName());
        var saved = saveFile(newFile);

        e.setServerName(saved.serverName());
        e.setOriginalName(saved.originalName());
        e.setExtension(saved.extension());
        e.setUrl(publicUrl(saved.serverName()));

        return toResponse(e);
    }

    /** 삭제(소프트 삭제 + 물리파일 정리) */
    @Transactional
    public void delete(Long userId, Long imageId) {
        ImageEntity e = getAlive(imageId);
        requireManager(userId, e.getStoreId());
        e.softDelete();
        storage.deleteIfExists(e.getServerName()); // 물리 파일도 제거(원치 않으면 주석)
    }

    // ===== Helpers =====

    private void requireManager(Long userId, Long storeId) {
        if (!ownershipPort.isManagerOrStore(userId, storeId)) {
            throw new RuntimeException("해당 스토어의 매니저가 아닙니다.");
        }
    }

    private void validateImage(MultipartFile f) {
        if (f == null || f.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        if (f.getContentType() == null || !f.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private LocalFileStorage.SavedFile saveFile(MultipartFile file) {
        try {
            // 저장소 구현체에서 파일명/확장자 처리 (UUID 등)
            return storage.save(file);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    private String publicUrl(String serverName) {
        String base = publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
        return base + "/uploads/" + serverName;
    }

    private ImageEntity getAlive(Long id) {
        return imageRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
    }

    private ImageResponse toResponse(ImageEntity e) {
        return new ImageResponse(
            e.getId(), e.getUrl(), e.getOriginalName(), e.getServerName(), e.getExtension(),
            e.getStatus(), e.getItemId(), e.getStoreId(), e.getUserId(),
            e.isDeleted(), e.getRegisteredAt(), e.getUpdatedAt()
        );
    }
}