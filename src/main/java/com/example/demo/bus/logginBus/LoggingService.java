package com.example.demo.bus.logginBus;

import com.example.demo.entity.Hasta;
import com.example.demo.entity.Personel;
import com.example.demo.entity.RandevuAjanda;
import com.example.demo.entity.RandevuLog;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoggingService {
    private final SessionFactory sessionFactory;

    public LoggingService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void logRandevuDegisiklik(Hasta hasta, String action, String aciklama, Personel personel,RandevuAjanda randevuAjanda,LocalDateTime randevuBaslangic,LocalDateTime randevuBitis) {

        RandevuLog log = new RandevuLog();
        log.setHasta(hasta);
        log.setAction(action);
        log.setAciklama(aciklama);
        log.setPersonel(personel);
        log.setDegisiklikTarihi(LocalDateTime.now());
        log.setRandevuBaslangic(randevuBaslangic);
        log.setRandevuBitis(randevuBitis);
        log.setRandevuAjanda(randevuAjanda);
        sessionFactory.getCurrentSession().save(log);
    }

}
