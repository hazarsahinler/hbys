package com.example.demo.dao;

import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BransDAO extends BaseDAO{
    private final SessionFactory sessionFactory;

    public BransDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
 /*   private final SessionFactory sessionFactory;

    public BransDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }*/


    public JSONArray getBranslar() {
        StringBuilder hql = new StringBuilder();
        hql.append("Select new map (a.bransIsmi as bransIsmi, a.bransKodu as bransKodu) from Brans a");
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        return JSONArray.fromObject(query.list());
    }


    public JSONArray getBranslarByParam(Integer bransId, String bransIsmi, String bransKodu) {
        StringBuilder hql = new StringBuilder();
        hql.append("Select new map( ");
        hql.append(" a.bransIsmi as bransIsmi, ");
        hql.append(" a.bransKodu as bransKodu, ");
        hql.append(" a.bransId as bransId )");
        hql.append(" from Brans a ");
        hql.append(" Where 1=1 ");
        if (bransId != null && bransId != 0) {
            hql.append(" and a.bransId = :bransId");
        }
        if (bransIsmi != null && bransIsmi != "") {
            hql.append(" and lower(a.bransIsmi) like :bransIsmi ");
        }
        if (bransKodu != null && bransKodu != "") {
            hql.append(" and a.bransKodu = :bransKodu");
        }
        Query query = sessionFactory.getCurrentSession().createQuery(String.valueOf(hql));
        if (bransId != null && bransId != 0) {
            query.setParameter("bransId", bransId);
        }
        if (bransIsmi != null && bransIsmi != "") {
            query.setParameter("bransIsmi", "%" + bransIsmi.toLowerCase() + "%");
        }
        if (bransKodu != null && bransKodu != "") {
            query.setParameter("bransKodu", bransKodu);
        }
        return JSONArray.fromObject(query.list());

    }




    @Transactional
    public JSONObject deleteBrans(Integer bransId, String bransIsmi, String bransKodu) {
        HibernateTemplate hibernateTemplate;
        StringBuilder hql = new StringBuilder();
        JSONArray jsonArray = getBranslarByParam(bransId, bransIsmi, bransKodu);
        JSONObject jsonObject = new JSONObject();
        if (jsonArray.size() != 0) {
            hql.append("Delete from Brans ");
            hql.append(" Where (1=1)  ");
            if (bransId != null && bransId != 0) {
                hql.append("AND bransId = :bransId ");
            }
            if (bransIsmi != null && !bransIsmi.isEmpty()) {
                hql.append("AND bransIsmi = :bransIsmi ");
            }
            if (bransKodu != null && !bransKodu.isEmpty()) {
                hql.append("AND bransKodu = :bransKodu ");
            }
            Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
            if (bransId != null && bransId != 0) {
                query.setParameter("bransId", bransId);
            }
            if (bransIsmi != null && bransIsmi != "") {
                query.setParameter("bransIsmi", bransIsmi);
            }
            if (bransKodu != null && bransKodu != "") {
                query.setParameter("bransKodu", bransKodu);
            }
            int i = query.executeUpdate();
            if (i != 0) {
                jsonObject.put("message", "Delete is succsess");
                return jsonObject;
            } else {

                jsonObject.put("message", "ERROR");
                return jsonObject;
            }


        }
        jsonObject.put("message", "No record found");
        return jsonObject;
    }



    @Transactional
    public JSONObject insertBrans(String bransIsmi, String bransKodu) {
        StringBuilder hql = new StringBuilder();
        Integer bransId = 0;
        String tempBransIsmi = bransIsmi;
        String tempBransKodu = bransKodu;
        bransIsmi = null;
        JSONArray jsonArray = getBranslarByParam(null,null, bransKodu);
        JSONObject jsonObject = new JSONObject();
        //brans kodundan tablodaki yeri bulduk.
        if (jsonArray.size() != 0) {
            bransIsmi=tempBransIsmi;
            hql.append("Update Brans set ");
            if(bransIsmi!=null && bransIsmi != "") {
                hql.append(" bransIsmi = :bransIsmi, ");
            }
            if(bransKodu!=null && bransKodu != "") {
                hql.append(" bransKodu = :bransKodu ");
            }
            hql.append(" where (bransKodu = :bransKodu) ");
            Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
            if(bransIsmi!=null && bransIsmi != "") {
                query.setParameter("bransIsmi", bransIsmi);
            }else{
                jsonObject.put("message","Parameters cannot be null!!!");
                return jsonObject;
            }
            if(bransKodu!=null && bransKodu != "") {
                query.setParameter("bransKodu", bransKodu);
            }else{
                jsonObject.put("message","Parameters cannot be null!!!");
                return jsonObject;
            }
            int i = query.executeUpdate();
            if (i != 0) {
                jsonObject.put("message", "update is succsess");
                return jsonObject;
            }else{
                jsonObject.put("message", "Insert ERROR");
                return jsonObject;
            }
        }
        //brans isminden bulalım.
        else{
            bransIsmi=tempBransIsmi;
            bransKodu=null;
            JSONArray jsonArray1=getBranslarByParam(bransId,bransIsmi,bransKodu);
            if(jsonArray1.size()!=0) {
                bransKodu=tempBransKodu;
                hql.append("Update Brans  set ");
                if(bransIsmi!=null && bransIsmi != "") {
                    hql.append(" bransIsmi = :bransIsmi, ");
                }
                if(bransKodu!=null && bransKodu != "") {
                    hql.append(" bransKodu = :bransKodu ");
                }
                hql.append(" where (bransIsmi = :bransIsmi) ");
                Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
                if(bransIsmi!=null && bransIsmi != "") {
                    query.setParameter("bransIsmi", bransIsmi);
                }else
                {
                    jsonObject.put("message","Parameters cannot be null!!!");
                    return jsonObject;
                }
                if(bransKodu!=null && bransKodu != "") {
                    query.setParameter("bransKodu", bransKodu);
                }else
                {
                    jsonObject.put("message","Parameters cannot be null!!!");
                    return jsonObject;
                }
                int i = query.executeUpdate();
                if (i != 0) {
                    jsonObject.put("message", "Update is succsess");
                }else {
                    jsonObject.put("message", "ERROR");
                }
                //brans ismide mevcut değil ise aşağıdaki döngüye girecek.
            }else{
                bransIsmi=tempBransIsmi;
                bransKodu=tempBransKodu;
                hql.append(" Insert into Brans(bransIsmi,bransKodu) values( ");
                if(bransIsmi != null && bransIsmi != "") {
                    hql.append("  :bransIsmi, ");
                }else{
                    jsonObject.put("message","Parameters cannot be null!!!");
                    return jsonObject;
                }
                if(bransKodu != null && bransKodu != "") {
                    hql.append("  :bransKodu)   ");
                }else{
                    jsonObject.put("message","Parameters cannot be null!!!");
                    return jsonObject;
                }
                Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
                query.setParameter("bransIsmi", bransIsmi);
                query.setParameter("bransKodu", bransKodu);

                int i = query.executeUpdate();
                if (i != 0) {
                    jsonObject.put("message", "New Brans added.");
                    return jsonObject;
                }else{
                    jsonObject.put("message", "ERROR");
                    return jsonObject;
                }


            }

        }
        return jsonObject;


    }
}
