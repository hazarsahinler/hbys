package com.example.demo.Util;

import com.example.demo.entity.Takvim;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.util.Date;

@Component
public class Utility {
    public Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date parseDateWithFormat(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fark parametresi 2000 yılından sonraki yillara göre belirlenir.örneğin 2024 girildiğinde fark 24 olacaktır.
     * Hicri takvimde kurban ve ramazan bayramları işaretlenmiştir.Sadece fark parametresi yardımıyla istenen yıl üzerinde,
     * miladi takvim dönüşümü yapılır.
     * @param fark
     * @return
     */
    public static LocalDate getKurbanTarihi(Integer fark) {
        // Hicri takvimde Zilhicce(12.ay) ayının 10. günü

        HijrahDate hijrahDate = HijrahDate.of(1421 + fark, 12, 10);
        LocalDate kurbanBayramiMiladi = LocalDate.from(hijrahDate);


        return kurbanBayramiMiladi;
    }

    public LocalDate getRamazanTarihi(Integer fark) {
        //hicri takvimde şevval ayinin 1.günü
        HijrahDate hijrahDate = HijrahDate.of(1421 + fark, 10, 1);
        LocalDate ramazanBayramiMiladi = LocalDate.from(hijrahDate);
        return ramazanBayramiMiladi;
    }
    public Boolean resmiTatilCalisma(String resmiTatil) {
        Boolean resmiTatilCalisma = Boolean.parseBoolean(resmiTatil);
        if (!resmiTatilCalisma) {
            return false;

        }
        return true;
    }

    public Boolean convertBoolean(String value, boolean defaltValue) {
        Boolean response = defaltValue;
        try {
            response = Boolean.parseBoolean(value);
        } catch (Exception ex) {
            return response;
        }

        return response;
    }

    public Integer convertInteger(String value, Integer defaltValue) {
        Integer response = defaltValue;
        try {
            response = Integer.parseInt(value);
        } catch (Exception ex) {
            return response;
        }

        return response;
    }

    public Long convertLong(String value, Long defaltValue) {
        Long response = defaltValue;
        try {
            response = Long.parseLong(value);
        } catch (Exception ex) {
            return response;
        }

        return response;
    }

}
