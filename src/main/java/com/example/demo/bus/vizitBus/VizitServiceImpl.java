package com.example.demo.bus.vizitBus;

import com.example.demo.Enum.RandevuDurum;
import com.example.demo.Util.Utility;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VizitServiceImpl implements VizitService {
    private final HastaVizitDAO hastaVizitDao;
    private final HttpServletResponse httpServletResponse;
    private final HastaDAO hastaDAO;
    private final BransDAO bransDAO;
    private final PolikinlikDAO polikinlikDAO;
    private final PersonelDAO personelDAO;
    private final VizitTaniDAO vizitTaniDAO;
    private final HastaVizitDAO hastaVizitDAO;
    private final Utility utility;
    private final RandevuAjandaDAO randevuAjandaDAO;

    public VizitServiceImpl(HastaVizitDAO hastaVizitDao, HttpServletResponse httpServletResponse, HastaDAO hastaDAO, BransDAO bransDAO, PolikinlikDAO polikinlikDAO, PersonelDAO personelDAO, VizitTaniDAO vizitTaniDAO, HastaVizitDAO hastaVizitDAO, Utility utility, RandevuAjandaDAO randevuAjandaDAO) {
        this.hastaVizitDao = hastaVizitDao;
        this.httpServletResponse = httpServletResponse;
        this.hastaDAO = hastaDAO;
        this.bransDAO = bransDAO;
        this.polikinlikDAO = polikinlikDAO;
        this.personelDAO = personelDAO;
        this.vizitTaniDAO = vizitTaniDAO;
        this.hastaVizitDAO = hastaVizitDAO;
        this.utility = utility;
        this.randevuAjandaDAO = randevuAjandaDAO;
    }

    /**
     * Gerekli parametreler alınır.TC NO ile hastanın varlığının kontrolü yapılır.
     * Eğer hasta mevcut ise vizitler kontrolü yapılır.
     * Eğer vizitleri varsa vizitlerinin branş kontrolü yapılır.
     * Kayıt yapamaması için,Hem branş aynı olmalı hem de aktiflik durumu true olmalı.
     * Eğer vizit var ama farklı branştan ise ya da aktif değilse vizit kaydı gerçekleşir.
     *
     * @param tcKimlikNo
     * @param bransId
     * @param polikinlikId
     * @param personelId
     * @param hastaVizitGiris
     * @param hastaVizitCikis
     * @param aktif
     * @return
     */
    @Override
    @Transactional
    public JSONObject addVizit(String tcKimlikNo, Integer bransId, Integer polikinlikId, Integer personelId, String randevulu, String randevuId, Date hastaVizitGiris, Date hastaVizitCikis) {
        HastaVizit hastaVizit = new HastaVizit();
        List<Hasta> hastaList = hastaDAO.getObjectsByParam(Hasta.class, "tcKimlikNo", tcKimlikNo);
        Brans brans = bransDAO.getObjectById(Brans.class, bransId);
        Polikinlik polikinlik = polikinlikDAO.getObjectById(Polikinlik.class, polikinlikId);
        Personel personel = personelDAO.getObjectById(Personel.class, personelId);
        JSONObject jsonObject = new JSONObject();
        Hasta hasta = null;


        if (hastaList == null || hastaList.size() == 0) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", tcKimlikNo + "nolu kimlik numarasi bulunamadi, kayit yaptiktan sonra tekrar deneyiniz.");
            return jsonObject;
        } else {
            hasta = hastaList.get(0);

        }

        Boolean bool = hastaVizitDao.getCount(bransId, hasta.getHastaId(), hastaVizitGiris, true);
        if (bool == true) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "Aynı branstan vizitiniz devam etmekte.Vizit kaydı basarisiz");
            return jsonObject;
        }

        hastaVizit.setHasta(hasta);
        hastaVizit.setBrans(brans);
        hastaVizit.setPolikinlik(polikinlik);
        hastaVizit.setPersonel(personel);
        hastaVizit.setHastaVizitGiris(hastaVizitGiris);
        hastaVizit.setHastaVizitCikis(hastaVizitCikis);
        hastaVizit.setAktif(true);
        hastaVizit.setRandevulu(false);
        hastaVizitDao.saveOrUpdate(hastaVizit);
        int id = hastaVizit.getHastaVizitId();

        List<RandevuAjanda> randevuAjanda = randevuAjandaDAO.getRandevuByHastaAndBrans(hasta.getHastaId(), bransId);
        if (!utility.convertBoolean(randevulu, false)) {
            if (randevuAjanda.size() > 0) {
                RandevuAjanda randevu = randevuAjanda.get(0);
                randevu.setHastaVizit(hastaVizit);
                hastaVizit.setRandevulu(true);
                randevuAjandaDAO.saveOrUpdate(randevu);
            }
        } else {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "RANDEVU BULUNAMADİ");
            return jsonObject;
        }

        jsonObject.put("succsess", true);
        jsonObject.put("message", id + " idsi ile Vizit OLuşturuldu");
        return jsonObject;


    }

    /**
     * VizitId parametresi ile öncelikle hastaVizitin doğruluğu tespit edilir.
     * Sonrasında, bu vizite ait tanilar vizitTaniDao altinda bulunan getTanilar() fonk.dan çekilir.
     * Eğer tanilar boş ise hastanin hastaneden taburcu olup olmadığı kontrolü saglanir.
     * Kosullari saglayan vizit iptali gerçekleştirilir.(aktif column=false)
     *
     * @param hastaVizitId
     * @return
     */
    @Transactional
    @Override
    public JSONObject deleteVizit(Integer hastaVizitId) {
        HastaVizit hastaVizit = hastaVizitDao.getObjectById(HastaVizit.class, hastaVizitId);
        JSONObject jsonObject = new JSONObject();
        List<VizitTanilar> vizitTanilarList = vizitTaniDAO.getObjectsByParam(VizitTanilar.class, "hastaVizit", hastaVizit);

        if (hastaVizit == null) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "Hasta Vizit ID bulunamadi.");
            return jsonObject;
        }

        JSONArray jsonArray = vizitTaniDAO.getTanilar(hastaVizitId);
        if (jsonArray != null && jsonArray.size() > 0) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "Hastaya kayitli tanilar bulunmakta, kayit silinemez.");
            return jsonObject;
        }

        if (hastaVizit.getAktif() == false) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "Bu vizit tekrar silinemez.");
            return jsonObject;
        }

        if (hastaVizit.getHastaVizitCikis() != null) {
            jsonObject.put("succsess", false);
            jsonObject.put("message", "Hasta taburcu olduktan sonra vizit silinemez!!");
            return jsonObject;
        }


        hastaVizit.setAktif(Boolean.FALSE);
        hastaVizitDao.saveOrUpdate(hastaVizit);
        jsonObject.put("succsess", true);
        jsonObject.put("message", "Hasta Vizit Başarıyla Silindi.");
        return jsonObject;

    }

    @Override
    public JSONArray getVizitByParam(Integer bransId, String bransKodu, Integer personelId,
                                     Integer poliklinikId, Integer hastaId,
                                     String HastaVizitGiris, String HastaVizitCikis) {

        JSONArray respObj = hastaVizitDAO.getVizitByParam(bransId, bransKodu, personelId, poliklinikId, hastaId, HastaVizitGiris, HastaVizitCikis);
        return respObj;
    }


}
