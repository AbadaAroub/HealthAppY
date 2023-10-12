package com.example.healthappy;

public class Caregiver {
    private String Name;
    private String Mobile_nr;
    private String email;
    //private Elderly elderly;

    public Caregiver() {
    }
    public Caregiver(String name, String mobile_nr, String email){
        this.Name = name;
        this.Mobile_nr = mobile_nr;
        this.email = email;
    }

    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getMobile_nr() {
        return Mobile_nr;
    }
    public void setMobile_nr(String Mobile_nr) {
        this.Mobile_nr = Mobile_nr;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    /*public Elderly getElderly() {
        return elderly;
    }
    public void setElderly(Elderly elderly) {
        this.elderly = elderly;
    }*/
}