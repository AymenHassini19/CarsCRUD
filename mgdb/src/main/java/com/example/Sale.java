package com.example;

public class Sale {
    String id;
    String carId;
    String clientId;
    String salespersonId;
    Double fullPrice;
    Double initialDeposit;
    double interestRate;
    double monthlyPayment;
    int leaseDuration;
    int monthsRemaining;
    boolean FullyPaid;


    public Sale(String carId, String clientId, String salespersonId, Double fullPrice,
                Double initialDeposit, double interestRate, double monthlyPayment,
                int leaseDuration, int monthsRemaining, boolean isFullyPaid) {
        this.carId = carId;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.fullPrice = fullPrice;
        this.initialDeposit = initialDeposit;
        this.interestRate = interestRate;
        this.monthlyPayment = monthlyPayment;
        this.leaseDuration = leaseDuration;
        this.monthsRemaining = monthsRemaining;
        this.FullyPaid = FullyPaid;
    }


    public Sale(String id, String carId, String clientId, String salespersonId, Double fullPrice,
                Double initialDeposit, double interestRate, double monthlyPayment,
                int leaseDuration, int monthsRemaining, boolean isFullyPaid) {
        this.id = id;
        this.carId = carId;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.fullPrice = fullPrice;
        this.initialDeposit = initialDeposit;
        this.interestRate = interestRate;
        this.monthlyPayment = monthlyPayment;
        this.leaseDuration = leaseDuration;
        this.monthsRemaining = monthsRemaining;
        this.FullyPaid = FullyPaid;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getSalespersonId() { return salespersonId; }
    public void setSalespersonId(String salespersonId) { this.salespersonId = salespersonId; }

    public Double getFullPrice() { return fullPrice; }
    public void setFullPrice(Double fullPrice) { this.fullPrice = fullPrice; }

    public Double getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(Double initialDeposit) { this.initialDeposit = initialDeposit; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public int getLeaseDuration() { return leaseDuration; }
    public void setLeaseDuration(int leaseDuration) { this.leaseDuration = leaseDuration; }

    public int getMonthsRemaining() { return monthsRemaining; }
    public void setMonthsRemaining(int monthsRemaining) { this.monthsRemaining = monthsRemaining; }

    public boolean isFullyPaid() { return FullyPaid; }
    public void setFullyPaid(boolean FullyPaid) { this.FullyPaid = FullyPaid; }


}
