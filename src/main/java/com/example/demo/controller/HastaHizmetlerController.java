package com.example.demo.controller;

import com.example.demo.bus.hastaHizmetBus.HastaHizmetlerService;
import com.example.demo.dao.HizmetDAO;
import com.example.demo.entity.HastaHizmetler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class HastaHizmetlerController {
    private final HastaHizmetlerService hastaHizmetlerService;
    private final HizmetDAO hizmetDAO;

    public HastaHizmetlerController(HastaHizmetlerService hastaHizmetlerService, HizmetDAO hizmetDAO) {
        this.hastaHizmetlerService = hastaHizmetlerService;
        this.hizmetDAO = hizmetDAO;
    }

    @RequestMapping("/hizmet/add")
    public void addHizmet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer hastaVizitId = Integer.parseInt(request.getParameter("hastaVizitId"));
        Integer hizmetId = Integer.parseInt(request.getParameter("hizmetId"));
        Integer personelId = Integer.parseInt(request.getParameter("personelId"));
        Date hizmetTarihi = parseDate(request.getParameter("hizmetTarihi"));
        JSONObject jsonObject = hastaHizmetlerService.addHizmet(hastaVizitId, hizmetId, personelId, hizmetTarihi);
        response.setContentType("application/json");
        if (jsonObject != null && jsonObject.size() > 0) {
            response.getWriter().write(jsonObject.toString());
        } else {
            jsonObject.put("message", "No record found");
            response.getWriter().write(jsonObject.toString());
        }
    }
    @RequestMapping("/hizmet/delete")
    public void deleteHizmet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer hastaHizmetId = Integer.parseInt(request.getParameter("hastaHizmetId"));
        JSONObject jsonObject = hastaHizmetlerService.deleteHizmet(hastaHizmetId);
        response.setContentType("application/json");
        response.getWriter().write(jsonObject.toString());
    }
    @RequestMapping("/hizmet/getHizmetlerByVizitId")
    public void getHizmetlerByVizitId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer vizitId = Integer.parseInt(request.getParameter("vizitId"));
        JSONArray jsonArray = hastaHizmetlerService.getHizmetlerByVizitId(vizitId);
        response.setContentType("application/json");
        response.getWriter().write(jsonArray.toString());
    }


    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
