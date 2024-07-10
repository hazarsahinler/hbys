package com.example.demo.bus.hastaBus;

import com.example.demo.Enum.SigortaTipi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;

public interface HastaService {

    JSONArray findByParam(String tcKimlikNo, String hastaIsim, String hastaSoyisim, String sigortaTipi, String telNo, String mail);

    JSONArray getAllHastalar();

    JSONObject addHasta(String tcKimlikNo, String hastaIsim, String hastaSoyisim, Integer firmaId, String sigortaTipi, String telNo, String mail, Date dogumTarihi);

}
