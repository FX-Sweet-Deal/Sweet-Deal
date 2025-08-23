package com.example.image.domain.image.repository;



import com.example.image.domain.image.repository.enums.ImageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String imageUrl;

    private String originalName;

    private String serverName;

    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ImageStatus status;

    @Column(nullable = false)
    private Long itemId;   // 상품 FK

    @Column(nullable = false)
    private Long storeId;  // 권한 체크용

    @Column(nullable = false)
    private Long userId;   // 등록자 (스토어 매니저)

    @Column(nullable = false)
    private boolean deleted;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.registeredAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = ImageStatus.ACTIVE;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.status = ImageStatus.DELETED;
    }
}
