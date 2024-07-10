package com.example.demo.bus.polikinlikBus;

import com.example.demo.dao.BransDAO;
import com.example.demo.dao.PolikinlikDAO;
import com.example.demo.entity.Brans;
import com.example.demo.entity.Polikinlik;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolikinlikServiceImpl implements PolikinlikService {
    private final PolikinlikDAO polikinlikDao;
    private final BransDAO bransDAO;
    private final PolikinlikDAO polikinlikDAO;

    public PolikinlikServiceImpl(PolikinlikDAO polikinlikDao, BransDAO bransDAO, PolikinlikDAO polikinlikDAO) {
        this.polikinlikDao = polikinlikDao;
        this.bransDAO = bransDAO;
        this.polikinlikDAO = polikinlikDAO;
    }

    @Override
    public JSONArray getPolikinlikByBrans(String bransIsmi, String bransKodu, String bransId) {
        return polikinlikDao.getPolikinlikByBrans(bransIsmi, bransKodu, bransId);
    }

    @Override
    public JSONObject addPolikinlik(String polikinlikIsim, String bransId) {
        JSONObject respObj = new JSONObject();
        if (polikinlikIsim != null && !polikinlikIsim.isEmpty()) {
            Polikinlik polikinlik = new Polikinlik();
            List<Polikinlik> polikinlikList = polikinlikDAO.getObjectsByParam(Polikinlik.class, "polikinlikIsim", polikinlikIsim);
            if(polikinlikList.size()>0){
                respObj.put("status", "error");
                respObj.put("message","Bu polikinlik mevcut.");
                return respObj;
            }
            polikinlik.setPolikinlikIsim(polikinlikIsim);
            Brans brans = bransDAO.getObjectById(Brans.class, Integer.parseInt(bransId));
            polikinlik.setBrans(brans);
            polikinlikDao.saveOrUpdate(polikinlik);
            respObj.put("success", true);
            respObj.put("message", brans.getBransIsmi() + " isimli bransa polikinlik eklendi.");
            return respObj;
        } else {
            respObj.put("success", false);
            respObj.put("message", "Polikinlik eklenemedi.");
            return respObj;
        }

    }
}
