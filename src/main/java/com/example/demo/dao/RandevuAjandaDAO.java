package com.example.demo.dao;

import com.example.demo.Enum.RandevuDurum;
import com.example.demo.Util.Utility;
import com.example.demo.entity.RandevuAjanda;
import net.sf.json.JSONArray;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class RandevuAjandaDAO extends BaseDAO {

    private final Utility utility;
    private final SessionFactory sessionFactory;

    public RandevuAjandaDAO(SessionFactory sessionFactory, Utility utility, SessionFactory sessionFactory1) {
        super(sessionFactory);
        this.utility = utility;
        this.sessionFactory = sessionFactory1;
    }

    public JSONArray getBosRandevuSlotlar(String personelId, String bransId, String polikinlikId, String basTarihi, String bitTarihi) {
        String strGirisTarihi = null;
        String strCikisTarihi = null;
        if (basTarihi != null && basTarihi != "") {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            strGirisTarihi = sdf.format(utility.parseDate(basTarihi));

        }
        if (bitTarihi != null && bitTarihi != "") {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            strCikisTarihi = sdf.format(utility.parseDate(bitTarihi));
        }

        RandevuDurum randevuDurum = RandevuDurum.Bos;

        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT new map(");
        hql.append(" to_char(a.baslangicSaati,'HH24:MI') || '-' || to_char(a.bitisSaati,'HH24:MI') as randevuSaat, ");
        hql.append(" a.brans.bransIsmi as brans, ");
        hql.append(" a.polikinlik.polikinlikIsim as polikinlik, ");
        hql.append(" a.personel.personelIsim || ' ' || a.personel.personelSoyisim as personelBilgi, ");
        hql.append(" to_char(a.takvim.tarih,'dd/mm/yyyy') as tarih) ");
        hql.append(" FROM RandevuAjanda a ");
        hql.append(" WHERE (1=1)");

        if (personelId != null && personelId != "") {
            hql.append(" AND a.personel.personelId =: personelId ");
        }
        if (bransId != null && bransId != "") {
            hql.append(" AND a.brans.bransId =: bransId ");
        }
        if (polikinlikId != null && polikinlikId != "") {
            hql.append(" AND a.polikinlik.polikinlikId =: polikinlikId ");
        }
        hql.append(" AND a.randevuDurum =: randevuDurum");
        if (basTarihi != null && basTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'dd/mm/yyyy') >= :basTarihi ");
        }
        if (bitTarihi != null && bitTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'dd/mm/yyyy') <= :bitTarihi ");
        }
        if (basTarihi != null && bitTarihi != null && basTarihi != "" && bitTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'dd/mm/yyyy') BETWEEN :basTarihi AND :bitTarihi ");

        }
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if (bransId != null && bransId != "") {
            query.setParameter("bransId", utility.convertInteger(bransId, 0));
        }

        query.setParameter("randevuDurum", randevuDurum);


        if (personelId != null && personelId != "") {
            query.setParameter("personelId", utility.convertInteger(personelId, 0));
        }
        if (polikinlikId != null && polikinlikId != "") {
            query.setParameter("polikinlikId", utility.convertInteger(polikinlikId, 0));
        }
        if (basTarihi != null && !basTarihi.isEmpty()) {
            query.setParameter("basTarihi", strGirisTarihi);
        }
        if (bitTarihi != null && !bitTarihi.isEmpty()) {
            query.setParameter("bitTarihi", strCikisTarihi);
        }
        JSONArray resplist = JSONArray.fromObject(query.list());
        return resplist;
    }

    public List<RandevuAjanda> getRandevuAjandaByParam(LocalDateTime basTar, LocalDateTime bitTar, Integer doktorId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.baslangicSaati >= :basTar ");
        hql.append(" And a.bitisSaati <= :bitTar ");
        hql.append(" And a.personel.personelId = :doktorId ");

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("basTar", basTar);
        query.setParameter("bitTar", bitTar);
        query.setParameter("doktorId", doktorId);

        return query.list();
    }

    public List<RandevuAjanda> getRandevuByTarih(Date basTar, Date bitTar, Integer doktorId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.takvim.tarih >= :basTar ");
        hql.append(" And a.takvim.tarih <= :bitTar ");
        hql.append(" And a.personel.personelId = :doktorId ");

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("basTar", basTar);
        query.setParameter("bitTar", bitTar);
        query.setParameter("doktorId", doktorId);

        return query.list();
    }

    public List<RandevuAjanda> getRandevuByHastaAndPersonel(Integer hastaId, Integer personelId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.hasta.hastaId = :hastaId ");
        hql.append(" And a.personel.personelId = :personelId ");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("hastaId", hastaId);
        query.setParameter("personelId", personelId);
        return query.list();
    }

    public List<RandevuAjanda> getRandevuByHastaAndBrans(Integer hastaId, Integer bransId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.hasta.hastaId = :hastaId ");
        hql.append(" And a.brans.bransId = :bransId ");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("hastaId", hastaId);
        query.setParameter("bransId", bransId);
        return query.list();
    }

    public JSONArray getAktifRandevularByHasta(Integer hastaId, String baslangicTarihi, String bitisTarihi) {
        if (baslangicTarihi != null && baslangicTarihi != "") {
            utility.parseDate(baslangicTarihi);
        }
        if (bitisTarihi != null && bitisTarihi != "") {
            utility.parseDate(bitisTarihi);
        }

        StringBuilder hql = new StringBuilder();
        hql.append(" Select new map(");
        hql.append(" a.hasta.tcKimlikNo as TC,");
        hql.append(" a.hasta.hastaIsim || ' ' || a.hasta.hastaSoyisim as hasta,");
        hql.append(" a.brans.bransIsmi as bransIsmi,");
        hql.append(" to_char(a.baslangicSaati,'HH24:MI') || '-' || to_char(a.bitisSaati,'HH24:MI') as randevuSaat, ");
        hql.append(" to_char(a.takvim.tarih,'yyyy-MM-dd') as tarih, ");
        hql.append(" a.personel.personelIsim || ' ' || a.personel.personelSoyisim as personel)");
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        if (baslangicTarihi != null && bitisTarihi != null && baslangicTarihi != "" && bitisTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'yyyy-MM-dd') BETWEEN :baslangicTarihi AND :bitisTarihi ");
        }
        if (baslangicTarihi != null && baslangicTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'yyyy-MM-dd') >= :baslangicTarihi ");
        }
        if (bitisTarihi != null && bitisTarihi != "") {
            hql.append(" AND to_char(a.takvim.tarih, 'yyyy-MM-dd') <= :bitisTarihi ");
        }
        if (hastaId != null && hastaId != 0) {
            hql.append(" And a.hasta.hastaId = :hastaId ");
        }
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if (hastaId != null && hastaId != 0) {
            query.setParameter("hastaId", hastaId);
        }
        if (baslangicTarihi != null && baslangicTarihi != "") {
            query.setParameter("baslangicTarihi", baslangicTarihi);
        }
        if (bitisTarihi != null && bitisTarihi != "") {
            query.setParameter("bitisTarihi", bitisTarihi);
        }


        JSONArray response = JSONArray.fromObject(query.list());
        return response;
    }

    public JSONArray getDoluRandevularByDoktor(Integer personelId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" Select new map(");
        hql.append(" a.hasta.tcKimlikNo as TC,");
        hql.append(" a.hasta.hastaIsim || ' ' || a.hasta.hastaSoyisim as hasta,");
        hql.append(" a.hastaNot as not,");
        hql.append(" to_char(a.baslangicSaati,'HH24:MI') || '-' || to_char(a.bitisSaati,'HH24:MI') as randevuSaat, ");
        hql.append(" to_char(a.takvim.tarih,'dd/mm/yyyy') as tarih) ");
        hql.append(" From RandevuAjanda a ");
        hql.append(" Where a.aktif = true ");
        hql.append(" And a.personel.personelId = :personelId ");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("personelId", personelId);
        JSONArray response = JSONArray.fromObject(query.list());
        return response;

    }
    public List<RandevuAjanda> getRezervRandevular(){
        StringBuilder hql = new StringBuilder();
        hql.append("FROM RandevuAjanda a");
        hql.append(" WHERE a.aktif = true ");
        hql.append(" AND a.randevuDurum = :randevuDurum");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("randevuDurum", RandevuDurum.rezerv);
        List<RandevuAjanda> rezervRandevular = query.list();
        return rezervRandevular;
    }
}
