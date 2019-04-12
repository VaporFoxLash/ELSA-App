package com.example.elsa;

class UserInformation {
    public String title;
    public String surname;
    public String name;
    public String address;
    public String telephone;
    public String IDnumber;
    public String mobileNumber;


    public UserInformation(String title, String surname, String name, String IDNumber,
                           String mobileNumber, String telephone, String address) {
        this.title = title;
        this.surname = surname;
        this.name = name;
        this.address = address;
        this.IDnumber = IDNumber;
        this.telephone = telephone;
        this.mobileNumber = mobileNumber;
    }

    public UserInformation(){

    }
}
