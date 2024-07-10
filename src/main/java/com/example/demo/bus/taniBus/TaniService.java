package com.example.demo.bus.taniBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface TaniService {
    JSONObject deleteByParam(String taniKod,String taniAd);
    JSONObject insertByParam(Integer tani_id,String taniKod,String taniAd);
    JSONArray getTaniByParam(String taniKod,String taniAd);
}
