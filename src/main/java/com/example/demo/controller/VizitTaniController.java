package com.example.demo.controller;

import com.example.demo.bus.vizitTaniBus.VizitTaniService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

@RestController
@RequestMapping("/api/vizitTani")
public class VizitTaniController {
    private final VizitTaniService vizitTaniService;

    public VizitTaniController(VizitTaniService vizitTaniService) {
        this.vizitTaniService = vizitTaniService;
    }
    @RequestMapping("/getTaniByVizitId")
    public void getTanilarByVizitId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer vizitId = Integer.parseInt(req.getParameter("hastaVizitId"));
        JSONArray jsonArray = vizitTaniService.getVizitTani(vizitId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonArray.toString());

    }

    @RequestMapping("/addTani")
    public void addTani(HttpServletResponse response, HttpServletRequest httpServletRequest) throws IOException {

        Integer hastaVizitId = Integer.valueOf(httpServletRequest.getParameter("hastaVizitId"));
        Integer taniId = Integer.valueOf(httpServletRequest.getParameter("taniId"));
        Date taniGirisTarihi = parseDate(httpServletRequest.getParameter("girisTarihi"));

        JSONObject jsonObject =vizitTaniService.addTaniToVizit(hastaVizitId,taniId,taniGirisTarihi);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonObject != null) {
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