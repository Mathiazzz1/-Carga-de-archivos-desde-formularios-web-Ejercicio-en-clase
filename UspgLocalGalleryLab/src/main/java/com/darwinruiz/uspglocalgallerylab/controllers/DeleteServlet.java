package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    private LocalFileRepository repo;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String rel = req.getParameter("path");
        if (rel == null || rel.isBlank() || rel.contains("..")) {
            resp.sendError(400, "path inválido");
            return;
        }
        try {
            repo.delete(rel);
        } catch (IOException ignored) {}

        String page = req.getParameter("page");
        String size = req.getParameter("size");
        String qs = "";
        if (page != null || size != null) {
            qs = "?";
            if (page != null) qs += "page=" + page;
            if (size != null) qs += (qs.length()>1 ? "&" : "") + "size=" + size;
        }
        resp.sendRedirect(req.getContextPath() + "/list" + qs);
    }
}