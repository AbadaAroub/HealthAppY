package com.example.healthappy;

public class Elderly {
    private String User_ID;
    private String Name;
    private String Mobile_nr;
    private String Address;
    private String email;
    private Caregiver caregivers;
    private String Allergies;

    public Elderly() {

    }

    public String getUser_ID() {
        return User_ID;
    }
    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public String getMobile_nr() {
        return Mobile_nr;
    }
    public void setMobile_nr(String mobile_nr) {
        Mobile_nr = mobile_nr;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public Caregiver getCaregivers() {
        return caregivers;
    }
    public void setCaregivers(Caregiver caregivers) {
        this.caregivers = caregivers;
    }
    public String getAllergies() {
        return Allergies;
    }
    public void setAllergies(String allergies) {
        Allergies = allergies;
    }
}
