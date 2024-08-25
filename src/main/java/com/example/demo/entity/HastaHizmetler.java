package com.example.demo.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.type.TrueFalseConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="hasta_hizmetler", schema="hasta")
@Data
public class HastaHizmetler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hasta_hizmet_id")
    private int hastaHizmetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vizit_id")
    private HastaVizit hastaVizit;

    @ManyToOne
    @JoinColumn(name = "hizmet_id")
    private Hizmet hizmet;

    @Column(name = "hizmet_tarihi")
    private Date hizmetTarihi;

    @Column(name = "aktif_pasif")
    @Convert(converter = TrueFalseConverter.class)
    private boolean aktifPasif;

    @ManyToOne
    @JoinColumn(name="personel_id")
    private Personel personel;
}
