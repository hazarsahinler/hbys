package com.example.demo.Enum;

public enum TatilTipi {
    YilBasi(0),
    EgemenlikVeCocuk(1),
    Isci(2),
    GenclikVeSpor(3),
    OnBesTemmuz(4),
    Zafer(5),
    Cumhuriyet(6),
    Kurban(7),
    Ramazan(8);



    private int tip;

    private TatilTipi(int tip) {
        this.tip = tip;
    }

    public int getTip() {
        return tip;
    }

    public static TatilTipi fromTip(int tip) {
        for (TatilTipi tatilTipi : values()) {
            if (tatilTipi.getTip() == tip) {
                return tatilTipi;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + tip);
    }
}
