package com.example.govrecords.repository;

import com.example.govrecords.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenRepository extends JpaRepository<Citizen, Long> { }
 