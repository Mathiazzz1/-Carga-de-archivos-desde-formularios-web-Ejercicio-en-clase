package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private LocalFileRepository repo;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        List<String> all = repo.listByExtensionsRecursive("imagenes", ".png", ".jpg", ".jpeg", ".gif", ".webp");

        // TODO-4: leer page y size, calcular fromIndex/toIndex y sublista
        int page = 1, size = 12; // defaults
        try {
            String pStr = req.getParameter("page");
            String sStr = req.getParameter("size");
            if (pStr != null) page = Math.max(1, Integer.parseInt(pStr));
            if (sStr != null) size = Math.max(1, Integer.parseInt(sStr));
        } catch (NumberFormatException ignore) {}

        int total = all.size();
        int from = Math.max(0, Math.min((page - 1) * size, total));
        int to = Math.max(from, Math.min(from + size, total));
        List<String> pageItems = all.subList(from, to);

        req.setAttribute("localImages", pageItems);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("total", total);
        req.setAttribute("totalPages", (int)Math.ceil(total / (double)size));
        // req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/gallery.jsp").forward(req, resp);
    }
}
