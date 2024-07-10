package com.example.demo.bus.vizitTaniBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Date;

public interface VizitTaniService {
    JSONObject addTaniToVizit(Integer hastaVizitId, Integer taniId, Date taniTarihi);
    JSONArray getVizitTani(Integer hastaVizitId);

}
