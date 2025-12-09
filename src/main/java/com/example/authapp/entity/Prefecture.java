package com.example.authapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "prefectures")
public class Prefecture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", unique = true, nullable = false, length = 2)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "average_rent")
    private Integer averageRent;
    
    public Prefecture() {
    }
    
    public Prefecture(String code, String name, Integer averageRent) {
        this.code = code;
        this.name = name;
        this.averageRent = averageRent;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAverageRent() {
        return averageRent;
    }
    
    public void setAverageRent(Integer averageRent) {
        this.averageRent = averageRent;
    }
}

