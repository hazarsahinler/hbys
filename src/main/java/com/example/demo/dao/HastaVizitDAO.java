package com.example.demo.dao;

import com.example.demo.Util.Utility;
import com.example.demo.entity.HastaVizit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Repository
public class HastaVizitDAO extends BaseDAO {
    private final SessionFactory sessionFactory;
    private final Utility utility;

    public HastaVizitDAO(SessionFactory sessionFactory, Utility utility) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
        this.utility = utility;
    }

    public JSONArray getVizitByParam(Integer bransId, String bransKodu, Integer personelId, Integer poliklinikId, Integer hastaId, String hastaVizitGiris, String hastaVizitCikis) {

        Boolean aktifPasif = true;
        if(hastaVizitGiris != null && !hastaVizitGiris.equals("")) {
            utility.parseDate(hastaVizitGiris);
        }
        if(hastaVizitCikis != null && !hastaVizitCikis.equals("")) {
            utility.parseDate(hastaVizitCikis);
        }

        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT new map(");
        hql.append(" a.polikinlik.polikinlikIsim as polikinlikIsim, ");
        hql.append(" a.personel.uzmanlik || ' ' || a.personel.personelIsim || ' ' || a.personel.personelSoyisim as personelBilgi, ");
        hql.append(" a.hasta.hastaIsim || ' ' || a.hasta.hastaSoyisim as hastaBilgiler) ");
        hql.append(" FROM HastaVizit a ");
        hql.append(" WHERE (1=1) ");

        if (bransId != null) {
            hql.append(" AND a.brans.bransId=:bransId ");
        }
        if (bransKodu != null && !bransKodu.isEmpty()) {
            hql.append(" AND a.brans.bransKodu=:bransKodu ");
        }
        if (personelId != null) {
            hql.append(" AND a.personel.personelId=:personelId ");
        }
        if (poliklinikId != null) {
            hql.append(" AND a.polikinlik.poliklinikId=:poliklinikId ");
        }
        if (hastaId != null) {
            hql.append(" AND a.hasta.hastaId=:hastaId ");
        }
        if (aktifPasif) {
            hql.append(" AND a.aktif=:aktif ");
        }
        if (hastaVizitGiris != null && hastaVizitCikis != null && hastaVizitGiris != "" && hastaVizitCikis != "") {
            hql.append(" AND to_char(a.hastaVizitGiris, 'dd/mm/yyyy') BETWEEN :hastaVizitGiris AND :hastaVizitCikis ");
        } else if (hastaVizitGiris != null && hastaVizitGiris != "") {
            hql.append(" AND to_char(a.hastaVizitTarihi, 'dd/mm/yyyy') >= :hastaVizitGiris ");
        }

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());

        if (bransId != null) {
            query.setParameter("bransId", bransId);
        }
        if (bransKodu != null && !bransKodu.isEmpty()) {
            query.setParameter("bransKodu", bransKodu);
        }
        if (personelId != null) {
            query.setParameter("personelId", personelId);
        }
        if (poliklinikId != null) {
            query.setParameter("poliklinikId", poliklinikId);
        }
        if (hastaId != null) {
            query.setParameter("hastaId", hastaId);
        }
        if (aktifPasif) {
            query.setParameter("aktif", true);
        }
        if (hastaVizitGiris != null && hastaVizitCikis != null && hastaVizitGiris != "" && hastaVizitCikis != "") {
            query.setParameter("hastaVizitGiris", hastaVizitGiris);
            query.setParameter("hastaVizitCikis", hastaVizitCikis);
        } else if (hastaVizitGiris != null && !hastaVizitGiris.equals("")) {
            query.setParameter("hastaVizitGiris", hastaVizitGiris);
        }

        JSONArray respObj = JSONArray.fromObject(query.list());
        return respObj;
    }


    public boolean getCount(Integer bransId, Integer hastaId, Date hastaVizitGiris, Boolean aktif) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strGirisTarihi = sdf.format(hastaVizitGiris);

        StringBuilder hql = new StringBuilder();
        hql.append("select count(a) ");
        hql.append("from HastaVizit a ");
        hql.append("where a.hasta.hastaId = :hastaId ");
        hql.append("and a.brans.bransId = :bransId ");
        hql.append("and to_char(a.hastaVizitGiris, 'dd/mm/yyyy') = :hastaVizitGiris ");
        hql.append("and a.aktif = :aktif");

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("bransId", bransId);
        query.setParameter("hastaId", hastaId);
        query.setParameter("hastaVizitGiris", strGirisTarihi);
        query.setParameter("aktif", aktif);


        Long count = (Long) query.uniqueResult();

        return count > 0;
    }
}
