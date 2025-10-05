package com.example.govrecords.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CitizenDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "PAN is required")
    @Size(min = 10, max = 10, message = "PAN must be 10 characters")
    private String pan;

    @NotBlank(message = "Aadhaar is required")
    @Size(min = 12, max = 12, message = "Aadhaar must be 12 digits")
    private String aadhaar;

    // No-args constructor
    public CitizenDto() {}

    // All-args constructor
    public CitizenDto(String fullName, String pan, String aadhaar) {
        this.fullName = fullName;
        this.pan = pan;
        this.aadhaar = aadhaar;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }
}
