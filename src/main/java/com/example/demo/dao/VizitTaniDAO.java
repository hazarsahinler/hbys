package com.example.demo.dao;

import com.example.demo.entity.HastaVizit;
import com.example.demo.entity.VizitTanilar;
import io.swagger.v3.core.util.Json;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class VizitTaniDAO extends BaseDAO {
    private final SessionFactory sessionFactory;

    public VizitTaniDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public void save(VizitTanilar vizitTanilar) {
        sessionFactory.getCurrentSession().save(vizitTanilar);
    }

    public JSONArray getTanilar(Integer hastaVizitId) {
        StringBuilder hql2 = new StringBuilder();
        hql2.append("Select new map(");
        hql2.append(" t.tani.taniKod as taniKodu,");
        hql2.append(" t.tani.taniAd as tani ) ");
        hql2.append(" from VizitTanilar t ");
        hql2.append(" Where (1=1)");
        if (hastaVizitId != null) {
            hql2.append("and t.hastaVizit.hastaVizitId=:hastaVizitId");
        }
        Query query1 = sessionFactory.getCurrentSession().createQuery(hql2.toString());
        if (hastaVizitId != null) {
            query1.setParameter("hastaVizitId", hastaVizitId);
        }
        JSONArray jsonArray1 = JSONArray.fromObject(query1.list());
        return jsonArray1;

    }

    public JSONArray getVizitTanilar(Integer hastaVizitId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" Select new map(");
        hql.append(" a.hastaVizit.hastaVizitId as hastaVizitId,");
        hql.append(" a.tani.taniKod as taniKodu,");
        hql.append(" a.tani.taniAd as tani ) ");
        hql.append(" from VizitTanilar  a ");
        hql.append("where (1=1)");
        if (hastaVizitId != null) {
            hql.append(" and a.hastaVizit.hastaVizitId=:hastaVizitId");
        }
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if (hastaVizitId != null) {
            query.setParameter("hastaVizitId", hastaVizitId);
        }
        JSONArray jsonArray = JSONArray.fromObject(query.list());
        return jsonArray;

    }

    public boolean getCount(Integer hastaVizitId, Integer taniId, Date taniTarihi) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strGirisTarihi = sdf.format(taniTarihi);

        StringBuilder hql = new StringBuilder();
        hql.append("select count(a) ");
        hql.append("from VizitTanilar a ");
        hql.append("where a.hastaVizit.hastaVizitId = :hastaVizitId ");
        hql.append("and a.tani.tani_id = :tani_id ");
        hql.append("and to_char(a.taniTarihi, 'dd/mm/yyyy') = :taniTarihi ");

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("hastaVizitId", hastaVizitId);
        query.setParameter("tani_id", taniId);
        query.setParameter("taniTarihi", strGirisTarihi);


        Long count = (Long) query.uniqueResult();

        return count > 0;
    }

}
