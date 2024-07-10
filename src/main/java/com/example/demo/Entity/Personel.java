package com.example.demo.entity;

import com.example.demo.Enum.Uzmanlik;
import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="personel",schema="hastane")
@Data
public class Personel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personel_id")
    private int personelId;

    @Column(name = "tc_kimlik_no")
    private String tcKimlikNo;

    @Column(name = "personel_isim")
    private String personelIsim;

    @Column(name = "personel_soyisim")
    private String personelSoyisim;


    @Column(name = "ise_baslama_tarih")
    private Date iseBaslamaTarih;

    @Column(name = "dogum_tarihi")
    private Date dogumTarihi;


    @ManyToOne
    @JoinColumn(name = "branş_id")
    private Brans brans;

    @Enumerated(EnumType.STRING)
    private Uzmanlik uzmanlik;

    @ManyToOne
    @JoinColumn(name = "polikinlik_id")
    private Polikinlik polikinlik;





}
