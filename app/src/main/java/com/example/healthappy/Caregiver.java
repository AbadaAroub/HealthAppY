package com.example.healthappy;

public class Caregiver {
    private String User_UID;
    private String Name;
    private String Mobile_nr;
    private String email;
    private Elderly elderly;

    public Caregiver() {
    }

    public String getUser_UID() {
        return User_UID;
    }
    public void setUser_UID(String User_UID) {
        this.User_UID = User_UID;
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
    public Elderly getElderly() {
        return elderly;
    }
    public void setElderly(Elderly elderly) {
        this.elderly = elderly;
    }
}