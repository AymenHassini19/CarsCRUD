package com.example;

public class Client extends Person {

    String address;
    String idCard;

    // Constructor without id
    public Client(String fname, String lname, String email, String phoneNumber, String address, String idCard) {
        super(fname, lname, email, phoneNumber);
        this.address = address;
        this.idCard = idCard;
    }

    // Constructor with id
    public Client(String id, String fname, String lname, String email, String phoneNumber, String address, String idCard) {
        super(id, fname, lname, email, phoneNumber);
        this.address = address;
        this.idCard = idCard;
    }

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getIdCard() {return idCard;}
    public void setIdCard(String idCard) {this.idCard = idCard;}
}
