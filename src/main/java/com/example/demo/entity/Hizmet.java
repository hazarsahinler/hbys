package com.example.demo.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "hizmetler",schema="hastane")
@Data
public class Hizmet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "hizmet_id")
    private int hizmetId;

    @Column(name = "hizmet_adi")
    private String hizmetAdi;

    @Column(name = "hizmet_kodu")
    private String hizmetKodu;



}
