package com.example;

public class Car {
    String id;
    String brand;
    String model;
    String color;
    int year;
    double price;
    boolean availibility;

    public Car(String brand, String model, String color, int year, double price, boolean availibility){
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.price = price;
        this.availibility = availibility;
    }

    public Car(String id , String brand, String model, String color, int year, double price, boolean availibility){
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.price = price;
        this.availibility = availibility;
    }

    public String getId() { return id; } 
    public void setId(String id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean getAvailibility() {return availibility;}
    public void setAvailibility(boolean availibility) {this.availibility = availibility;}

    public String getFullSpecs(){ return brand+" "+model+" "+color+" "+year;}

    @Override
    public String toString() {
    return getFullSpecs(); 
}

}
