package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private final Path avatarDir;

    public AvatarStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.avatarDir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("avatars");
    }

    @PostConstruct
    void ensureDir() throws IOException {
        Files.createDirectories(avatarDir);
    }

    /**
     * Saves the file and returns a public path (e.g. {@code /uploads/avatars/uuid.jpg}).
     */
    public String store(MultipartFile file, UUID userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("avatar file is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("avatar must be an image (jpeg, png, gif, or webp)");
        }

        String ext = extensionForContentType(contentType);
        String filename = userId + "-" + UUID.randomUUID() + ext;
        Path dest = avatarDir.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/avatars/" + filename;
    }

    public void deleteIfExists(String avatarUrl) {
        if (avatarUrl == null || !avatarUrl.startsWith("/uploads/avatars/")) {
            return;
        }
        String name = avatarUrl.substring("/uploads/avatars/".length());
        if (name.isEmpty() || name.contains("..") || name.contains("/") || name.contains("\\")) {
            return;
        }
        Path file = avatarDir.resolve(name).normalize();
        if (!file.startsWith(avatarDir)) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
            // best-effort cleanup
        }
    }

    private static String extensionForContentType(String contentType) {
        String ct = contentType.toLowerCase(Locale.ROOT);
        return switch (ct) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".bin";
        };
    }
}
