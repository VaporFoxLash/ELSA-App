package com.example.elsa;

class UserInformation {
    public String title;
    public String surname;
    public String name;
    public String address;
    public String telephone;
    public String idnumber;
    public String mobileNumber;
    public String region;

    public UserInformation(String title, String surname, String name, String idnumber,
                           String mobileNumber, String telephone, String address, String region) {
        this.title = title;
        this.surname = surname;
        this.name = name;
        this.address = address;
        this.idnumber = idnumber;
        this.telephone = telephone;
        this.mobileNumber = mobileNumber;
        this.region = region;
    }

    public UserInformation(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getidnumber() {
        return idnumber;
    }

    public void setidnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
