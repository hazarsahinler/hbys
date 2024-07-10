package com.example.demo.entity;

import lombok.Data;

import jakarta.persistence.*;


@Entity
@Table(name = "tanilar",schema="hastane")
@Data
public class Tani {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "tani_id")
    private int tani_id;

    @Column(name = "tani_kod")
    private String taniKod;

    @Column(name = "tani_ad")
    private String taniAd;




}
