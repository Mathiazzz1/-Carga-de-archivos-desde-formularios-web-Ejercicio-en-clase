package com.darwinruiz.uspglocalgallerylab.util;

import jakarta.servlet.http.Part;
import java.util.Set;

public class ImageValidator {
    public static final long MAX_BYTES = 3L * 1024 * 1024; // 3 MB
    public static final Set<String> ALLOWED_EXT =
            Set.of(".png",".jpg",".jpeg",".gif",".webp");

    public static boolean isValid(Part part, String fileName) {
        if (part == null || fileName == null || fileName.isBlank()) return false;

        // tamaño
        if (part.getSize() <= 0 || part.getSize() > MAX_BYTES) return false;

        // MIME
        String ct = part.getContentType();
        if (ct == null || !ct.toLowerCase().startsWith("image/")) return false;

        // extensión
        String name = fileName.toLowerCase();
        int dot = name.lastIndexOf('.');
        String ext = (dot >= 0) ? name.substring(dot) : "";
        return ALLOWED_EXT.contains(ext);
    }
}
