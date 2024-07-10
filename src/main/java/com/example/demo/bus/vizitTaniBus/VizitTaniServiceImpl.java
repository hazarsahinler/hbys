package com.example.demo.bus.vizitTaniBus;

import com.example.demo.dao.HastaVizitDAO;
import com.example.demo.dao.TaniDAO;
import com.example.demo.dao.VizitTaniDAO;
import com.example.demo.entity.HastaHizmetler;
import com.example.demo.entity.HastaVizit;
import com.example.demo.entity.Tani;
import com.example.demo.entity.VizitTanilar;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VizitTaniServiceImpl implements VizitTaniService {
    private final VizitTaniDAO vizitTaniDAO;
    private final HastaVizitDAO hastaVizitDAO;
    private final TaniDAO taniDAO;

    public VizitTaniServiceImpl(VizitTaniDAO vizitTaniDAO, HastaVizitDAO hastaVizitDAO, TaniDAO taniDAO) {

        this.vizitTaniDAO = vizitTaniDAO;
        this.hastaVizitDAO = hastaVizitDAO;
        this.taniDAO = taniDAO;
    }

    @Override
    @Transactional
    public JSONObject addTaniToVizit(Integer hastaVizitId, Integer taniId, Date taniTarihi) {
        VizitTanilar vizitTanilar = new VizitTanilar();
        JSONObject jsonObject = new JSONObject();
        HastaVizit hastaVizit = hastaVizitDAO.getObjectById(HastaVizit.class, hastaVizitId);
        Tani tani = taniDAO.getObjectById(Tani.class, taniId);
        List<VizitTanilar> vizitTanilarList = vizitTaniDAO.getObjectsByParam(VizitTanilar.class, "HastaVizit.hastaVizitId", hastaVizitId);
        boolean bool = vizitTaniDAO.getCount(hastaVizitId, taniId, taniTarihi);
        if (hastaVizitId == null || taniId == null) {
            jsonObject.put("status", "error");
            jsonObject.put("message", "Hasta veya tani hatali.");
            return jsonObject;
        }

        if (bool == false) {
            if (hastaVizit != null && tani != null && hastaVizit.getAktif() == true) {
                vizitTanilar.setHastaVizit(hastaVizit);
                vizitTanilar.setTani(tani);
                vizitTanilar.setTaniTarihi(taniTarihi);
                vizitTaniDAO.save(vizitTanilar);
                jsonObject.put("succsess", true);
                jsonObject.put("message", "Tani basariyla eklendi.");
                return jsonObject;

            }
        }


        jsonObject.put("succsess", false);
        jsonObject.put("message", "Ayni tani yalnizca bir defa eklenebilir..");
        return jsonObject;

    }

    @Override
    public JSONArray getVizitTani(Integer hastaVizitId) {
        JSONArray jsonArray = new JSONArray();
        JSONArray vizitBilgi = vizitTaniDAO.getVizitTanilar(hastaVizitId);


        return vizitBilgi;


    }
}
