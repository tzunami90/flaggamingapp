package com.beone.flagggaming.tiendas;

public class Tienda {
    private String name;
    private String mail;
    private String dir;
    private String days;
    private String hr;
    private String insta;
    private String tel;

    public Tienda(String name, String mail, String dir, String days, String hr, String insta, String tel) {
        this.name = name;
        this.mail = mail;
        this.dir = dir;
        this.days = days;
        this.hr = hr;
        this.insta = insta;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getDir() {
        return dir;
    }

    public String getDays() {
        return days;
    }

    public String getHr() {
        return hr;
    }

    public String getInsta() {
        return insta;
    }

    public String getTel() {
        return tel;
    }
}
