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

    public static void validateFileList(List<MultipartFile> fileList, long maxImageFileSize) {
        try {
            fileList.stream().forEach((MultipartFile file) -> {
                validateFile(file, maxImageFileSize);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateFile(MultipartFile file, long maxImageFileSize) {
        // boolean result = false;

        // 제네릭 타입 Req에 맞게 요청 객체를 변환

        // 파일 크기 체크
        if (!FileUtil.isValidFileSize(file, maxImageFileSize))
            throw new RuntimeException("파일 크기가 제한을 초과합니다.");

        // 파일 유형 확인
        if (!FileUtil.validateImageFileExtension(file))
            throw new RuntimeException("잘못된 파일 유형입니다. (jpg, jpeg, png 확장자만 허용)");

        // return result = true;
    }
}
