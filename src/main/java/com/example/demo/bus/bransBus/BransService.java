package com.example.demo.bus.bransBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface BransService {
    JSONArray getAllBrans();
    JSONArray getByParam(Integer bransId,String bransIsmi,String bransKodu);
    JSONObject deleteByParam(Integer bransId, String bransIsmi, String bransKodu);
    JSONObject insertBrans(Integer bransId, String bransIsmi, String bransKodu) throws Exception;
}
