package com.example.dailyexpenseapp;

public class Expense {
    private String description;
    private double amount;
    private String category;
    private String paymentMode;

    public Expense(String description, double amount, String category, String paymentMode) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.paymentMode = paymentMode;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getPaymentMode() { return paymentMode; }

    @Override
    public String toString() {
        return description + " - $" + amount;
    }
}
