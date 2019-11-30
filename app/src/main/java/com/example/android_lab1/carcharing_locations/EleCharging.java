package com.example.android_lab1.carcharing_locations;

public class EleCharging {
    private String id;
    private String localTitle;
    private String addr;

    private String dLatitude;
    private String dLongitude;
    private String phoneNumber;


    public EleCharging(String localTitle, String addr, String dLatitude, String dLongitude, String phoneNumber) {

        this.localTitle = localTitle;
        this.addr = addr;
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;
        this.phoneNumber = phoneNumber;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(String dLatitude) {
        this.dLatitude = dLatitude;
    }

    public String getdLongitude() {
        return dLongitude;
    }

    public void setdLongitude(String dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getLocalTitle() {
        return localTitle;
    }

    public void setLocalTitle(String localTitle) {
        this.localTitle = localTitle;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
