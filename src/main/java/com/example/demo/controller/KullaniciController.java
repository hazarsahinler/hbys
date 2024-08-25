package com.example.demo.controller;

import com.example.demo.bus.kullaniciBus.KullaniciService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/kullanici")
public class KullaniciController {
    private final KullaniciService kullaniciService;

    public KullaniciController(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }


    @RequestMapping("/ekle")
    public void kullaniciKayit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String kullaniciAdi = request.getParameter("kullaniciAdi");
        String kullaniciSifre = request.getParameter("kullaniciSifre");
        Integer personelId = Integer.parseInt(request.getParameter("personelId"));

        JSONObject jsonObject = kullaniciService.kullaniciKayit(kullaniciAdi, kullaniciSifre, personelId);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());

    }
    @RequestMapping("/giris")
    public void kullaniciGiris(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String kullaniciAdi = request.getParameter("kullaniciAdi");
        String kullaniciSifre = request.getParameter("kullaniciSifre");
        JSONObject jsonObject = kullaniciService.kullaniciGiris(kullaniciAdi, kullaniciSifre);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());

    }
}
