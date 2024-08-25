package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "polikinlikler",schema="hastane")
@Data
public class Polikinlik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "polikinlik_id")
    private int polikinlikId;

    @Column(name = "polikinlik_isım")
    private String polikinlikIsim;

    @ManyToOne
    @JoinColumn(name = "brans_id")
    private Brans brans;



}
