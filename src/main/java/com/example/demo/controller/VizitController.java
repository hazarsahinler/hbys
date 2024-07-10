package com.example.demo.controller;

import com.example.demo.Util.Utility;
import com.example.demo.bus.vizitBus.VizitService;
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
@RequestMapping("/api/vizit")
public class VizitController {

    private final VizitService vizitService;
    private final Utility utils;

    public VizitController(VizitService vizitService, Utility utils) {
        this.vizitService = vizitService;
        this.utils = utils;
    }

    @RequestMapping("/deleteVizit")
    public void deleteVizit(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("hastaVizitId"));
        JSONObject jsonObject = vizitService.deleteVizit(id);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonObject != null) {
            response.getWriter().write(jsonObject.toString());
        }
    }

    @RequestMapping("/addVizit")
    public void addVizit(HttpServletResponse response, HttpServletRequest httpServletRequest) throws IOException {
        String tcKimlikNo = httpServletRequest.getParameter("tcKimlikNo");
        Integer bransId = Integer.valueOf(httpServletRequest.getParameter("bransId"));
        Integer polikinlikId = Integer.valueOf(httpServletRequest.getParameter("polikinlikId"));
        Integer personelId = Integer.valueOf(httpServletRequest.getParameter("personelId"));
        String randevulu = httpServletRequest.getParameter("randevulu");
        String randevuId = httpServletRequest.getParameter("randevuId");

        Date hastaVizitGiris = utils.parseDate(httpServletRequest.getParameter("girisTarihi"));
        Date hastaVizitCikis = utils.parseDate(httpServletRequest.getParameter("cikisTarihi"));

        JSONObject jsonObject = vizitService.addVizit(tcKimlikNo, bransId, polikinlikId, personelId,randevulu,randevuId, hastaVizitGiris, hastaVizitCikis);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonObject != null) {
            response.getWriter().write(jsonObject.toString());
        }
    }

    @RequestMapping("/getVizitlerByParams")
    public void getVizitlerByParams(HttpServletResponse response, HttpServletRequest httpServletRequest) throws IOException {
        Integer bransId=null;
        Integer poliklinikId=null;
        Integer personelId=null;
        Integer hastaId=null;

        String hastaVizitGiris = (httpServletRequest.getParameter("girisTarihi"));
        String hastaVizitCikis = (httpServletRequest.getParameter("cikisTarihi"));
        String tempbransId = (httpServletRequest.getParameter("bransId"));
        String bransKod = httpServletRequest.getParameter("bransKod");
        String temppersonelId = (httpServletRequest.getParameter("personelId"));
        String temppoliklinikId = (httpServletRequest.getParameter("polikinlikId"));
        String temphastaId = (httpServletRequest.getParameter("hastaId"));
        String tempgiris = (httpServletRequest.getParameter("girisTarihi"));
        String tempcikis = (httpServletRequest.getParameter("cikisTarihi"));
        if (tempbransId != null && tempbransId != "") {
            bransId=Integer.parseInt(tempbransId);
        }
        if(temppersonelId != null && temppersonelId != ""){
             personelId = Integer.parseInt(temppersonelId);
        }
        if(temppoliklinikId != null && temppoliklinikId != ""){
            poliklinikId = Integer.parseInt(temppoliklinikId);
        }
        if(temphastaId != null && temphastaId != ""){
             hastaId = Integer.parseInt(temphastaId);
        }


        JSONArray respArray = vizitService.getVizitByParam(bransId, bransKod, personelId, poliklinikId, hastaId, hastaVizitGiris, hastaVizitCikis);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (respArray != null && respArray.size() > 0) {
            response.getWriter().write(respArray.toString());

        } else {
            response.getWriter().write("no record found");
        }
    }


}
