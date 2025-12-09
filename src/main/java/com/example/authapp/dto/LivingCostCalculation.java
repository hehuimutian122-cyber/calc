package com.example.authapp.dto;

public class LivingCostCalculation {
    private String postalCode;
    private String prefecture;
    private int annualIncome;
    private int monthlyRent;
    private int monthlyUtilities;
    private int monthlyFood;
    private int monthlyCommunication;
    private int monthlyOthers;
    
    public LivingCostCalculation() {
    }
    
    public LivingCostCalculation(String postalCode, String prefecture, int annualIncome,
            int monthlyRent, int monthlyUtilities, int monthlyFood, 
            int monthlyCommunication, int monthlyOthers) {
        this.postalCode = postalCode;
        this.prefecture = prefecture;
        this.annualIncome = annualIncome;
        this.monthlyRent = monthlyRent;
        this.monthlyUtilities = monthlyUtilities;
        this.monthlyFood = monthlyFood;
        this.monthlyCommunication = monthlyCommunication;
        this.monthlyOthers = monthlyOthers;
    }
    
    // Getters と Setters
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPrefecture() {
        return prefecture;
    }
    
    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }
    
    public int getAnnualIncome() {
        return annualIncome;
    }
    
    public void setAnnualIncome(int annualIncome) {
        this.annualIncome = annualIncome;
    }
    
    public int getMonthlyRent() {
        return monthlyRent;
    }
    
    public void setMonthlyRent(int monthlyRent) {
        this.monthlyRent = monthlyRent;
    }
    
    public int getMonthlyUtilities() {
        return monthlyUtilities;
    }
    
    public void setMonthlyUtilities(int monthlyUtilities) {
        this.monthlyUtilities = monthlyUtilities;
    }
    
    public int getMonthlyFood() {
        return monthlyFood;
    }
    
    public void setMonthlyFood(int monthlyFood) {
        this.monthlyFood = monthlyFood;
    }
    
    public int getMonthlyCommunication() {
        return monthlyCommunication;
    }
    
    public void setMonthlyCommunication(int monthlyCommunication) {
        this.monthlyCommunication = monthlyCommunication;
    }
    
    public int getMonthlyOthers() {
        return monthlyOthers;
    }
    
    public void setMonthlyOthers(int monthlyOthers) {
        this.monthlyOthers = monthlyOthers;
    }
    
    public int getMonthlyTotal() {
        return monthlyRent + monthlyUtilities + monthlyFood + monthlyCommunication + monthlyOthers;
    }
    
    public int getAnnualTotal() {
        return getMonthlyTotal() * 12;
    }
}

