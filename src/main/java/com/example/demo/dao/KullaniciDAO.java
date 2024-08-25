package com.example.demo.dao;

import com.example.demo.entity.Kullanici;
import com.example.demo.entity.Personel;
import jakarta.transaction.Transactional;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KullaniciDAO extends BaseDAO{
    private final SessionFactory sessionFactory;

    public KullaniciDAO(SessionFactory sessionFactory, SessionFactory sessionFactory1) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory1;
    }

    public List<Kullanici> kullaniciBul(Integer personelId){
        StringBuilder hql = new StringBuilder();
        hql.append( " from Kullanici a " );
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.personel.personelId =: personelId");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("personelId", personelId);

        return query.list();

    }

    @Transactional
    public Kullanici kullaniciBilgiSorgu(String kullaniciAd){
        StringBuilder hql = new StringBuilder();
        hql.append( " from Kullanici a " );
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.kullaniciAdi =: kullaniciAdi");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("kullaniciAdi", kullaniciAd);
        query.setMaxResults(1);

        return (Kullanici) query.uniqueResult();

    }
}
