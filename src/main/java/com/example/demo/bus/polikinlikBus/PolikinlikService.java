package com.example.demo.bus.polikinlikBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface PolikinlikService {
    JSONArray getPolikinlikByBrans(String bransIsmi,String bransKodu,String bransId);
    JSONObject addPolikinlik(String polikinlikIsim,String bransId);
}
