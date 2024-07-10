package com.example.demo.bus.hastaBus;

import com.example.demo.Enum.SigortaTipi;
import com.example.demo.dao.HastaDAO;
import com.example.demo.dao.SigortaFirmaDAO;
import com.example.demo.entity.Hasta;
import com.example.demo.entity.SigortaFirmalar;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HastaServiceImpl implements HastaService {


    private final HastaDAO hastaDao;
    private final SigortaFirmaDAO sigortaFirmaDAO;

    public HastaServiceImpl(HastaDAO hastaDao, SigortaFirmaDAO sigortaFirmaDAO) {
        this.hastaDao = hastaDao;
        this.sigortaFirmaDAO = sigortaFirmaDAO;
    }

   /* public HastaServiceImpl(HastaDaoImpl hastaDao) {
        this.hastaDao = hastaDao;
    }
*/


    @Override
    public JSONArray findByParam(String tcKimlikNo, String hastaIsim, String hastaSoyisim, String sigortaTipi, String telNo, String mail) {
        return hastaDao.getByParam(tcKimlikNo, hastaIsim, hastaSoyisim, sigortaTipi, telNo, mail);
    }

    @Override
    public JSONArray getAllHastalar() {
        return hastaDao.getAllHastalar();
    }

    /**
     * hastanin dbde olmamasi durumunda hastaya vizit eklenebilir.
     *
     * @param tcKimlikNo
     * @param hastaIsim
     * @param hastaSoyisim
     * @param firmaId
     * @param sigortaTipi
     * @param telNo
     * @param mail
     * @param dogumTarihi
     * @return
     */
    @Override
    @Transactional
    public JSONObject addHasta(String tcKimlikNo, String hastaIsim, String hastaSoyisim, Integer firmaId, String sigortaTipi, String telNo, String mail, Date dogumTarihi) {
        Hasta hasta = new Hasta();
        SigortaFirmalar sigortaFirmalar = null;
        JSONObject jsonObject = new JSONObject();
        if (firmaId != null) {
            sigortaFirmalar = sigortaFirmaDAO.getObjectById(SigortaFirmalar.class, firmaId);
        }

        if (sigortaFirmalar == null && sigortaTipi != null) {

            if (tcKimlikNo != null && hastaIsim != null && hastaSoyisim != null && sigortaTipi != null && telNo != null && mail != null && dogumTarihi != null) {

                hasta.setTcKimlikNo(tcKimlikNo);
                hasta.setHastaIsim(hastaIsim);
                hasta.setHastaSoyisim(hastaSoyisim);
                try {
                    SigortaTipi sigortaTipiEnum = SigortaTipi.valueOf(sigortaTipi);
                    hasta.setSigortaTipi(sigortaTipiEnum);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Geçersiz sigortaTipi değeri: " + sigortaTipi);
                }
                hasta.setSigortaFirmalar(sigortaFirmalar);
                hasta.setTelNo(telNo);
                hasta.setMail(mail);
                hasta.setDogumTarihi(dogumTarihi);
                hastaDao.saveOrUpdate(hasta);
                jsonObject.put("accsess", true);
                jsonObject.put("message", "Kayit basarili, vizit kaydi yapabilirsiniz.");
                return jsonObject;
            } else {
                jsonObject.put("accsess", false);
                jsonObject.put("message", "girilen bilgiler eksiksiz olmalıdır.");
                return jsonObject;
            }


        }
        SigortaTipi sigortaTipiEnum;
        if (sigortaFirmalar != null && sigortaTipi != null) {
            if (tcKimlikNo != null && hastaIsim != null && hastaSoyisim != null && sigortaTipi != null && telNo != null && mail != null && dogumTarihi != null) {
                try {
                    sigortaTipiEnum = SigortaTipi.valueOf(sigortaTipi);
                    hasta.setSigortaTipi(sigortaTipiEnum);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Geçersiz sigortaTipi değeri: " + sigortaTipi);
                }
                if (sigortaTipiEnum != SigortaTipi.UCRETLI) {
                    jsonObject.put("accsess", false);
                    jsonObject.put("message", "UCRETLİ HASTAYI SEÇMENİZ GEREKLİ");
                    return jsonObject;
                }

                hasta.setTcKimlikNo(tcKimlikNo);
                hasta.setHastaIsim(hastaIsim);
                hasta.setHastaSoyisim(hastaSoyisim);
                hasta.setSigortaFirmalar(sigortaFirmalar);


            }
            hasta.setTelNo(telNo);
            hasta.setMail(mail);
            hasta.setDogumTarihi(dogumTarihi);
            hastaDao.saveOrUpdate(hasta);
            jsonObject.put("accsess", false);
            jsonObject.put("message", "Kayit basarili, vizit kaydi yapabilirsiniz.");
            return jsonObject;
        }

        jsonObject.put("accsess", false);
        jsonObject.put("message", "Sigorta firmasi seçin.");
        return jsonObject;


    }


}
