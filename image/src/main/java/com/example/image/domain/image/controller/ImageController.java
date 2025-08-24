package com.example.image.domain.image.controller;


import com.example.global.anntation.UserSession;
import com.example.global.resolver.User;
import com.example.image.domain.image.business.ImageBusiness;
import com.example.image.domain.image.controller.model.ImageCreateRequest;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.controller.model.ImageUpdateRequest;
import com.example.image.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "[사진 등록]")
    public ImageResponse upload(
        @Parameter(hidden = true) @UserSession User user,   // Swagger에 노출 안 되게
        @RequestPart("file") MultipartFile file,            // 파일
        @RequestParam("itemId") Long itemId,                // 텍스트 필드는 RequestParam 권장
        @RequestParam("storeId") Long storeId
    ) {
        return imageService.upload(user.getId(), itemId, storeId, file);
    }
    /** 메타데이터 수정(status, itemId 등 일부만) */
    @PatchMapping("/{imageId}")
    @Operation(summary = "[데이터 수정]")
    public ImageResponse updateMeta(@UserSession User user,
        @PathVariable Long imageId,
        @RequestParam(required=false) String status,
        @RequestParam(required=false) Long itemId){
        return imageService.updateMeta(user.getId(), imageId, status, itemId);
    }
}

