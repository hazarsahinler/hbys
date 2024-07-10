package com.example.demo.controller;

import com.example.demo.bus.polikinlikBus.PolikinlikService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/polikinlikler")
public class PolikinlikController {
    private final PolikinlikService polikinlikService;

    public PolikinlikController(PolikinlikService polikinlikService) {
        this.polikinlikService = polikinlikService;
    }

    @RequestMapping("/getPolikinliklerByParam")
    public void polikinlikler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String bransIsmi = httpServletRequest.getParameter("bransIsmi");
        String bransKodu = httpServletRequest.getParameter("bransKodu");
        String bransId = httpServletRequest.getParameter("bransId");

        JSONArray respObj = polikinlikService.getPolikinlikByBrans(bransIsmi, bransKodu, bransId);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        if (respObj != null && respObj.size() > 0) {
            httpServletResponse.getWriter().write(respObj.toString());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
            httpServletResponse.getWriter().write(jsonObject.toString());
        }

    }

    @RequestMapping("/addPolikinlik")
    public void addPolikinlik(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String polikinlikIsim=httpServletRequest.getParameter("polikinlikIsim");
        String bransId=httpServletRequest.getParameter("bransId");
        JSONObject respObj=polikinlikService.addPolikinlik(polikinlikIsim, bransId);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        if (respObj != null && respObj.size() > 0) {
            httpServletResponse.getWriter().write(respObj.toString());
        }
    }

}
