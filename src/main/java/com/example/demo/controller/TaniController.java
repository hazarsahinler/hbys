package com.example.demo.controller;

import com.example.demo.bus.taniBus.TaniService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/tani")
public class TaniController {
    private final TaniService taniService;

    public TaniController(TaniService taniService) {
        this.taniService = taniService;
    }


    @RequestMapping("/deleteTani")
    public void deleteTani(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String taniKod = httpServletRequest.getParameter("taniKod");
        String taniAd = httpServletRequest.getParameter("taniAd");

        JSONObject jsonObject = taniService.deleteByParam(taniKod, taniAd);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }
    @RequestMapping("/addTani")
    public void addTani(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String taniKod = httpServletRequest.getParameter("taniKod");
        String taniAd = httpServletRequest.getParameter("taniAd");
        String taniIdParam = httpServletRequest.getParameter("tani_id");
        Integer tani_id = null;
        if (taniIdParam != null && !taniIdParam.isEmpty()) {
            try {
                tani_id = Integer.parseInt(taniIdParam);
            } catch (NumberFormatException e) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("UTF-8");
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Invalid bransId format");
                httpServletResponse.getWriter().write(errorResponse.toString());
                return;
            }
        }

        JSONObject jsonObject = taniService.insertByParam(tani_id,taniKod,taniAd);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }
    @RequestMapping("/getByParam")
    public void getByParam(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String taniKod = httpServletRequest.getParameter("taniKod");
        String taniAd = httpServletRequest.getParameter("taniAd");
        JSONArray jsonArray = taniService.getTaniByParam(taniKod,taniAd);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonArray.toString());
    }
}
