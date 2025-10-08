package com.example.govrecords.model;

import jakarta.persistence.*;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "pan_encrypted", nullable = false)
    private String panEncrypted;

    @Column(name = "aadhaar_encrypted", nullable = false)
    private String aadhaarEncrypted;

    public Citizen() {}

    public Citizen(String fullName, String panEncrypted, String aadhaarEncrypted) {
        this.fullName = fullName;
        this.panEncrypted = panEncrypted;
        this.aadhaarEncrypted = aadhaarEncrypted;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPanEncrypted() { return panEncrypted; }
    public void setPanEncrypted(String panEncrypted) { this.panEncrypted = panEncrypted; }
    public String getAadhaarEncrypted() { return aadhaarEncrypted; }
    public void setAadhaarEncrypted(String aadhaarEncrypted) { this.aadhaarEncrypted = aadhaarEncrypted; }
}
