package com.example.demo.dao;

import io.swagger.v3.core.util.Json;
import net.sf.json.JSONArray;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PolikinlikDAO extends BaseDAO {
    private final SessionFactory sessionFactory;

    public PolikinlikDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;

    }



    public JSONArray getPolikinlikByBrans(String bransIsmi, String bransKodu,String bransId) {
        StringBuilder hql = new StringBuilder();

        hql.append(" Select new map( a.brans.bransIsmi as bransIsmi,");
        hql.append(" a.brans.bransKodu as bransKodu, ");
        hql.append(" a.polikinlikIsim as polikinlikIsim) ");
        hql.append(" from Polikinlik a ");
        hql.append(" where 1=1");

        if (bransIsmi != null && !bransIsmi.isEmpty()) {
            hql.append(" and lower(a.brans.bransIsmi) like :bransIsmi ");
        }
        if (bransKodu != null && !bransKodu.equals("")) {
            hql.append(" and lower(a.brans.bransKodu) like :bransKodu ");
        }
        if(bransId != null && !bransId.equals("")) {
            hql.append(" and a.brans.bransId = :bransId ");
        }
        hql.append(" ORDER BY a.polikinlikIsim ");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());

        if (bransIsmi != null && !bransIsmi.isEmpty()) {
            query.setParameter("bransIsmi","%" + bransIsmi.toLowerCase() + "%");
        }
        if (bransKodu != null && !bransKodu.equals("")) {
            query.setParameter("bransKodu", "%" +bransKodu.toLowerCase()+ "%");
        }
        if(bransId != null && !bransId.equals("")) {

            query.setParameter("bransId",Integer.parseInt(bransId));
        }

        JSONArray jsonArray = JSONArray.fromObject(query.list());
        return jsonArray;

    }
}
