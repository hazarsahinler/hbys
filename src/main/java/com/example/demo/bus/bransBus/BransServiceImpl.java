package com.example.demo.bus.bransBus;

import com.example.demo.dao.BransDAO;
import com.example.demo.entity.Brans;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BransServiceImpl implements BransService {
    private final BransDAO bransDao;
    public BransServiceImpl(BransDAO bransDao) {
        this.bransDao = bransDao;
    }
    /**
     * Tum brans bilgilerini geri döndürür.
     *
     * @return
     */
    @Override
    public JSONArray getAllBrans() {
        return bransDao.getBranslar();
    }
    /**
     * Alınan parametrelere göre branş bilgisi geri döndürür.
     *
     * @param bransId
     * @param bransIsmi
     * @param bransKodu
     * @return
     */
    @Override
    public JSONArray getByParam(Integer bransId, String bransIsmi, String bransKodu) {
        return bransDao.getBranslarByParam(bransId, bransIsmi, bransKodu);
    }
    /**
     * Girilen parametreler ile getBranslarByParam fonksiyonu çağırılıyor.
     * Eğer liste boş dönmezse silme işlemi tamamlanıyor.
     * @param bransId
     * @param bransIsmi
     * @param bransKodu
     * @return
     */
    @Override
    @Transactional
    public JSONObject deleteByParam(Integer bransId, String bransIsmi, String bransKodu) {
        JSONObject result = new JSONObject();
        List<Brans> bransList = bransDao.getObjectsByParam(Brans.class, "bransKodu", bransKodu);
        List<Brans> bransList2 = bransDao.getObjectsByParam(Brans.class,
                new String[]{"bransKodu", "bransIsmi"},
                new Object[]{bransKodu, "kardiyoloji"});

        for (Brans brans : bransList) {
            bransDao.deleteObject(brans);
        }

        result.put("success", true);
        result.put("message", bransList.size() + " Kayıt için silme işlemi başarıyla gerçekleşti");
        return result;
    }

    @Override
    @Transactional
    public JSONObject insertBrans(Integer bransId, String bransIsmi, String bransKodu) throws Exception {
        JSONObject result = new JSONObject();
        List<Brans> bransList = bransDao.getObjectsByParam(Brans.class, "bransKodu", bransKodu);
        Brans brans = new Brans();
        if(bransId != null){
            brans=bransDao.getObjectById(Brans.class, bransId);
            if(brans!=null){
                brans.setBransKodu(bransKodu);
                brans.setBransIsmi(bransIsmi);
                bransDao.saveOrUpdate(brans);
                result.put("success", true);
                result.put("message",bransKodu +"kodlu brans güncellendi");
                return result;
            }else{
                result.put("success", false);
                result.put("message",bransId +" Id bulunamadı");
                return result;
            }

        }
        if (bransList != null && bransList.size() > 0) {
            result.put("success", false);
            result.put("message", bransKodu + " kodlu branş zaten kayıtlı.");
            return result;
        }


        brans.setBransIsmi(bransIsmi);
        brans.setBransKodu(bransKodu);
        bransDao.saveOrUpdate(brans);

        result.put("success", true);
        result.put("message", "Kayıt işlemi başarıyla gerçekleşti");
        return result;
    }
}
