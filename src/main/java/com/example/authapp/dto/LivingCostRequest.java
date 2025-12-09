package com.example.authapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LivingCostRequest {
    
    @NotBlank(message = "郵便番号は必須です")
    @Pattern(regexp = "^\\d{7}$", message = "郵便番号は7桁の数字で入力してください") //7桁の数字のみ許容
    private String postalCode;
    
    @NotNull(message = "年収は必須です")
    @Min(value = 1, message = "年収は1円以上で入力してください")
    @Max(value = 100000000, message = "年収は1億円以下で入力してください")
    private Integer annualIncome;
    
    public LivingCostRequest() {
    }
    
    public LivingCostRequest(String postalCode, Integer annualIncome) {
        this.postalCode = postalCode;
        this.annualIncome = annualIncome;
    }
    
    // Getters and Setters
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public Integer getAnnualIncome() {
        return annualIncome;
    }
    
    public void setAnnualIncome(Integer annualIncome) {
        this.annualIncome = annualIncome;
    }
}

