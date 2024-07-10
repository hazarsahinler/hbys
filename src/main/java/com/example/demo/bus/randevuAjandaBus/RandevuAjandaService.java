package com.example.demo.bus.randevuAjandaBus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public interface RandevuAjandaService {
    /**
     * Bu method başlangıç ve bitiş tarih parametrelerini alır.Dbde bulunan takvim tablosuna bu tarihler arası ekleme yapar.
     * Başlangıç yil/01/01 şeklinde olmak zorundadır.Bitiş ise yil/12/31 şeklinde olmak zorundadır.
     * Bu iki tarih arasi günleri,haftasonu tatillerini,resmi tatilleri tabloda tutar.Resmi tatilin türünüde kaydeder.
     * Tabloda bulunan tarihler tekrar eklenemez.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    JSONObject createTakvim(String startDate, String endDate);

    /**
     * Gerekli parametreler alinir.Önce tarih kontrolü yapılır.Sonrasinda basla ve bitis saatlari 00.00 ayarlanip girilen saat:dakika kadar plusHours plusMinuts yapilir.
     * Sonrasinda girilen gün idleri vasıtasıyla, doktorun hangi günler çalışmak istediğine göre randevu slotları açılır(eğer randevu slotu mevcutsa açılmaz)
     * Basla-bitis arasi muayene suresi kadar dk ilerleterek slotlar açılır.
     * Kisit basla ve bitis saatleri ( mola saatleri ) arasinda randevu slotu açilmaz.
     * Resmi tatilde çalışmak isteyen doktorlar, seçtiği tarihler arasi mevcut olan resmi tatil günlerinde de slot oluşturup randevu verebilir.
     *
     * @param baslangicTarihi
     * @param bitisTarihi
     * @param baslangicSaati
     * @param bitisSaati
     * @param gunler
     * @param muayeneSure
     * @param bransId
     * @param polikinlikId
     * @param personelId
     * @param kisitBasla
     * @param kisitBitis
     * @param resmiTatil
     * @return
     */

    JSONObject createAjanda(String baslangicTarihi, String bitisTarihi,
                            String baslangicSaati, String bitisSaati,
                            String gunler,
                            String muayeneSure, String bransId,
                            String polikinlikId, String personelId,
                            String kisitBasla, String kisitBitis, String resmiTatil, String kayitGuncelle);

    /**
     * girilen parametrelere göre boş slotları geri döndürür.
     *
     * @param personelId
     * @param bransId
     * @param polikinlikId
     * @param basTarihi
     * @param bitTarihi
     * @return
     */
    JSONArray getBosRandevuSlotlar(String personelId, String bransId, String polikinlikId, String basTarihi, String bitTarihi);

    JSONObject deleteRandevuById(String id);

    JSONObject deleteRandevuByTarih(String baslangicTarihi, String bitisTarihi, String personelId);

    JSONObject createRandevu(String hastaId,String randevuId, String hastaNotu);

    JSONObject randevuIptal(String randevuId);

    JSONArray getAktifRandevularByHasta(String hastaId,String baslangicTarihi,String bitisTarihi);

    JSONArray getDoluRandevularByDoktorId(String personelId);

    JSONObject addRezervToRandevu(String slotId,String rezervNot);
    JSONObject addHastaToRezervSlot(String hastaId,String slotId);
}
