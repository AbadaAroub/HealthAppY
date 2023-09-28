package com.example.healthappy;

public class Elderly {
    private String Name;
    private String Mobile_nr;
    private String Address;
    private Allergies Allergy;
    private Caregiver caregivers;
    private enum Allergies {
        NUTS,
        GLUTEN,
        LACTOS;
    }

    public Elderly() {

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
    public void setMobile_nr(String mobile_nr) {
        Mobile_nr = mobile_nr;
    }

    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }

    public Allergies getAllergy() {
        return Allergy;
    }
    public void setAllergy(Allergies allergy) {
        Allergy = allergy;
    }

    public Caregiver getCaregivers() {
        return caregivers;
    }
    public void setCaregivers(Caregiver caregivers) {
        this.caregivers = caregivers;
    }
}
