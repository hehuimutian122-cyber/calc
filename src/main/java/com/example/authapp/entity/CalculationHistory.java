package com.example.authapp.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_histories")
public class CalculationHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ユーザーとの関連付け（多対一の関係）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // 入力情報
    @Column(name = "postal_code", nullable = false, length = 7)
    private String postalCode;
    
    @Column(name = "prefecture_name")
    private String prefectureName;
    
    @Column(name = "annual_income", nullable = false)
    private Integer annualIncome;
    
    // 計算結果（月額）
    @Column(name = "monthly_rent")
    private Integer monthlyRent;
    
    @Column(name = "monthly_utilities")
    private Integer monthlyUtilities;
    
    @Column(name = "monthly_food")
    private Integer monthlyFood;
    
    @Column(name = "monthly_communication")
    private Integer monthlyCommunication;
    
    @Column(name = "monthly_others")
    private Integer monthlyOthers;
    
    @Column(name = "monthly_total")
    private Integer monthlyTotal;
    
    @Column(name = "annual_total")
    private Integer annualTotal;
    
    // 作成日時
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // コンストラクタ
    public CalculationHistory() {
    }
    
    public CalculationHistory(User user, String postalCode, String prefectureName, 
                             Integer annualIncome, Integer monthlyRent, 
                             Integer monthlyUtilities, Integer monthlyFood,
                             Integer monthlyCommunication, Integer monthlyOthers,
                             Integer monthlyTotal, Integer annualTotal) {
        this.user = user;
        this.postalCode = postalCode;
        this.prefectureName = prefectureName;
        this.annualIncome = annualIncome;
        this.monthlyRent = monthlyRent;
        this.monthlyUtilities = monthlyUtilities;
        this.monthlyFood = monthlyFood;
        this.monthlyCommunication = monthlyCommunication;
        this.monthlyOthers = monthlyOthers;
        this.monthlyTotal = monthlyTotal;
        this.annualTotal = annualTotal;
        this.createdAt = LocalDateTime.now();
    }
    
    // 作成日時を自動設定（エンティティ保存前に実行）
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPrefectureName() {
        return prefectureName;
    }
    
    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }
    
    public Integer getAnnualIncome() {
        return annualIncome;
    }
    
    public void setAnnualIncome(Integer annualIncome) {
        this.annualIncome = annualIncome;
    }
    
    public Integer getMonthlyRent() {
        return monthlyRent;
    }
    
    public void setMonthlyRent(Integer monthlyRent) {
        this.monthlyRent = monthlyRent;
    }
    
    public Integer getMonthlyUtilities() {
        return monthlyUtilities;
    }
    
    public void setMonthlyUtilities(Integer monthlyUtilities) {
        this.monthlyUtilities = monthlyUtilities;
    }
    
    public Integer getMonthlyFood() {
        return monthlyFood;
    }
    
    public void setMonthlyFood(Integer monthlyFood) {
        this.monthlyFood = monthlyFood;
    }
    
    public Integer getMonthlyCommunication() {
        return monthlyCommunication;
    }
    
    public void setMonthlyCommunication(Integer monthlyCommunication) {
        this.monthlyCommunication = monthlyCommunication;
    }
    
    public Integer getMonthlyOthers() {
        return monthlyOthers;
    }
    
    public void setMonthlyOthers(Integer monthlyOthers) {
        this.monthlyOthers = monthlyOthers;
    }
    
    public Integer getMonthlyTotal() {
        return monthlyTotal;
    }
    
    public void setMonthlyTotal(Integer monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }
    
    public Integer getAnnualTotal() {
        return annualTotal;
    }
    
    public void setAnnualTotal(Integer annualTotal) {
        this.annualTotal = annualTotal;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

