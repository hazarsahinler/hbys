package com.example.demo.bus.randevuAjandaBus;

import com.example.demo.Enum.RandevuDurum;
import com.example.demo.Enum.TatilTipi;

import com.example.demo.Util.Utility;
import com.example.demo.bus.logginBus.LoggingService;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class RandevuAjandaServiceImpl implements RandevuAjandaService {

    private final RandevuAjandaDAO randevuAjandaDAO;
    private final LoggingService loggingService;
    private final Utility utility;
    private final BransDAO bransDAO;
    private final PolikinlikDAO polikinlikDAO;
    private final PersonelDAO personelDAO;
    private final HastaDAO hastaDAO;

    public RandevuAjandaServiceImpl(RandevuAjandaDAO randevuAjandaDAO, LoggingService loggingService, Utility utility, BransDAO bransDAO, PolikinlikDAO polikinlikDAO, PersonelDAO personelDAO, HastaDAO hastaDAO) {
        this.randevuAjandaDAO = randevuAjandaDAO;
        this.loggingService = loggingService;
        this.utility = utility;
        this.bransDAO = bransDAO;
        this.polikinlikDAO = polikinlikDAO;
        this.personelDAO = personelDAO;
        this.hastaDAO = hastaDAO;
    }

    @Override
    @Transactional
    public JSONObject createTakvim(String startDate, String endDate) {
        JSONObject respObj = new JSONObject();

        List<Takvim> takvimList1 = randevuAjandaDAO.getObjectsByParam(Takvim.class, "tarih", utility.parseDate(startDate));
        List<Takvim> takvimList2 = randevuAjandaDAO.getObjectsByParam(Takvim.class, "tarih", utility.parseDate(endDate));
        if (takvimList1 != null && takvimList1.size() != 0) {
            respObj.put("succsess", false);
            respObj.put("message", "BU KAYIT MEVCUT");
            return respObj;
        }
        if (takvimList2 != null && takvimList2.size() != 0) {
            respObj.put("succsess", false);
            respObj.put("message", "BU KAYIT MEVCUT!!");
            return respObj;
        }


        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.getMonthValue() != 1 || start.getDayOfMonth() != 1 || end.getMonthValue() != 12 || end.getDayOfMonth() != 31) {
            respObj.put("succsess", false);
            respObj.put("message", "Girdiginiz tarihler 01/01 den baslayip 31/12 de bitmelidir.");
            return respObj;
        }

        LocalDate ilk = LocalDate.of(2000, 1, 1);
        List<Takvim> takvimList = new ArrayList<>();
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {
            Takvim takvim = new Takvim();
            takvim.setTarih(Date.valueOf(currentDate));
            takvim.setGunIsmi(currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("tr", "TR")));
            takvim.setAyIsmi(currentDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("tr", "TR")));
            takvim.setAy(Integer.valueOf(currentDate.getMonthValue()));
            takvim.setGun(Integer.valueOf(currentDate.getDayOfMonth()));
            //hafta sonu tatili eklemek için.
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            takvim.setHaftaSonuTatil(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            takvim.setResmiTatil(false);

            //resmi tatilleri takip edecek.
            if (currentDate.getMonthValue() == 1 && currentDate.getDayOfMonth() == 1) {
                takvim.setTatilTipi(TatilTipi.YilBasi);// yılbaşı
                takvim.setResmiTatil(true);
            }

            if (currentDate.getMonthValue() == 4 && currentDate.getDayOfMonth() == 23) {
                takvim.setTatilTipi(TatilTipi.EgemenlikVeCocuk); // 23 nisan
                takvim.setResmiTatil(true);

            }
            if (currentDate.getMonthValue() == 5 && currentDate.getDayOfMonth() == 1) {
                takvim.setTatilTipi(TatilTipi.Isci); // 1 mayıs
                takvim.setResmiTatil(true);

            }
            if (currentDate.getMonthValue() == 5 && currentDate.getDayOfMonth() == 19) {
                takvim.setTatilTipi(TatilTipi.GenclikVeSpor); // 19 mayıs
                takvim.setResmiTatil(true);

            }
            if (currentDate.getMonthValue() == 7 && currentDate.getDayOfMonth() == 15) {
                takvim.setTatilTipi(TatilTipi.OnBesTemmuz); // 15 temmuz
                takvim.setResmiTatil(true);

            }
            if (currentDate.getMonthValue() == 8 && currentDate.getDayOfMonth() == 30) {
                takvim.setTatilTipi(TatilTipi.Zafer);// 30 ağustos
                takvim.setResmiTatil(true);

            }
            if (currentDate.getMonthValue() == 10 && currentDate.getDayOfMonth() == 29) {
                takvim.setTatilTipi(TatilTipi.Cumhuriyet); // 29 ekim
                takvim.setResmiTatil(true);
            }

            int fark = (int) ChronoUnit.YEARS.between(ilk, currentDate);
            //yili fark kadar ileri al
            LocalDate ramazanBasi = utility.getRamazanTarihi(fark);
            LocalDate kurbanBasi = utility.getKurbanTarihi(fark);


            //ay ve gün eşitse veya baslangic ve bitis arasindaki günlerse SET(T)
            if (((currentDate.getMonthValue() == ramazanBasi.getMonthValue()) && (currentDate.getDayOfMonth() == ramazanBasi.getDayOfMonth())) || ((currentDate.getMonthValue() == ramazanBasi.plusDays(2).getMonthValue()) && (currentDate.getDayOfMonth() == ramazanBasi.plusDays(2).getDayOfMonth()) || (currentDate.isAfter(ramazanBasi) && currentDate.isBefore(ramazanBasi.plusDays(2))))) {
                takvim.setTatilTipi(TatilTipi.Ramazan);
                takvim.setResmiTatil(true);

            }
            if ((currentDate.getMonthValue() == kurbanBasi.getMonthValue()) && (currentDate.getDayOfMonth() == kurbanBasi.getDayOfMonth()) || (currentDate.getMonthValue() == kurbanBasi.plusDays(3).getMonthValue()) && (currentDate.getDayOfMonth() == kurbanBasi.plusDays(2).getDayOfMonth()) || (currentDate.isAfter(kurbanBasi) && currentDate.isBefore(kurbanBasi.plusDays(3)))) {
                takvim.setTatilTipi(TatilTipi.Kurban);
                takvim.setResmiTatil(true);
            }

            takvimList.add(takvim);
            currentDate = currentDate.plusDays(1);
        }
        for (Takvim takvim : takvimList) {
            randevuAjandaDAO.saveOrUpdate(takvim);
        }
        respObj.put("success", true);
        respObj.put("message", "takvim eklendi");
        return respObj;
    }

    @Override
    @Transactional
    public JSONObject createAjanda(String baslangicTarihi, String bitisTarihi,
                                   String baslangicSaati, String bitisSaati,
                                   String gunler, String muayeneSure, String bransId,
                                   String polikinlikId, String personelId,
                                   String kisitBasla, String kisitBitis,
                                   String resmiTatil, String kayitGuncelle) {

        JSONObject respObj = new JSONObject();
        Brans brans = bransDAO.getObjectById(Brans.class, Integer.parseInt(bransId));
        Polikinlik polikinlik = polikinlikDAO.getObjectById(Polikinlik.class, Integer.parseInt(polikinlikId));
        Personel personel = personelDAO.getObjectById(Personel.class, Integer.parseInt(personelId));

        List<Takvim> takvimList = randevuAjandaDAO.getObjectsByParam(Takvim.class, "tarih", utility.parseDate(baslangicTarihi));
        List<Takvim> takvimList1 = randevuAjandaDAO.getObjectsByParam(Takvim.class, "tarih", utility.parseDate(bitisTarihi));
        Takvim takvimBasla = takvimList.get(0);
        Takvim takvimBitis = takvimList1.get(0);
        int baslaId = takvimBasla.getTakvimId();
        int bitisId = takvimBitis.getTakvimId();
        Takvim ajandaTakvim = takvimBasla;

        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate baslangic = LocalDate.parse(baslangicTarihi, DATEFORMATTER);
        LocalDate bitisTime = LocalDate.parse(bitisTarihi, DATEFORMATTER);
        LocalDateTime basla = LocalDateTime.of(baslangic, LocalTime.MIDNIGHT);
        LocalDateTime bitis = LocalDateTime.of(bitisTime, LocalTime.MIDNIGHT);

        String[] molaBasla = kisitBasla.split(":");
        String[] molaBitis = kisitBitis.split(":");
        String[] baslaSaat = baslangicSaati.split(":");
        String[] bitisSaat = bitisSaati.split(":");

        LocalDateTime kisitBaslama = basla.plusHours(Long.parseLong(molaBasla[0])).plusMinutes(Long.parseLong(molaBasla[1]));
        LocalDateTime kisitBitirme = basla.plusHours(Long.parseLong(molaBitis[0])).plusMinutes(Long.parseLong(molaBitis[1]));
        LocalDateTime tempKisitBitirme = kisitBitirme;

        Long muayeneSuresi = Long.parseLong(muayeneSure);
        basla = basla.plusHours(Long.parseLong(baslaSaat[0])).plusMinutes(Long.parseLong(baslaSaat[1]));
        LocalDateTime tempBasla = basla;
        bitis = bitis.plusHours(Long.parseLong(bitisSaat[0])).plusMinutes(Long.parseLong(bitisSaat[1]));
        LocalDateTime muayeneBitis = basla;

        if (LocalDateTime.now().isAfter(tempBasla)) {
            respObj.put("success", false);
            respObj.put("message", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " tarihinden öncesini secemezsiniz.");
            return respObj;
        }

        if (tempBasla.isAfter(bitis)) {
            respObj.put("success", false);
            respObj.put("message", "GECERSIZ TARIH ARALIGI");
            return respObj;
        }

        List<RandevuAjanda> ajandaList = new ArrayList<>();
        String[] gunList = gunler.split(",");
        Boolean calisilacakGun = false;
        Boolean resmiTatilCalisacakMi = false;
        Boolean kayitVarmi = false;

        List<RandevuAjanda> tempRandevuAjandaList = randevuAjandaDAO.getRandevuAjandaByParam(basla, bitis, personel.getPersonelId());
        LocalDateTime finalBasla = basla;
        LocalDateTime finalBitis = bitis;
        if (utility.convertBoolean(kayitGuncelle, false)) {
            for (RandevuAjanda ajanda : tempRandevuAjandaList) {
                loggingService.logRandevuDegisiklik(ajanda.getHasta(), "UPDATE", "Randevu slotu pasife cevrildi", ajanda.getPersonel(), ajanda, ajanda.getBaslangicSaati(), ajanda.getBitisSaati());
                ajanda.setAktif(false);
                randevuAjandaDAO.saveOrUpdate(ajanda);

            }
            tempRandevuAjandaList.clear();
        }

        while (basla.getHour() != bitis.getHour() || basla.getDayOfMonth() != bitis.getDayOfMonth()) {
            for (String gun : gunList) {
                String tempAjandaGun = ajandaTakvim.getGunIsmi();
                int dayValue = Integer.parseInt(gun);
                DayOfWeek dayOfWeek = basla.getDayOfWeek();

                if (dayOfWeek == DayOfWeek.of(dayValue)) {
                    calisilacakGun = true;
                    break;
                } else {
                    calisilacakGun = false;
                }
            }

            if (!calisilacakGun) {
                basla = tempBasla.plusDays(1);
                tempBasla = basla;
                kisitBitirme = tempKisitBitirme.plusDays(1);
                tempKisitBitirme = kisitBitirme;
                if (baslaId != bitisId) {
                    baslaId++;
                    ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);
                } else {
                    basla = bitis;
                }
            } else if (basla.getHour() >= bitis.getHour()) {
                basla = tempBasla.plusDays(1);
                tempBasla = basla;
                kisitBitirme = tempKisitBitirme.plusDays(1);
                tempKisitBitirme = kisitBitirme;
                if (baslaId != bitisId) {
                    baslaId++;
                    ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);
                }
            } else if (basla.getHour() < kisitBaslama.getHour() || (basla.getHour() == kisitBaslama.getHour() && basla.getMinute() < kisitBaslama.getMinute())) {
                RandevuAjanda randevuAjanda = new RandevuAjanda();
                muayeneBitis = basla.plusMinutes(muayeneSuresi);
                if (muayeneBitis.getHour() == kisitBaslama.getHour() && muayeneBitis.getMinute() > kisitBaslama.getMinute()) {
                    basla = kisitBitirme;
                    continue;
                }
                randevuAjanda.setMuayeneSure(Integer.parseInt(muayeneSure));
                randevuAjanda.setBaslangicSaati(basla);
                randevuAjanda.setBitisSaati(muayeneBitis);
                randevuAjanda.setBrans(brans);
                randevuAjanda.setPolikinlik(polikinlik);
                randevuAjanda.setPersonel(personel);
                randevuAjanda.setTakvim(ajandaTakvim);
                randevuAjanda.setRandevuDurum(RandevuDurum.Bos);
                randevuAjanda.setAktif(true);

                if (tempRandevuAjandaList.stream().filter(x -> x.getBaslangicSaati().isAfter(finalBasla) && x.getBitisSaati().isBefore(finalBitis)).count() > 0) {
                    basla = tempBasla.plusDays(1);
                    tempBasla = basla;
                    if (baslaId != bitisId) {
                        baslaId++;
                        ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);
                    } else {
                        basla = bitis;
                    }
                    continue;
                }

                Boolean icindeVar = false;
                basla = basla.plusMinutes(muayeneSuresi);

                if (ajandaTakvim.getResmiTatil()) {
                    resmiTatilCalisacakMi = utility.resmiTatilCalisma(resmiTatil);
                    if (!resmiTatilCalisacakMi) {
                        basla = tempBasla.plusDays(1);
                        tempBasla = basla;
                        if (baslaId != bitisId) {
                            baslaId++;
                            ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);

                        } else {
                            basla = bitis;
                        }
                    } else {
                        kayitVarmi = true;
                        randevuAjandaDAO.saveOrUpdate(randevuAjanda);
                        loggingService.logRandevuDegisiklik(randevuAjanda.getHasta(), "CREATE", "Randevu slotu eklendi", randevuAjanda.getPersonel(), randevuAjanda, randevuAjanda.getBaslangicSaati(), randevuAjanda.getBitisSaati());
                    }
                } else {
                    kayitVarmi = true;
                    randevuAjandaDAO.saveOrUpdate(randevuAjanda);
                    loggingService.logRandevuDegisiklik(randevuAjanda.getHasta(), "CREATE", "Randevu slotu eklendi", randevuAjanda.getPersonel(), randevuAjanda, randevuAjanda.getBaslangicSaati(), randevuAjanda.getBitisSaati());
                }

            } else if (basla.getHour() == kisitBitirme.getHour() && basla.getMinute() >= kisitBitirme.getMinute()) {
                RandevuAjanda randevuAjanda = new RandevuAjanda();
                if (basla.getHour() < bitis.getHour()) {
                    basla = kisitBitirme;
                    kisitBitirme = kisitBitirme.plusMinutes(muayeneSuresi);
                }

                muayeneBitis = basla.plusMinutes(muayeneSuresi);
                if (muayeneBitis.getHour() == bitis.getHour() && muayeneBitis.getMinute() > bitis.getMinute()) {
                    continue;
                }
                randevuAjanda.setMuayeneSure(Integer.parseInt(muayeneSure));
                randevuAjanda.setBaslangicSaati(basla);
                randevuAjanda.setBitisSaati(muayeneBitis);
                randevuAjanda.setBrans(brans);
                randevuAjanda.setPolikinlik(polikinlik);
                randevuAjanda.setPersonel(personel);
                randevuAjanda.setTakvim(ajandaTakvim);
                randevuAjanda.setRandevuDurum(RandevuDurum.Bos);
                randevuAjanda.setAktif(true);

                if (tempRandevuAjandaList.stream().filter(x -> x.getBaslangicSaati().isAfter(finalBasla) && x.getBitisSaati().isBefore(finalBitis)).count() > 0) {
                    basla = tempBasla.plusDays(1);
                    tempBasla = basla;
                    if (baslaId != bitisId) {
                        baslaId++;
                        ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);
                    } else {
                        basla = bitis;
                    }
                    continue;
                }
                basla = basla.plusMinutes(muayeneSuresi);
                if (ajandaTakvim.getResmiTatil()) {
                    resmiTatilCalisacakMi = utility.resmiTatilCalisma(resmiTatil);
                    if (!resmiTatilCalisacakMi) {
                        basla = tempBasla.plusDays(1);
                        tempBasla = basla;
                        if (baslaId != bitisId) {
                            baslaId++;
                            ajandaTakvim = randevuAjandaDAO.getObjectById(Takvim.class, baslaId);
                        } else {
                            basla = bitis;
                        }
                    } else {
                        kayitVarmi = true;
                        randevuAjandaDAO.saveOrUpdate(randevuAjanda);
                        loggingService.logRandevuDegisiklik(randevuAjanda.getHasta(), "CREATE", "Randevu slotu eklendi", randevuAjanda.getPersonel(), randevuAjanda, randevuAjanda.getBaslangicSaati(), randevuAjanda.getBitisSaati());
                    }
                } else {
                    kayitVarmi = true;
                    randevuAjandaDAO.saveOrUpdate(randevuAjanda);
                    loggingService.logRandevuDegisiklik(randevuAjanda.getHasta(), "CREATE", "Randevu slotu eklendi", randevuAjanda.getPersonel(), randevuAjanda, randevuAjanda.getBaslangicSaati(), randevuAjanda.getBitisSaati());
                }

            } else {
                basla = basla.plusMinutes(muayeneSuresi);
            }

        }
        if (kayitVarmi) {
            respObj.put("success", true);
            respObj.put("message", "Ajanda olusturuldu");
        } else {
            respObj.put("success", false);
            respObj.put("message", baslangic + " tarihinden " + bitisTime + " tarihine kadar randevu slotu mevcut.Önceki slotları silmek için 'silmek istiyorum' kutucugunu isaretleyin. ");

        }


        return respObj;
    }


    @Override
    public JSONArray getBosRandevuSlotlar(String personelId, String bransId, String polikinlikId, String basTarihi, String bitTarihi) {
        JSONArray respList = randevuAjandaDAO.getBosRandevuSlotlar(personelId, bransId, polikinlikId, basTarihi, bitTarihi);
        return respList;
    }

    @Override
    @Transactional
    public JSONObject deleteRandevuById(String id) {
        JSONObject respObj = new JSONObject();
        RandevuAjanda randevu = randevuAjandaDAO.getObjectById(RandevuAjanda.class, utility.convertInteger(id, 0));

        if (randevu.getRandevuDurum() == RandevuDurum.Bos) {
            randevu.setAktif(false);
            loggingService.logRandevuDegisiklik(randevu.getHasta(), "UPDATE", "Randevu slotu pasife cevrildi", randevu.getPersonel(), randevu, randevu.getBaslangicSaati(), randevu.getBitisSaati());
            respObj.put("success", true);
            respObj.put("message", randevu.getBaslangicSaati() + " tarihinde baslayan ve " + randevu.getBitisSaati() + " tarihinde biten randevu slotu basariyla silinmistir.");
        } else {
            respObj.put("success", false);
            respObj.put("message", "Bos olmayan randevulari silemezsiniz.");
        }
        return respObj;

    }

    @Override
    @Transactional
    public JSONObject deleteRandevuByTarih(String baslangicTarihi, String bitisTarihi, String personelId) {
        JSONObject respObj = new JSONObject();
        if (baslangicTarihi == null || baslangicTarihi == "") {
            respObj.put("success", false);
            respObj.put("message", "BASLANGIC TARIHI BOS OLAMAZ");
            return respObj;
        }
        if (bitisTarihi == null || bitisTarihi == "") {
            respObj.put("success", false);
            respObj.put("message", "BITIS TARIHI BOS OLAMAZ");
            return respObj;
        }
        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date baslangic = Date.valueOf(baslangicTarihi);
        Date bitisTime = Date.valueOf(bitisTarihi);
        List<RandevuAjanda> randevuAjandaList = randevuAjandaDAO.getRandevuByTarih(baslangic, bitisTime, utility.convertInteger(personelId, 0));
        if (randevuAjandaList != null && randevuAjandaList.size() > 0) {
            for (RandevuAjanda ajanda : randevuAjandaList) {
                if (ajanda.getRandevuDurum() == RandevuDurum.Bos) {
                    ajanda.setAktif(false);
                    loggingService.logRandevuDegisiklik(ajanda.getHasta(), "UPDATE", "Randevu slotu pasife cevrildi", ajanda.getPersonel(), ajanda, ajanda.getBaslangicSaati(), ajanda.getBitisSaati());

                }

            }
            respObj.put("success", true);
            respObj.put("message", "Belirtilen tarihler arasindaki randevular silindi.");
        } else {
            respObj.put("success", false);
            respObj.put("message", "Belirtilen tarihlerde randevu bulunamadi");
        }
        return respObj;
    }

    @Override
    @Transactional
    public JSONObject createRandevu(String hastaId, String randevuId, String hastaNotu) {
        JSONObject respObj = new JSONObject();

        Hasta hasta = hastaDAO.getObjectById(Hasta.class, utility.convertInteger(hastaId, 0));
        RandevuAjanda randevu = randevuAjandaDAO.getObjectById(RandevuAjanda.class, utility.convertInteger(randevuId, 0));
        List<RandevuAjanda> randevuAjandas = randevuAjandaDAO.getRandevuByHastaAndPersonel(hasta.getHastaId(), randevu.getPersonel().getPersonelId());
        List<RandevuAjanda> randevuAjandas2 = randevuAjandaDAO.getRandevuByHastaAndBrans(hasta.getHastaId(), randevu.getBrans().getBransId());
        if (randevuAjandas.size() > 0) {
            respObj.put("success", false);
            respObj.put("message", "Bu doktordan aktif randevunuz bulunmakta.Tekrar randevu alamazsiniz.");
            return respObj;
        }
        if (randevuAjandas2.size() > 0) {
            respObj.put("success", false);
            respObj.put("message", "Bu branstan aktif randevunuz mevcut.Farkli doktordan randevu alamazsiniz.");
            return respObj;
        }
        randevu.setHasta(hasta);
        randevu.setRandevuDurum(RandevuDurum.Dolu);
        if (hastaNotu != null || hastaNotu != "") {
            randevu.setHastaNot(hastaNotu);
        }
        randevuAjandaDAO.saveOrUpdate(randevu);
        loggingService.logRandevuDegisiklik(randevu.getHasta(), "UPDATE", "Hastaya randevu verildi", randevu.getPersonel(), randevu, randevu.getBaslangicSaati(), randevu.getBitisSaati());
        respObj.put("success", true);
        respObj.put("message", "Randevu basariyla alindi.");
        return respObj;

    }

    @Override
    @Transactional
    public JSONObject randevuIptal(String randevuId) {
        RandevuAjanda randevu = randevuAjandaDAO.getObjectById(RandevuAjanda.class, utility.convertInteger(randevuId, 0));
        JSONObject respObj = new JSONObject();
        if (LocalDateTime.now().isBefore(randevu.getBaslangicSaati().minusHours(24))) {
            loggingService.logRandevuDegisiklik(randevu.getHasta(), "UPDATE", "Randevu iptal edildi", randevu.getPersonel(), randevu, randevu.getBaslangicSaati(), randevu.getBitisSaati());
            randevu.setHastaNot(null);
            randevu.setHasta(null);
            randevu.setRandevuDurum(RandevuDurum.Bos);
            randevuAjandaDAO.saveOrUpdate(randevu);
            respObj.put("success", true);
            respObj.put("message", "Randevu iptal edildi.");
            return respObj;
        } else {
            respObj.put("success", false);
            respObj.put("message", "Randevu, randevu saatinden 24 saat öncesine kadar iptal edilebilir. Sonrasinda iptal edilemez. ");
            return respObj;
        }

    }

    @Override
    public JSONArray getAktifRandevularByHasta(String hastaId, String baslangicTarihi, String bitisTarihi) {
        JSONArray response = randevuAjandaDAO.getAktifRandevularByHasta(utility.convertInteger(hastaId, 0), baslangicTarihi, bitisTarihi);
        return response;

    }

    @Override
    public JSONArray getDoluRandevularByDoktorId(String personelId) {
        JSONArray resp = randevuAjandaDAO.getDoluRandevularByDoktor(utility.convertInteger(personelId, 0));
        return resp;
    }

    @Override
    @Transactional
    public JSONObject addRezervToRandevu(String slotId, String rezervNot) {
        JSONObject respObj = new JSONObject();
        RandevuAjanda randevuSlot = randevuAjandaDAO.getObjectById(RandevuAjanda.class, utility.convertInteger(slotId, 0));
        if (rezervNot == null || rezervNot.equals("")) {
            respObj.put("success", false);
            respObj.put("message", "Rezerve edeceginiz randevular icin not yazmalisiniz.");
            return respObj;
        }
        randevuSlot.setRandevuDurum(RandevuDurum.rezerv);
        randevuSlot.setDoktorRezervNot(rezervNot);
        loggingService.logRandevuDegisiklik(randevuSlot.getHasta(), "UPDATE", "Slot rezerv edildi.", randevuSlot.getPersonel(), randevuSlot, randevuSlot.getBaslangicSaati(), randevuSlot.getBitisSaati());

        randevuAjandaDAO.saveOrUpdate(randevuSlot);
        System.out.println(randevuSlot);
        respObj.put("success", true);
        respObj.put("message", "Randevu slotu basariyla rezerv edildi.");
        return respObj;
    }

    @Override
    @Transactional
    public JSONObject addHastaToRezervSlot(String hastaId, String slotId) {
        Hasta hasta = hastaDAO.getObjectById(Hasta.class, utility.convertInteger(hastaId, 0));
        RandevuAjanda randevuSlot = randevuAjandaDAO.getObjectById(RandevuAjanda.class, utility.convertInteger(slotId, 0));
        randevuSlot.setHasta(hasta);
        randevuSlot.setRandevuDurum(RandevuDurum.Dolu);
        randevuAjandaDAO.saveOrUpdate(randevuSlot);
        loggingService.logRandevuDegisiklik(randevuSlot.getHasta(), "UPDATE", "Rezervlenen randevu onaylandi.", randevuSlot.getPersonel(), randevuSlot, randevuSlot.getBaslangicSaati(), randevuSlot.getBitisSaati());
        JSONObject respObj = new JSONObject();
        respObj.put("success", true);
        respObj.put("message", "REZERVE OLAN SLOTA HASTA BASARIYLA ATANDI");
        return respObj;
    }


    @Transactional
    @Scheduled(cron = "0 0 17 * * *")
    public void rezervTakip() {
        List<RandevuAjanda> rezervRandevular = randevuAjandaDAO.getRezervRandevular();
        for (RandevuAjanda rezerv : rezervRandevular) {
            if (rezerv.getHastaVizit() == null && LocalDateTime.now().plusHours(24).isAfter(rezerv.getBaslangicSaati())) {
                loggingService.logRandevuDegisiklik(null, "UPDATE", "Rezervlenen randevu, randevu alinabilir hale geldi", rezerv.getPersonel(), rezerv, rezerv.getBaslangicSaati(), rezerv.getBitisSaati());
                rezerv.setRandevuDurum(RandevuDurum.Bos);
                rezerv.setDoktorRezervNot(null);
                randevuAjandaDAO.saveOrUpdate(rezerv);
                System.out.println(rezerv);
            }

        }
    }


}
