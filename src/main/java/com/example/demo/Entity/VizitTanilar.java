package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "vizit_tanilar",schema = "hasta")
@Data
public class VizitTanilar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vizit_tanilar_id")
    private int vizit_tanilar_id;

    @ManyToOne
    @JoinColumn(name="vizit_id")
    private HastaVizit hastaVizit;

    @ManyToOne
    @JoinColumn(name = "tani_id")
    private Tani tani;

    @Column(name = "tani_tarihi")
    private Date taniTarihi;

}
