package com.example.demo.bus.vizitBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;


public interface VizitService {

    JSONObject addVizit(String tcKimlikNo, Integer bransId, Integer polikinlikId, Integer personelId,String randevulu ,String randevuId,Date hastaVizitGiris, Date hastaVizitCikis);

    JSONObject deleteVizit(Integer hastaVizitId);

    JSONArray getVizitByParam(Integer bransId, String bransKodu, Integer personelId, Integer poliklinikId, Integer hastaId,String hastaVizitGiris, String hastaVizitCikis);
}
