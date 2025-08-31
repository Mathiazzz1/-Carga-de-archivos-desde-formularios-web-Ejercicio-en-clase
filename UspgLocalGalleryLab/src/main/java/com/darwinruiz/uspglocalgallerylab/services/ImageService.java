package com.darwinruiz.uspglocalgallerylab.services;

import com.darwinruiz.uspglocalgallerylab.dto.UploadResult;
import com.darwinruiz.uspglocalgallerylab.repositories.IFileRepository;
import com.darwinruiz.uspglocalgallerylab.util.ImageValidator;
import com.darwinruiz.uspglocalgallerylab.util.NamePolicy;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageService {
    private final IFileRepository repo;

    public ImageService(IFileRepository repo) {
        this.repo = repo;
    }

    public UploadResult uploadLocalImages(Collection<Part> parts) {
        int ok = 0, bad = 0;
        List<String> saved = new ArrayList<>();
        if (parts == null) return new UploadResult(0, 0, saved);

        java.time.LocalDate today = java.time.LocalDate.now();
        String subdir = NamePolicy.datedSubdir(today);

        for (Part p : parts) {
            try {
                if (!"file".equals(p.getName()) || p.getSize() <= 0) continue;

                String submitted = p.getSubmittedFileName();
                String norm = NamePolicy.normalize(submitted);

                if (!ImageValidator.isValid(p, norm)) {
                    bad++;
                    continue;
                }

                try (InputStream in = p.getInputStream()) {
                    String rel = repo.save(subdir, norm, in);
                    saved.add(rel);
                    ok++;
                }
            } catch (Exception ex) {
                bad++;
            }
        }
        return new UploadResult(ok, bad, saved);
    }
}
