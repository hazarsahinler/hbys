package com.example.demo.controller;

import com.example.demo.bus.hastaBus.HastaService;
import com.example.demo.bus.hastaBus.HastaServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/hasta")
public class HastaController {
    private final HastaService hastaService;
    private final HastaServiceImpl hastaServiceImpl;

    public HastaController(HastaService hastaService, HastaServiceImpl hastaServiceImpl) {
        this.hastaService = hastaService;
        this.hastaServiceImpl = hastaServiceImpl;
    }


    //tüm hastaları al
    @GetMapping()
    public void getAllHasta(HttpServletResponse response) throws IOException {
        JSONArray respObj = hastaService.getAllHastalar();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
            response.getWriter().write(jsonObject.toString());
        }
    }

    @RequestMapping("/add")
    public void addHasta(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer firmaId;
        String tcKimlikNo = request.getParameter("tcKimlikNo");
        String hastaIsim = request.getParameter("hastaIsim");
        String hastaSoyisim = request.getParameter("hastaSoyisim");
        String id = request.getParameter("firmaId");
        if (id != null && id != "") {
            firmaId = Integer.parseInt(id);
        } else {
            firmaId = null;
        }
        String sigortaTipi = request.getParameter("sigortaTipi");
        String telNo = request.getParameter("telNo");
        String mail = request.getParameter("mail");
        Date hastaVizitGiris = parseDate(request.getParameter("girisTarihi"));
        JSONObject jsonObject = hastaService.addHasta(tcKimlikNo, hastaIsim, hastaSoyisim, firmaId, sigortaTipi, telNo, mail, hastaVizitGiris);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonObject != null) {
            response.getWriter().write(jsonObject.toString());
        }


    }


    //İstenilen bilgiye göre tabloda arama yapıp hasta listesi geri döndürür
    @RequestMapping("/search")
    public void findByParam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Parametreleri oku

        String kimlikNo = request.getParameter("kimlikNo");
        String isim = request.getParameter("isim");
        String soyisim = request.getParameter("soyisim");
        String sigortaTipi = request.getParameter("sigortaTipi");
        String telefon = request.getParameter("telNo");
        String email = request.getParameter("mail");

        // HastaService'i kullanarak sonuçları getir
        JSONArray respObj = hastaService.findByParam(kimlikNo, isim, soyisim,sigortaTipi,telefon, email);

        // Sonucu response'a yaz
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
            response.getWriter().write(jsonObject.toString());
        }
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}






