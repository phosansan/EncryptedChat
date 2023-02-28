package com.example.encryptedchat.model;

public class Tab {
    private String ID, noTelp, nama, keterangan, tanggal;

    public Tab() {
    }

    public Tab(String ID, String noTelp, String nama, String keterangan, String tanggal) {
        this.ID = ID;
        this.noTelp = noTelp;
        this.nama = nama;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
