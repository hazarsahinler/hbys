package com.example.demo.bus.hastaHizmetBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;

import java.util.Date;

public interface HastaHizmetlerService {
    JSONObject addHizmet(Integer hastaVizitId, Integer hizmetId, Integer personelId, Date hizmetTarihi);
    JSONObject deleteHizmet(Integer hastaHizmetId);
    JSONArray getHizmetlerByVizitId(Integer hastaVizitId);
}
