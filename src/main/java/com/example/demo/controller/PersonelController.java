package com.example.demo.controller;

import com.example.demo.Util.Utility;
import com.example.demo.bus.personelBus.PersonelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/personel")
public class PersonelController {
    private final PersonelService personelService;
    private final Utility utility;

    public PersonelController(PersonelService personelService, Utility utility) {
        this.personelService = personelService;
        this.utility = utility;
    }

    @GetMapping
    public JSONArray getAllPersonel() {
        return JSONArray.fromObject(personelService.getAllPersonel());
    }

    @RequestMapping("/search")
    public void searchPersonel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String personelIsim = request.getParameter("personelIsim");
        String personelSoyisim = request.getParameter("personelSoyisim");
        String brans = request.getParameter("brans");
        String uzmanlik = request.getParameter("uzmanlik");
        String polikinlik = request.getParameter("polikinlik");

        JSONArray jsonArray = personelService.getByParam(personelIsim, personelSoyisim, brans, uzmanlik, polikinlik);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (jsonArray != null && jsonArray.size() > 0) {
            response.getWriter().write(jsonArray.toString());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
            response.getWriter().write(jsonObject.toString());
        }


    }

    @RequestMapping("/addPersonel")
    public void addPersonel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String personelIsim = request.getParameter("personelIsim");
        String personelSoyisim = request.getParameter("personelSoyisim");
        String tcKimlikNo = request.getParameter("tcKimlikNo");
        Date iseBaslamaTarih = utility.parseDate(request.getParameter("iseBaslamaTarih"));
        Date dogumTarihi = utility.parseDate(request.getParameter("dogumTarihi"));
        String bransId = request.getParameter("bransId");
        String uzmanlik = request.getParameter("uzmanlik");
        String polikinlikId = request.getParameter("polikinlikId");

        JSONObject jsonObject = personelService.addPersonel(personelIsim,personelSoyisim,tcKimlikNo,iseBaslamaTarih,dogumTarihi,bransId,uzmanlik,polikinlikId);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonObject != null) {
            response.getWriter().write(jsonObject.toString());
        }


    }
}
