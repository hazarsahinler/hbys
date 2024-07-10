package com.example.demo.bus.hastaHizmetBus;

import com.example.demo.dao.HastaVizitDAO;
import com.example.demo.dao.HizmetDAO;
import com.example.demo.dao.PersonelDAO;
import com.example.demo.entity.HastaHizmetler;
import com.example.demo.entity.HastaVizit;
import com.example.demo.entity.Hizmet;
import com.example.demo.entity.Personel;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HastaHizmetlerServiceImpl implements HastaHizmetlerService {
    private final HizmetDAO hizmetDAO;
    private final HastaVizitDAO hastaVizitDAO;
    private final PersonelDAO personelDAO;

    public HastaHizmetlerServiceImpl(HizmetDAO hizmetDAO, HastaVizitDAO hastaVizitDAO, PersonelDAO personelDAO) {
        this.hizmetDAO = hizmetDAO;
        this.hastaVizitDAO = hastaVizitDAO;
        this.personelDAO = personelDAO;
    }


    /**
     * Gerekli parametreler alinir, hastaya sadece doktoru işlem yapabilir.
     *
     * @param hastaVizitId
     * @param hizmetId
     * @param personelId
     * @param hizmetTarihi
     * @return
     */
    @Override
    @Transactional
    public JSONObject addHizmet(Integer hastaVizitId, Integer hizmetId, Integer personelId, Date hizmetTarihi) {
        JSONObject responseObj = new JSONObject();
        HastaVizit hastaVizit = hastaVizitDAO.getObjectById(HastaVizit.class, hastaVizitId);
        Hizmet hizmet = hizmetDAO.getObjectById(Hizmet.class, hizmetId);
        Personel personel = personelDAO.getObjectById(Personel.class, personelId);

        if (hastaVizit != null && hizmet != null && personel != null) {
            if (hastaVizit.getPersonel() != personel) {
                responseObj.put("success", false);
                responseObj.put("message", "Bu hastanın doktoru değilsiniz!!");
                return responseObj;
            }
            HastaHizmetler hastaHizmetler = new HastaHizmetler();
            hastaHizmetler.setHastaVizit(hastaVizit);
            hastaHizmetler.setHizmet(hizmet);
            hastaHizmetler.setPersonel(personel);
            hastaHizmetler.setHizmetTarihi(hizmetTarihi);
            hastaHizmetler.setAktifPasif(true);
            hizmetDAO.saveOrUpdate(hastaHizmetler);

            responseObj.put("success", true);
            responseObj.put("message", " hizmet basarili bir sekilde hastaya atandi.");
            return responseObj;
        }

        responseObj.put("success", false);
        responseObj.put("message", "Vizit veya hizmet bulunamadi.");
        return responseObj;


    }

    /**
     * tablodan seçilen hizmeti silen method.
     *
     * @param hastaHizmetId
     * @return
     */

    @Override
    @Transactional
    public JSONObject deleteHizmet(Integer hastaHizmetId) {
        JSONObject responseObj = new JSONObject();

        HastaHizmetler hastaHizmetler = hizmetDAO.getObjectById(HastaHizmetler.class, hastaHizmetId);

        if (hastaHizmetler != null) {
            hastaHizmetler.setAktifPasif(false);
            responseObj.put("success", true);
            responseObj.put("message", "Hizmet basariyla silindi.");
            return responseObj;
        } else {
            responseObj.put("success", false);
            responseObj.put("message", "Hizmet Bulunamadi.");
            return responseObj;
        }
    }

    @Override
    public JSONArray getHizmetlerByVizitId(Integer hastaVizitId) {
        JSONArray jsonArray = hizmetDAO.getHizmetList(hastaVizitId);
        return jsonArray;
    }

}

