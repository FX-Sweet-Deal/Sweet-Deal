package com.example.image.domain.image.service;

import com.example.image.domain.image.LocalFileStorage;
import com.example.image.domain.image.converter.ImageConverter;
import com.example.image.domain.image.repository.ImageEntity;
import com.example.image.domain.image.repository.ImageRepository;
import com.example.image.domain.image.repository.enums.ImageKind;
import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final StoreOwnershipPort ownershipPort;
    private final LocalFileStorage storage;

    private final ImageConverter imageConverter;


    @Value("${file.public-base-url}")
    private String publicBaseUrl;

    @Value("${file.public-path}")
    private String publicPath;

    /** 업로드 (form-data: file, itemId, storeId) */
    @Transactional
    public ImageEntity upload(Long userId, Long itemId, Long storeId, MultipartFile file, ImageKind imageKind) {

        log.info("itemId {}, storeId {}, imageKind {}", itemId, storeId, imageKind);
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
            .imageKind(imageKind)
            .itemId(itemId)
            .storeId(storeId)
            .userId(userId)
            .deleted(false)
            .build();

        return imageRepository.save(entity);
    }

    /** 메타데이터만 수정 (status, itemId 등) */
    @Transactional
    public ImageEntity updateMeta(
        Long userId,
        Long imageId,
        ImageKind imageKind,
        @Nullable String status,
        @Nullable Long itemId
    ) {
        ImageEntity imageEntity = getAlive(imageId);
        requireManager(userId, imageEntity.getStoreId());
        imageEntity.setImageKind(imageKind);

        if (status != null) imageEntity.setStatus(ImageStatus.valueOf(status));
        if (itemId != null)  imageEntity.setItemId(itemId);

        return imageEntity;
    }

    /** 파일 교체 (물리 파일 바꾸고 URL/메타 갱신) */
    @Transactional
    public ImageEntity replaceFile(Long userId, Long imageId, MultipartFile newFile, ImageKind imageKind) {
        ImageEntity imageEntity = getAlive(imageId);
        requireManager(userId, imageEntity.getStoreId());
        validateImage(newFile);

        // 기존 파일 삭제 → 새 파일 저장
        storage.deleteIfExists(imageEntity.getServerName());
        var saved = saveFile(newFile);

        imageEntity.setServerName(saved.serverName());
        imageEntity.setOriginalName(saved.originalName());
        imageEntity.setExtension(saved.extension());
        imageEntity.setUrl(publicUrl(saved.serverName()));
        imageEntity.setImageKind(imageKind);

        return imageEntity;
    }


    /** 삭제(소프트 삭제 + 물리파일 정리) */
    @Transactional
    public void delete(Long userId, Long imageId) {
        ImageEntity imageEntity = getAlive(imageId);
        requireManager(userId, imageEntity.getStoreId());
        imageEntity.softDelete();
        storage.deleteIfExists(imageEntity.getServerName()); // 물리 파일도 제거(원치 않으면 주석)
    }

    /** 단건 조회 */
    @Transactional
    public ImageEntity getById(Long id) {
        return getAlive(id);
    }

    /** 상품별 조회 */
    @Transactional
    public List<ImageEntity> getByItem(Long itemId) {
        return imageRepository.findByItemIdAndDeletedFalse(itemId);
    }



    /** 스토어별 조회 */
    @Transactional
    public List<ImageEntity> getByStore(Long storeId) {
        return imageRepository.findByStoreIdAndDeletedFalse(storeId);
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
            var saved = storage.save(file);
            log.info("📂 File saved - serverName={}, originalName={}, extension={}",
                saved.serverName(), saved.originalName(), saved.extension());
            return saved;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    private String publicUrl(String serverName) {
        String base = publicBaseUrl.endsWith("/") ?
            publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
        String path = publicPath.startsWith("/") ? publicPath : "/" + publicPath;

        String fullUrl = base + path + serverName;
        log.info("🌐 Generated public URL = {}", fullUrl);

        return fullUrl;
    }
    private ImageEntity getAlive(Long id) {
        return imageRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
    }

    public ImageEntity getByServerName(String serverName) {
        return imageRepository.findByServerName(serverName)
            .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
    }

    public ImageEntity save(ImageEntity imageEntity) {
        return imageRepository.save(imageEntity);
    }
}