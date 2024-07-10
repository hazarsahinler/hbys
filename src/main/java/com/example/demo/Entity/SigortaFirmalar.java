package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sigorta_firmalar",schema="sigorta")
@Data
public class SigortaFirmalar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firma_id")
    private int firmaId;

    @Column(name="firma_adi")
    private String firmaAdi;

}
