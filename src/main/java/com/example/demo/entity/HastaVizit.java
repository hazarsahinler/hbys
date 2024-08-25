package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.type.TrueFalseConverter;
import org.hibernate.type.YesNoConverter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hasta_vizit", schema = "hasta")
@Data
public class HastaVizit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hasta_vizit_id")
    private int hastaVizitId;

    @ManyToOne
    @JoinColumn(name = "hasta_id")
    private Hasta hasta;

    @ManyToOne
    @JoinColumn(name = "brans_id")
    private Brans brans;

    @ManyToOne
    @JoinColumn(name = "polikinlik_id")
    private Polikinlik polikinlik;

    @ManyToOne
    @JoinColumn(name = "personel_id")
    private Personel personel;

    @Column(name = "hasta_vizit_giris")
    private Date hastaVizitGiris;

    @Column(nullable = true, name = "hasta_vizit_cikis")
    private Date hastaVizitCikis;

    @Convert(converter = TrueFalseConverter.class)
    @Column(name = "aktif")
    private Boolean aktif;

    @Convert(converter = TrueFalseConverter.class)
    @Column(name = "randevulu")
    private Boolean randevulu;


}
