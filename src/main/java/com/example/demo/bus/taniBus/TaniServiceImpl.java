package com.example.demo.bus.taniBus;

import com.example.demo.dao.TaniDAO;
import com.example.demo.entity.Tani;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaniServiceImpl implements TaniService {
    private final TaniDAO taniDAO;

    public TaniServiceImpl(TaniDAO taniDAO) {
        this.taniDAO = taniDAO;
    }

    /**
     * Tani kodundan tablodaki veriyi bulup silme methodu.
     * @param taniKod
     * @param taniAd
     * @return
     */

    @Override
    @Transactional
    public JSONObject deleteByParam(String taniKod, String taniAd) {
        List<Tani> taniList = taniDAO.getObjectsByParam(Tani.class, "taniKod", taniKod);
        for(Tani tani : taniList) {
            taniDAO.deleteObject(tani);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "success");
        jsonObject.put("message",taniList.size()+" kayit silindi.");
        return jsonObject;
    }

    /**
     * Eğer tani id null gelirse, tani kodu checkleyecek.tani kodu tabloda varsa false message verecek.
     * Tani kodu tabloda yoksa yeni bir tani eklenecek.
     * Tani id null gelmezse tabloda veriyi bulacak, update edecek.
     * @param tani_id
     * @param taniKod
     * @param taniAd
     * @return
     */
    @Override
    @Transactional
    public JSONObject insertByParam(Integer tani_id,String taniKod, String taniAd) {
        JSONObject jsonObject = new JSONObject();
        List<Tani> taniList = taniDAO.getObjectsByParam(Tani.class, "taniKod", taniKod);
        Tani tani = new Tani();
        if (tani_id != null) {
            tani = taniDAO.getObjectById(Tani.class, tani_id);
            if (tani != null) {
                tani.setTaniKod(taniKod);
                tani.setTaniAd(taniAd);
                taniDAO.saveOrUpdate(tani);
                jsonObject.put("status", "success");
                jsonObject.put("message", taniKod + " kodlu tani güncellendi.");
                return jsonObject;
            }
        }
        if( !taniList.isEmpty()) {
            jsonObject.put("status", "false");
            jsonObject.put("massage",taniKod+" kodlu tani tabloya kayıtlı.");
            return jsonObject;
        }
        tani.setTaniKod(taniKod);
        tani.setTaniAd(taniAd);

        taniDAO.saveOrUpdate(tani);
        jsonObject.put("status", "success");
        jsonObject.put("message",taniKod+ " kodu eklendi.");
        return jsonObject;
    }

    @Override
    public JSONArray getTaniByParam(String taniKod, String taniAd) {
        return taniDAO.getTaniByParam(taniKod,taniAd);
    }

}
