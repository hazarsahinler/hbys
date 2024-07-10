package com.example.demo.Enum;

public enum RandevuDurum {
    Bos(0),
    Dolu(1),
    Kapali(2),
    rezerv(3);

    private int durum;

    private RandevuDurum(int durum) {
        this.durum = durum;
    }

    public int getDurum() {
        return durum;
    }

    public static RandevuDurum fromDurum(int durum) {
        for (RandevuDurum randevuDurum : values()) {
            if (randevuDurum.getDurum() == durum) {
                return randevuDurum;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + durum);
    }
}
