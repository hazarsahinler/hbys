package com.example.demo.bus.kullaniciBus;

import com.example.demo.Enum.Role;
import com.example.demo.bus.jwt.JwtService;
import com.example.demo.dao.KullaniciDAO;
import com.example.demo.dao.PersonelDAO;
import com.example.demo.entity.Kullanici;
import com.example.demo.entity.Personel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KullaniciServiceImpl implements KullaniciService {

    private final PersonelDAO personelDAO;
    private final KullaniciDAO kullaniciDAO;
    private final JwtService jwtService;

    public KullaniciServiceImpl(PersonelDAO personelDAO, KullaniciDAO kullaniciDAO, JwtService jwtService) {
        this.personelDAO = personelDAO;
        this.kullaniciDAO = kullaniciDAO;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public JSONObject kullaniciKayit(String kullaniciAd, String kullaniciSifre, Integer personelId) {
        String sifre = BCrypt.hashpw(kullaniciSifre, BCrypt.gensalt());
        JSONObject result = new JSONObject();
        Kullanici kullanici = new Kullanici();
        kullanici.setKullaniciAdi(kullaniciAd);
        kullanici.setKullaniciSifre(sifre);
        kullanici.setRole(Role.Personel);
        Personel personel = personelDAO.getObjectById(Personel.class, personelId);
        kullanici.setPersonel(personel);
        kullanici.setAktif(true);
        List<Kullanici> kullaniciList = kullaniciDAO.kullaniciBul(personelId);
        if (kullaniciList.size() > 0) {
            result.put("Succsess", "false");
            result.put("message", "Personel zaten kayitli");
            return result;

        }
        kullaniciDAO.saveOrUpdate(kullanici);
        result.put("Succsess", "true");
        result.put("Message", "Personel Basariyla Kayit Edildi.");
        return result;

    }

    @Override
    @Transactional
    public JSONObject kullaniciGiris(String kullaniciAd, String kullaniciSifre) {
        Kullanici kullanici = kullaniciDAO.kullaniciBilgiSorgu(kullaniciAd);
        JSONObject resp = new JSONObject();
        if (kullanici != null) {
            if (BCrypt.checkpw(kullaniciSifre, kullanici.getKullaniciSifre())) {
                resp.put("succsess", "true");
                String token = jwtService.generateToken(kullanici.getKullaniciAdi());
                resp.put("token",token);
                return resp;
            } else
                resp.put("succsess", "false");
            resp.put("Message", "Hatali sifre.Lutfen sifreyi duzeltip tekrar deneyin.");
            return resp;
        } else
            resp.put("Succsess", "false");
        resp.put("Message", "Kullanici Bulunamadi.");
        return resp;
    }


}
