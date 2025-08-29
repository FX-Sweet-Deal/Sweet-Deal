package com.example.image.domain.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileStorage {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public SavedFile save(MultipartFile file) throws IOException {
        Files.createDirectories(Path.of(uploadDir));
        String original = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')+1).toLowerCase() : "bin";
        String serverName = UUID.randomUUID() + "." + ext;
        file.transferTo(Path.of(uploadDir, serverName));
        return new SavedFile(serverName, original, ext);
    }

    public void deleteIfExists(String serverName) {
        try { Files.deleteIfExists(Path.of(uploadDir, serverName)); } catch (Exception ignore) {}
    }

    public record SavedFile(String serverName, String originalName, String extension) {}

}
