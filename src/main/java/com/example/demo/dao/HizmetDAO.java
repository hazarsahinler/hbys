    package com.example.demo.dao;

    import com.example.demo.entity.HastaHizmetler;
    import com.example.demo.entity.HastaVizit;
    import com.example.demo.entity.Hizmet;
    import net.sf.json.JSONArray;
    import net.sf.json.JSONObject;
    import org.hibernate.SessionFactory;
    import org.hibernate.query.Query;
    import org.springframework.stereotype.Repository;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    @Repository
    public class HizmetDAO extends BaseDAO{
        private final SessionFactory sessionFactory;
        private final HastaVizitDAO hastaVizitDAO;

        public HizmetDAO(SessionFactory sessionFactory, SessionFactory sessionFactory1, HastaVizitDAO hastaVizitDAO) {
            super(sessionFactory);
            this.sessionFactory = sessionFactory1;
            this.hastaVizitDAO = hastaVizitDAO;
        }
        public JSONArray getHizmetList(Integer hastaVizitId) {
            Boolean aktifPasif = true;

            StringBuilder hql = new StringBuilder();
            hql.append(" Select new map(");
            hql.append(" a.hastaVizit.hastaVizitId as hastaVizitId, ");
            hql.append(" a.hizmet.hizmetAdi as hizmetAdi) ");
            hql.append(" from HastaHizmetler a ");
            hql.append(" where (1=1) ");
            if (hastaVizitId != null && hastaVizitId != 0) {
                hql.append(" and a.hastaVizit.hastaVizitId =: hastaVizitId ");
            }
            hql.append(" and a.aktifPasif =:aktifPasif ");  // Added space at the beginning

            Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
            if (hastaVizitId != null && hastaVizitId != 0) {
                query.setParameter("hastaVizitId", hastaVizitId);
            }
            if (aktifPasif != null && aktifPasif) {
                query.setParameter("aktifPasif", true);
            }
            JSONArray jsonArray = JSONArray.fromObject(query.list());
            return jsonArray;
        }


    }
