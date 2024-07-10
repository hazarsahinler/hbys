package com.example.demo.dao;

import com.example.demo.entity.HastaVizit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaniDAO extends BaseDAO {
    private final SessionFactory sessionFactory;
    private final HastaVizitDAO hastaVizitDAO;

    public TaniDAO(SessionFactory sessionFactory, HastaVizitDAO hastaVizitDAO) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
        this.hastaVizitDAO = hastaVizitDAO;
    }


    public JSONArray getTaniByParam(String taniKod, String taniAd) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select new map( ");
        hql.append(" t.taniKod as tanininKodu,");
        hql.append(" t.taniAd as tanininAdi ) ");
        hql.append(" From Tani t ");
        hql.append(" Where (1=1)");
        if (taniKod != null && taniKod != "") {
            hql.append(" and lower(t.taniKod) like :taniKod");
        }
        if (taniAd != null && taniAd != "") {
            hql.append(" and lower(t.taniAd) like :taniAd");
        }
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if (taniKod != null && taniKod != "") {
            query.setParameter("taniKod", "%" + taniKod.toLowerCase() + "%");
        }
        if (taniAd != null && taniAd != "") {
            query.setParameter("taniAd", "%" + taniAd.toLowerCase() + "%");
        }
        return JSONArray.fromObject(query.list());


    }
}
