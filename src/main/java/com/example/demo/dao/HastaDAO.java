package com.example.demo.dao;

import com.example.demo.Enum.SigortaTipi;
import com.example.demo.Enum.Uzmanlik;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import static java.lang.Integer.parseInt;

@Repository
public class HastaDAO extends BaseDAO {


    private final SessionFactory sessionFactory;

    public HastaDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public JSONArray getAllHastalar() {
        StringBuilder hql = new StringBuilder();
        hql.append(" Select new map(");
        hql.append("a.hastaIsim || ' ' || a.hastaSoyisim as hastaAdi, ");
        hql.append("a.tcKimlikNo as tcNo, ");
        hql.append("a.sigortaFirmalar as sigortaFirmasi, ");
        hql.append("a.sigortaTipi as sigortaTipi, ");
        hql.append("to_char(a.dogumTarihi, 'dd/mm/yyyy') as dogumTarihi)");
        hql.append(" from Hasta a ");

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        JSONArray jsonArray = JSONArray.fromObject(query.list());
        return jsonArray;
    }


    public JSONArray getByParam(String tcKimlikNo, String hastaIsim, String hastaSoyisim, String sigortaTipi, String telNo, String mail) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new map (h.tcKimlikNo as tcKimlikNo, ");
        hql.append("h.hastaIsim || ' ' || h.hastaSoyisim as hastaAdi, ");
        hql.append("h.telNo as telNo, ");
        hql.append("h.sigortaTipi as sigortaTipi, ");
        hql.append("h.mail as mail) ");
        hql.append("FROM Hasta h ");
        hql.append("WHERE 1=1 ");

        if (tcKimlikNo != null && !tcKimlikNo.isEmpty()) {
            hql.append("AND h.tcKimlikNo like :tcKimlikNo ");
        }

        if (hastaIsim != null && !hastaIsim.isEmpty()) {
            hql.append("AND lower(h.hastaIsim) like :hastaIsim ");
        }

        if (hastaSoyisim != null && !hastaSoyisim.isEmpty()) {
            hql.append("AND lower(h.hastaSoyisim) like :hastaSoyisim ");
        }

        if (sigortaTipi != null && !sigortaTipi.equals("")) {

            hql.append("AND h.sigortaTipi =:sigortaTipi ");
        }

        if (telNo != null && !telNo.isEmpty()) {
            hql.append("AND lower(h.telNo) like :telNo ");
        }

        if (mail != null && !mail.isEmpty()) {
            hql.append("AND lower(h.mail) like :mail ");
        }

        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());

        if (tcKimlikNo != null && !tcKimlikNo.isEmpty()) {
            query.setParameter("tcKimlikNo", tcKimlikNo);
        }


        if (hastaIsim != null && !hastaIsim.isEmpty()) {
            query.setParameter("hastaIsim","%" + hastaIsim.toLowerCase()+"%");
        }

        if (hastaSoyisim != null && !hastaSoyisim.isEmpty()) {
            query.setParameter("hastaSoyisim", "%"+hastaSoyisim.toLowerCase()+"%");
        }

        if (sigortaTipi != null && !sigortaTipi.isEmpty()) {
            try {
                SigortaTipi sigortaTipiEnum = SigortaTipi.valueOf(sigortaTipi);
                query.setParameter("sigortaTipi", sigortaTipiEnum );
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Geçersiz sigortaTipi değeri: " + sigortaTipi);
            }
        }

        if (telNo != null && !telNo.isEmpty()) {
            query.setParameter("telNo","%" + telNo.toLowerCase()+"%");
        }

        if (mail != null && !mail.isEmpty()) {
            query.setParameter("mail", "%" + mail.toLowerCase() + "%");
        }
        JSONArray jsonArray = JSONArray.fromObject(query.list());
        return jsonArray;
    }


}
