package com.example.demo.entity;
import com.example.demo.Enum.TatilTipi;
import com.example.demo.Enum.Uzmanlik;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.type.TrueFalseConverter;

import java.util.Date;

@Entity
@Table(name = "takvim", schema = "hastane")
@Data
public class Takvim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "takvim_id")
    private int takvimId;

    @Column(name = "gun_ismi")
    private String gunIsmi;

    @Column(name = "ay_ismi")
    private String ayIsmi;

    @Column(name="gun")
    private int gun;

    @Column(name="ay")
    private int ay;

    @Column(name = "hafta_sonu_tatil")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean haftaSonuTatil;

    @Column(name="resmi_tatil")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean resmiTatil;

    @Enumerated(EnumType.ORDINAL)
    private TatilTipi tatilTipi;


    @Column(name = "tarih")
    private Date tarih;

}
