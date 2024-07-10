package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "randevu_log", schema = "hastane")
@Data
public class RandevuLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "hasta_id", nullable = true)
    private Hasta hasta;

    @ManyToOne
    @JoinColumn(name = "personel_id", nullable = true)
    private Personel personel;

    @ManyToOne
    @JoinColumn(name="randevu_slot_id")
    private RandevuAjanda randevuAjanda;

    @Column(name = "degisiklik_tarihi")
    private LocalDateTime degisiklikTarihi;

    @Column(name="randevu_baslangic")
    private LocalDateTime randevuBaslangic;

    @Column(name="randevu_bitis")
    private LocalDateTime randevuBitis;

    @Column(name = "action")
    private String action;

    @Column(name="aciklama")
    private String aciklama;
}
