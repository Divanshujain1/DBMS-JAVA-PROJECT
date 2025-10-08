package com.example.govrecords.service;

import com.example.govrecords.dto.CitizenDto;
import com.example.govrecords.model.Citizen;
import com.example.govrecords.repository.CitizenRepository;
import com.example.govrecords.util.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class CitizenService  {

    @Autowired
    private CitizenRepository repo;

    public Citizen save(CitizenDto dto) throws Exception {
        if (dto.getPan().length() != 10 || dto.getAadhaar().length() != 12) {
            throw new IllegalArgumentException("PAN or Aadhaar length invalid");
        }
        String panEnc = EncryptionUtil.encrypt(dto.getPan());
        String aadhaarEnc = EncryptionUtil.encrypt(dto.getAadhaar());
        Citizen c = new Citizen(dto.getFullName(), panEnc, aadhaarEnc);
        return repo.save(c);
    }

    public List<Citizen> listAll() {
        return repo.findAll();
    }

    public Optional<Citizen> findById(Long id) {
        return repo.findById(id);
    }
}
//Nju+fulgNI5vlcc4SfIHJVMFB3sQuwRDCIlfmMoPfag=
