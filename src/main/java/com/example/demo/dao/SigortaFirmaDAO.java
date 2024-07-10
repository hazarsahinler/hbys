package com.example.demo.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SigortaFirmaDAO extends BaseDAO{
    private final SessionFactory sessionFactory;
    public SigortaFirmaDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
