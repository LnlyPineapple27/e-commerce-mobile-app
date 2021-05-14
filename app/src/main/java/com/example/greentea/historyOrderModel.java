package com.example.greentea;

public class historyOrderModel {
    private String nameBuyer, totalAmount;

    public historyOrderModel(){

    }

    public historyOrderModel(String nameBuyer, String totalAmount) {
        this.nameBuyer = nameBuyer;
        this.totalAmount = totalAmount;
    }

    public String getNameBuyer() {
        return nameBuyer;
    }

    public void setNameBuyer(String nameBuyer) {
        this.nameBuyer = nameBuyer;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}