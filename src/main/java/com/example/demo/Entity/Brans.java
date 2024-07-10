package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Table(name = "branslar", schema="hastane")
@Data
public class Brans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brans_id")
    private int bransId;

    @Column(name = "brans_ismi")
    private String bransIsmi;

    @Column(name = "brans_kodu")
    private String bransKodu;


}
