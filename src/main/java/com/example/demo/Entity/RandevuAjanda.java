package com.example.demo.entity;

import com.example.demo.Enum.RandevuDurum;
import com.example.demo.Enum.Uzmanlik;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.type.TrueFalseConverter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "randevu_ajandalar", schema = "hastane")
@Data
public class RandevuAjanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "randevu_ajanda_id")
    private int randevuAjandaId;

    @Column(name = "baslangic_saati")
    private LocalDateTime baslangicSaati;

    @Column(name = "bitis_saat")
    private LocalDateTime bitisSaati;

    @Column(name = "muayene_sure")
    private int muayeneSure;

    @ManyToOne
    @JoinColumn(name = "gun_id")
    private Takvim takvim;

    @ManyToOne
    @JoinColumn(name = "brans_id")
    private Brans brans;

    @ManyToOne
    @JoinColumn(name = "polikinlik_id")
    private Polikinlik polikinlik;

    @ManyToOne
    @JoinColumn(name = "personel_id")
    private Personel personel;

    @ManyToOne
    @JoinColumn(name = "hasta_id", nullable = true)
    private Hasta hasta;

    @ManyToOne
    @JoinColumn(name = "hasta_vizit_id", nullable = true)
    private HastaVizit hastaVizit;

    @Column(name = "hasta_not", nullable = true)
    private String hastaNot;

    @Column(name = "doktor_rezerv_not", nullable = true)
    private String doktorRezervNot;

    @Enumerated(EnumType.ORDINAL)
    private RandevuDurum randevuDurum;

    @Column(name = "aktif")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean aktif;

}

