package com.example.demo.bus.personelBus;

import com.example.demo.Enum.Uzmanlik;
import com.example.demo.dao.BransDAO;
import com.example.demo.dao.PersonelDAO;
import com.example.demo.dao.PolikinlikDAO;
import com.example.demo.entity.Brans;
import com.example.demo.entity.Personel;
import com.example.demo.entity.Polikinlik;
import com.sun.jdi.IntegerValue;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersonelServiceImpl implements PersonelService {
    private final PersonelDAO personelDao;
    private final PersonelDAO personelDAO;
    private final BransDAO bransDAO;
    private final PolikinlikDAO polikinlikDAO;


    public PersonelServiceImpl(PersonelDAO personelDao, PersonelDAO personelDAO, BransDAO bransDAO, PolikinlikDAO polikinlikDAO) {
        this.personelDao = personelDao;
        this.personelDAO = personelDAO;
        this.bransDAO = bransDAO;
        this.polikinlikDAO = polikinlikDAO;
    }


    @Override
    public JSONArray getAllPersonel() {
        return personelDao.getPersonel();
    }

    @Override
    public JSONArray getByParam(String personelIsim, String personelSoyisim, String brans, String uzmanlik, String polikinlik) {
        return personelDao.getByParam(personelIsim, personelSoyisim, brans, uzmanlik, polikinlik);
    }

    @Override
    @Transactional
    public JSONObject addPersonel(String personelIsim, String personelSoyisim, String tcKimlikNo, Date iseBaslamaTarih, Date dogumTarihi, String bransId, String uzmanlik, String polikinlikId) {
        Personel personel = new Personel();
        JSONObject respObj = new JSONObject();
        List<Personel> tempPersonel = personelDAO.getObjectsByParam(Personel.class,"tcKimlikNo",tcKimlikNo);
        if (tcKimlikNo != null && !tcKimlikNo.isEmpty()) {

            if(tempPersonel.size()>0){
                respObj.put("message","Bu doktor sisteme kayitli.Mevcut doktor guncellenecek.");
                personel = tempPersonel.get(0);
            }
            personel.setTcKimlikNo(tcKimlikNo);
        } else {
            respObj.put("message", "Lütfen TC kimlik numarasi giriniz.");
            return respObj;
        }

        if (personelIsim != null && !personelIsim.isEmpty()) {
            personel.setPersonelIsim(personelIsim);
        } else {
            respObj.put("message", "Lütfen isminizi giriniz.");
            return respObj;
        }

        if (personelSoyisim != null && !personelSoyisim.isEmpty()) {
            personel.setPersonelSoyisim(personelSoyisim);
        } else {
            respObj.put("message", "Lütfen soyisminizi giriniz.");
            return respObj;
        }

        if (iseBaslamaTarih != null) {
            personel.setIseBaslamaTarih(iseBaslamaTarih);
        } else {
            respObj.put("message", "Lütfen ise baslama tarihininizi giriniz.");
            return respObj;
        }

        if (dogumTarihi != null) {
            personel.setDogumTarihi(dogumTarihi);
        } else {
            respObj.put("message", "Lütfen dogum tarihinizi giriniz.");
            return respObj;
        }

        if (bransId != null && !bransId.isEmpty()) {

            Brans brans = bransDAO.getObjectById(Brans.class, Integer.parseInt(bransId));
            personel.setBrans(brans);
        } else {
            respObj.put("message", "Lütfen bransinizi giriniz.");
            return respObj;
        }

        if (uzmanlik != null && !uzmanlik.isEmpty()) {
            personel.setUzmanlik(Uzmanlik.valueOf(uzmanlik));
        } else {
            respObj.put("message", "Lütfen uzmanliginizi giriniz.");
            return respObj;
        }

        if (polikinlikId != null && !polikinlikId.isEmpty()) {
            Polikinlik polikinlik = polikinlikDAO.getObjectById(Polikinlik.class, Integer.parseInt(polikinlikId));
            personel.setPolikinlik(polikinlik);
        } else {
            respObj.put("message", "Lütfen polikinliginizi giriniz.");
            return respObj;
        }

        if(tempPersonel.size()>0){
            personelDAO.saveOrUpdate(personel);
            respObj.put("succsess", true);
            respObj.put("message","Bu doktor sisteme kayitli.Mevcut doktor guncellenecek.");
            return respObj;
        }

        personelDAO.saveOrUpdate(personel);
        respObj.put("succsess", true);
        respObj.put("message", "Kayit basarili");
        return respObj;
    }


}
