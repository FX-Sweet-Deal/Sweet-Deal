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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name="image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100)
    private String serverName;

    @Column(length=100)
    private String originalName;

    // DB가 image_url 이면 name 지정 필수
    @Column(nullable=false, length=200)
    private String url;

    @Column(length=20)
    private String extension; // 스키마가 extends면 그대로

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=50)
    private ImageStatus status;

    @Column(nullable=false)
    private Long itemId;

    @Column(nullable=false)
    private Long storeId;

    @Column(nullable=false)
    private Long userId;

    @Column(nullable=false)
    private boolean deleted;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    @PrePersist void onCreate(){
        var now=LocalDateTime.now();
        registeredAt=now;
        updatedAt=now;
        if(status==null) status=ImageStatus.REGISTERED;
    }

    @PreUpdate  void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

    public void softDelete(){
        this.deleted=true;
        this.status=ImageStatus.DELETED;
    }
}