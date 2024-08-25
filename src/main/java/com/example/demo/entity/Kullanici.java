package com.example.demo.entity;

import com.example.demo.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(schema = "hastane",name = "kullanici_bilgi")
@Data
public class Kullanici {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kullanici_id")
    private int kullaniciId;

    @Column(name="kullanici_adi")
    private String kullaniciAdi;

    @Column(name="kullanici_sifre")
    private String kullaniciSifre;

    @OneToOne
    @JoinColumn(name="personel_id")
    private Personel personel;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "aktif")
    @Convert(converter = TrueFalseConverter.class)
    private Boolean aktif;

}
