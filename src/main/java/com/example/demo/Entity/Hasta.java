package com.example.demo.entity;

import com.example.demo.Enum.SigortaTipi;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Table(name = "hastalar", schema = "hasta")
@Data
public class Hasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hastaId;

    @Column(name = "tc_kimlik_no")
    private String tcKimlikNo;

    @Column(name = "hasta_isim")
    private String hastaIsim;

    @Column(name = "hasta_soyisim")
    private String hastaSoyisim;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firma_id", nullable = true)
    private SigortaFirmalar sigortaFirmalar;


    @Enumerated(EnumType.STRING)
    private SigortaTipi sigortaTipi;


    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "mail")
    private String mail;

    @Column(nullable = false, name = "dogum_tarihi")
    private Date dogumTarihi;

}
