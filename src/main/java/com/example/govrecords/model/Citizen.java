package com.example.govrecords.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String panEncrypted;
    private String aadhaarEncrypted;

    // No-args constructor
    public Citizen() {}

    // All-args constructor
    public Citizen(String fullName, String panEncrypted, String aadhaarEncrypted) {
        this.fullName = fullName;
        this.panEncrypted = panEncrypted;
        this.aadhaarEncrypted = aadhaarEncrypted;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPanEncrypted() { return panEncrypted; }
    public void setPanEncrypted(String panEncrypted) { this.panEncrypted = panEncrypted; }

    public String getAadhaarEncrypted() { return aadhaarEncrypted; }
    public void setAadhaarEncrypted(String aadhaarEncrypted) { this.aadhaarEncrypted = aadhaarEncrypted; }
}
