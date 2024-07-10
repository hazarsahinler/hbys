package com.example.demo.controller;

import com.example.demo.bus.randevuAjandaBus.RandevuAjandaService;
import com.example.demo.dao.RandevuAjandaDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RandevuAjandaController {
    private final RandevuAjandaService randevuAjandaService;
    private final RandevuAjandaDAO randevuAjandaDAO;

    public RandevuAjandaController(RandevuAjandaService randevuAjandaService, RandevuAjandaDAO randevuAjandaDAO) {
        this.randevuAjandaService = randevuAjandaService;
        this.randevuAjandaDAO = randevuAjandaDAO;
    }

    @RequestMapping("/getRandevuSlot")
    public void getRandevuSlot(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String personelId = request.getParameter("personel_id");
        String bransId = request.getParameter("brans_id");
        String polikinlikId = request.getParameter("polikinlik_id");
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        JSONArray respList = randevuAjandaService.getBosRandevuSlotlar(personelId, bransId, polikinlikId, startDate, endDate);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (respList != null && respList.size() > 0) {
            response.getWriter().write(respList.toString());
        }

    }


    @RequestMapping("/createTakvim")
    public void createTakvim(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        JSONObject respObj = randevuAjandaService.createTakvim(startDate, endDate);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }


    }

    @RequestMapping("/createAjanda")
    public void createAjanda(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baslangicTarihi = request.getParameter("baslangic_tarihi");
        String bitisTarihi = request.getParameter("bitis_tarihi");
        String baslangicSaati = request.getParameter("baslangic_saati");
        String bitisSaati = request.getParameter("bitis_saati");
        String gunler = request.getParameter("gunler");
        String muayeneSure = request.getParameter("muayene_sure");
        String bransId = request.getParameter("brans_id");
        String polikinlikId = request.getParameter("polikinlik_id");
        String personelId = request.getParameter("personel_id");
        String kisitBasla = request.getParameter("kısıtBasla");
        String kisitBitis = request.getParameter("kısıtBitis");
        String resmiTatil = request.getParameter("resmiTatil");
        String kayitGuncelle = request.getParameter("kayitGüncelle");
        JSONObject respObj = randevuAjandaService.createAjanda(
                baslangicTarihi, bitisTarihi, baslangicSaati,
                bitisSaati, gunler, muayeneSure,
                bransId, polikinlikId, personelId,
                kisitBasla, kisitBitis, resmiTatil, kayitGuncelle);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }

    }
    @RequestMapping("/createRandevu")
    public void createRandevu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hastaId = request.getParameter("hasta_id");
        String randevuId = request.getParameter("randevu_id");
        String hastaNotu=request.getParameter("hasta_notu");
        JSONObject respObj = randevuAjandaService.createRandevu(hastaId,randevuId,hastaNotu);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }

    }

    @RequestMapping("/deleteRandevuById")
    public void deleteRandevuById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String randevuId = request.getParameter("randevuId");
        JSONObject respObj =randevuAjandaService.deleteRandevuById(randevuId);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }
    }

    @RequestMapping("/deleteRandevuByTarih")
    public void deleteRandevuByTarih(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baslangicTarihi = request.getParameter("baslangic_tarihi");
        String bitisTarihi = request.getParameter("bitis_tarihi");
        String personelId = request.getParameter("personelId");
        JSONObject respObj =randevuAjandaService.deleteRandevuByTarih(baslangicTarihi,bitisTarihi,personelId);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }

    }
    @RequestMapping("/randevuIptal")
    public void randevuIptal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String randevuId = request.getParameter("randevu_id");
        JSONObject respObj= randevuAjandaService.randevuIptal(randevuId);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        }

    }
    @RequestMapping("/getAktifRandevular")
    public void getAktifRandevularByHasta(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hastaId = request.getParameter("hasta_id");
        String baslangic = request.getParameter("baslangic_tarihi");
        String bitis = request.getParameter("bitis_tarihi");
        JSONArray jsonArray = randevuAjandaService.getAktifRandevularByHasta(hastaId,baslangic,bitis);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (jsonArray != null && jsonArray.size() > 0) {
            response.getWriter().write(jsonArray.toString());
        }else{
            response.getWriter().write("AKTİF RANDEVU BULUNAMADİ");
        }

    }
    @RequestMapping("/getDoktorRandevular")
    public void getAktifRandevularDoktor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String personelId = request.getParameter("personel_id");
        JSONArray resp= randevuAjandaService.getDoluRandevularByDoktorId(personelId);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (resp != null && resp.size() > 0) {
            response.getWriter().write(resp.toString());
        }else {
            response.getWriter().write("AKTİF RANDEVU BULUNAMADİ");
        }


    }
    @RequestMapping("/addRezervToRandevu")
    public void addRezervToRandevu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String slotId = request.getParameter("slot_id");
        String rezervNotu = request.getParameter("rezerv_notu");
        JSONObject resp = randevuAjandaService.addRezervToRandevu(slotId,rezervNotu);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (resp != null && resp.size() > 0) {
            response.getWriter().write(resp.toString());
        }
    }
    @RequestMapping("/addHastaToRezerv")
    public void addHastaToRezerv(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String hastaId = request.getParameter("hasta_id");
        String slotId = request.getParameter("slot_id");
        JSONObject resp = randevuAjandaService.addHastaToRezervSlot(hastaId,slotId);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (resp != null && resp.size() > 0) {
            response.getWriter().write(resp.toString());
        }
    }


}
