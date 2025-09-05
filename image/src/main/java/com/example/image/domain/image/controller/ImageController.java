package com.example.image.domain.image.controller;


import com.example.global.annotation.UserSession;
import com.example.global.resolver.User;
import com.example.image.domain.image.business.ImageBusiness;
import com.example.image.domain.image.controller.model.ImageResponse;
import com.example.image.domain.image.repository.enums.ImageKind;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageBusiness imageBusiness;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "[사진 등록]")
    public ImageResponse upload(
        @Parameter(hidden = true) @UserSession User user,   // Swagger에 노출 안 되게
        @RequestPart("file") MultipartFile file,            // 파일
        @RequestParam("itemId") Long itemId,                // 텍스트 필드는 RequestParam 권장
        @RequestParam("storeId") Long storeId,
        @RequestParam("imageKind") ImageKind imageKind
    ) {
        return imageBusiness.upload(user.getId(), itemId, storeId, file, imageKind);
    }

    /** 파일 교체 */
    @PutMapping(value="/{imageId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "[파일 교체]")
    public ImageResponse replace(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long imageId,
        @RequestPart("file") MultipartFile file,
        @RequestParam("imageKind") ImageKind imageKind
    ){
        return imageBusiness.replaceFile(user.getId(), imageId, file, imageKind);
    }

    /** 메타데이터 수정(status, itemId 등 일부만) */
    @PatchMapping("/{imageId}")
    @Operation(summary = "[데이터 수정]")
    public ImageResponse updateMeta(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long imageId,
        @RequestParam("imageKind") ImageKind imageKind,
        @RequestParam(required=false) String status,
        @RequestParam(required=false) Long itemId){
        return imageBusiness.updateMeta(user.getId(), imageId, imageKind, status, itemId);
    }

    /** 삭제(소프트) */
    @DeleteMapping("/{imageId}")
    @Operation(summary = "[삭제]")
    public void delete(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long imageId){
        imageBusiness.delete(user.getId(), imageId);
    }

    /** 조회 */
    @GetMapping("/{imageId}")
    @Operation(summary = "[imageId 조희]")
    public ImageResponse getById(@PathVariable Long imageId){
        return imageBusiness.getById(imageId);
    }

    @GetMapping("/by-item/{itemId}")
    @Operation(summary = "itemId 조회")
    public List<ImageResponse> byItem(@PathVariable Long itemId){
        return imageBusiness.getByItem(itemId);
    }

    @GetMapping("/by-store/{storeId}")
    @Operation(summary = "storeId 조희")
    public List<ImageResponse> byStore(@PathVariable Long storeId){
        return imageBusiness.getByStore(storeId);
    }
}

