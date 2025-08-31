package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.dto.UploadResult;
import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import com.darwinruiz.uspglocalgallerylab.services.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 2 * 1024 * 1024,        // 2 MB en memoria antes de volcar a disco temporal
        maxFileSize = 3L * 1024 * 1024,             // 3 MB por archivo (requisito)
        maxRequestSize = 30L * 1024 * 1024          // límite total de la petición
)
public class UploadServlet extends HttpServlet {
    private ImageService service;

    @Override
    public void init() throws ServletException {
        // Repositorio local por defecto (usa java.io.tmpdir/uspg-local-lab-v2)
        this.service = new ImageService(LocalFileRepository.createDefault());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        UploadResult result = service.uploadLocalImages(req.getParts());

        // Redirige mostrando contadores en la URL para feedback en la JSP
        String redirect = String.format("%s/upload.jsp?uploaded=%d&rejected=%d",
                req.getContextPath(), result.uploaded, result.rejected);
        resp.sendRedirect(redirect);
    }
}
