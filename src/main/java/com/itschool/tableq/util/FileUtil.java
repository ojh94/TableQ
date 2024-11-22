package com.itschool.tableq.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUtil {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png");

    public static boolean isValidFileSize(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }

    public static boolean isValidFileType(MultipartFile file, String typePrefix) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith(typePrefix);
    }

    public static String getFileExtension(MultipartFile file) {
        String sanitizedFileName = sanitizeFileName(file.getOriginalFilename());
        return  "." + sanitizedFileName.substring(sanitizedFileName.lastIndexOf(".") + 1);
    }

    // 파일 이름 특수 문자를 통한 공격 및 코드 주입 방지
    public static String sanitizeFileName(String originalFileName) {
        return originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    public static boolean validateImageFileExtension(MultipartFile file) {
        String extension = getFileExtension(file);

        return ALLOWED_IMAGE_EXTENSIONS.contains(extension) && isValidFileType(file, "image/");
    }
}
