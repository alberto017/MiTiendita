package com.example.mitiendita.Model;

public class UsuarioModel {

    private String Name;
    private String Phone;
    private String Password;
    private String SliderStatus;
    private String IsStaff;
    private String SecureCode;
    private String Location;
    private String UserType;
    private String Reference;

    public UsuarioModel() {
    }//Constructor_1

    public UsuarioModel(String location) {
        Location = location;
    }//Constructor_2

    public UsuarioModel(String name, String phone, String password, String sliderStatus) {
        Name = name;
        Phone = phone;
        Password = password;
        SliderStatus = sliderStatus;
        IsStaff = "false";
    }//Constructor_3

    public UsuarioModel(String name, String phone, String password, String sliderStatus, String secureCode) {
        Name = name;
        Phone = phone;
        Password = password;
        SliderStatus = sliderStatus;
        IsStaff = "false";
        SecureCode = secureCode;
    }//Constructor_4

    public UsuarioModel(String name, String phone, String password, String sliderStatus, String secureCode, String location) {
        Name = name;
        Phone = phone;
        Password = password;
        SliderStatus = sliderStatus;
        IsStaff = "false";
        SecureCode = secureCode;
        Location = location;
    }//Constructor_5

    public UsuarioModel(String name, String phone, String password, String sliderStatus, String secureCode, String location, String userType) {
        Name = name;
        Phone = phone;
        Password = password;
        SliderStatus = sliderStatus;
        IsStaff = "false";
        SecureCode = secureCode;
        Location = location;
        UserType = userType;
    }//Constructor_5

    public UsuarioModel(String name, String phone, String password, String sliderStatus, String secureCode, String location, String userType, String reference) {
        Name = name;
        Phone = phone;
        Password = password;
        SliderStatus = sliderStatus;
        IsStaff = "false";
        SecureCode = secureCode;
        Location = location;
        UserType = userType;
        Reference = reference;
    }//Constructor_6

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSliderStatus() {
        return SliderStatus;
    }

    public void setSliderStatus(String sliderStatus) {
        SliderStatus = sliderStatus;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getSecureCode() {
        return SecureCode;
    }

    public void setSecureCode(String secureCode) {
        SecureCode = secureCode;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }
}//UsuarioModel
