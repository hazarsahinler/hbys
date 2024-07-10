package com.example.demo.dao;

import com.example.demo.Enum.Uzmanlik;
import net.sf.json.JSONArray;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PersonelDAO extends BaseDAO{
    private final SessionFactory sessionFactory;

    public PersonelDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public JSONArray getPersonel() {
        StringBuilder hql = new StringBuilder();
        hql.append("Select new map(" +
                "a.uzmanlik || ' ' || a.personelIsim || ' ' || a.personelSoyisim as personelInfo) " +
                "from Personel a");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        JSONArray jsonArray = JSONArray.fromObject(query.list());

        return jsonArray;
    }
    public JSONArray getByParam(String personelIsim, String personelSoyisim,String brans,String uzmanlik,String polikinlik) {
        StringBuilder hql = new StringBuilder();
        hql.append("Select new map( a.uzmanlik || ' ' || a.personelIsim || ' ' || personelSoyisim as personel, ");
        hql.append(" a.brans.bransIsmi as brans, ");
        hql.append(" a.polikinlik.polikinlikIsim as polikinlik) ");
        hql.append("from Personel a ");
        hql.append("WHERE 1=1 ");
        if(personelIsim!=null && !personelIsim.isEmpty()){
            hql.append(" AND a.personelIsim = :personelIsim");
        }
        if(personelSoyisim!=null && !personelSoyisim.isEmpty()){
            hql.append(" AND a.personelSoyisim = :personelSoyisim");
        }
        if (brans != null && !brans.isEmpty()) {
            hql.append(" AND a.brans.bransIsmi = :brans");
        }
        if (uzmanlik != null && !uzmanlik.isEmpty()) {
            hql.append(" AND a.uzmanlik = :uzmanlik");
        }
        if (polikinlik != null && !polikinlik.isEmpty()) {
            hql.append(" AND a.polikinlik.polikinlikIsim = :polikinlik");

        }
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if(personelIsim!=null && !personelIsim.isEmpty()){
            query.setParameter("personelIsim", personelIsim);

        }
        if (personelSoyisim != null && !personelSoyisim.isEmpty()) {
            query.setParameter("personelSoyisim", personelSoyisim);
        }
        if (brans!= null && !brans.isEmpty()) {
            query.setParameter("brans", brans);
        }
        if (uzmanlik != null && !uzmanlik.isEmpty()) {
            try {
                Uzmanlik uzmanlikEnum = Uzmanlik.valueOf(uzmanlik);
                query.setParameter("uzmanlik", uzmanlikEnum);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Geçersiz uzmanlık değeri: " + uzmanlik);
            }
        }
        if (polikinlik!= null && !polikinlik.isEmpty()) {
            query.setParameter("polikinlik", polikinlik);
        }
        JSONArray jsonArray = JSONArray.fromObject(query.list());
        return jsonArray;
    }

}
