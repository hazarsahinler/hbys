package com.example.demo.bus.kullaniciBus;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

public interface KullaniciService {

    JSONObject kullaniciKayit(String kullaniciAd,String kullaniciSifre,Integer personelId);

    JSONObject kullaniciGiris(String kullaniciAd,String kullaniciSifre);
}
