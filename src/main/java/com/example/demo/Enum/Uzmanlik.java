package com.example.demo.Enum;
public enum Uzmanlik {
    PratisyenDr("PratisyenDr"),
    UzmanDr("UzmanDr"),
    OperatorDr("OperatorDr"),
    YardımciDocentDr("YardımciDocentDr"),
    DocentDr("DocentDr"),
    ProfDr("ProfDr");
    Uzmanlik(String label){
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
