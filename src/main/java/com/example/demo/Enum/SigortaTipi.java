package com.example.demo.Enum;

public enum SigortaTipi {

    SGK("SGK"),
    OSS("OSS"),
    TSS("TSS"),
    UCRETLI("UCRETLI");
    SigortaTipi(String label){
        this.label= label;
    }
    private String label;
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
