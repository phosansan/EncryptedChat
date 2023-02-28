package com.example.encryptedchat.model;

public class Pesan {
    private String isiPesan, pengirim, penerima, tanggal;

    public Pesan() {
    }

    public Pesan(String isiPesan, String pengirim, String penerima, String tanggal) {
        this.isiPesan = isiPesan;
        this.pengirim = pengirim;
        this.penerima = penerima;
        this.tanggal = tanggal;
    }

    public String getIsiPesan() {
        return isiPesan;
    }

    public void setIsiPesan(String isiPesan) {
        this.isiPesan = isiPesan;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
