package com.example.demo.bus.personelBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;

public interface PersonelService {
    JSONArray getAllPersonel();
    JSONArray getByParam(String personelIsim, String personelSoyisim,String brans,String uzmanlik,String polikinlik);
    JSONObject addPersonel(String personelIsim,String personelSoyisim, String tcKimlikNo,  Date iseBaslamaTarih,Date dogumTarihi,
                           String bransId, String uzmanlik,String polikinlikId);
}
