package com.darwinruiz.uspglocalgallerylab.util;

public class NamePolicy {

    /** Normaliza el nombre: minúsculas, espacios→-, solo [a-z0-9._-], máx 80 caracteres. */
    public static String normalize(String original) {
        if (original == null) return "archivo";

        // solo nombre base (quitar ruta)
        String base = original.replace("\\", "/");
        int slash = base.lastIndexOf('/');
        if (slash >= 0) base = base.substring(slash + 1);

        // minúsculas + espacios → '-'
        base = base.toLowerCase().trim().replace(' ', '-');

        // separar stem / ext
        int dot = base.lastIndexOf('.');
        String ext = "";
        String stem = base;
        if (dot > 0 && dot < base.length() - 1) {
            ext = base.substring(dot);
            stem = base.substring(0, dot);
        }

        // permitir solo [a-z0-9._-]
        stem = stem.replaceAll("[^a-z0-9._-]", "");
        ext  = ext.replaceAll("[^a-z0-9._-]", "");

        if (stem.isBlank()) stem = "archivo";

        // limitar a 80 caracteres total (incluida la extensión)
        int maxLen = 80;
        String candidate = stem + ext;
        if (candidate.length() > maxLen) {
            int keep = Math.max(1, maxLen - ext.length());
            stem = stem.substring(0, Math.min(keep, stem.length()));
            candidate = stem + ext;
        }
        return candidate;
    }

    /** Subcarpeta por fecha: imagenes/yyyy/MM/dd */
    public static String datedSubdir(java.time.LocalDate d) {
        return String.format("imagenes/%04d/%02d/%02d", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }
}
