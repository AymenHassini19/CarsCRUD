package com.example;

public class Employee extends Person {

    String password;
    String department;
    double commissionRate;

    public Employee(String fname, String lname, String email, String phoneNumber, String password, String department, double commissionRate) {
        super(fname, lname, email, phoneNumber);
        this.password = password;
        this.department = department;
        this.commissionRate = commissionRate;
    }

    public Employee(String id, String fname, String lname, String email, String phoneNumber, String password, String department, double commissionRate) {
        super(id, fname, lname, email, phoneNumber);
        this.password = password;
        this.department = department;
        this.commissionRate = commissionRate;
    }

    public String getPassword() { return password; } 
    public void setPassword(String password) { this.password = password; } 
    
    public String getDepartment() { return department; } 
    public void setDepartment(String department) { this.department = department; } 
    
    public double getCommissionRate() { return commissionRate; } 
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

    @Override
    public String toString() {
        return super.toString();
    }
    
}
