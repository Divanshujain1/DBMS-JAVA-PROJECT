package com.example.govrecords.controller;

import com.example.govrecords.dto.CitizenDto;
import com.example.govrecords.model.Citizen;
import com.example.govrecords.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@Controller
public class CitizenController {

    @Autowired
    private CitizenService service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("citizenDto", new CitizenDto());
        return "index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("citizenDto") CitizenDto dto,
                           BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("error", "Validation failed!");
            return "index";
        }
        try {
            service.save(dto);
        } catch (Exception e) {
            model.addAttribute("error", "Server encryption error: " + e.getMessage());
            return "index";
        }
        return "redirect:/citizens";
    }

    @GetMapping("/citizens")
    public String list(Model model) {
        List<Citizen> all = service.listAll();
        List<Map<String,Object>> view = new ArrayList<>();
        for (Citizen c : all) {
            Map<String,Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("fullName", c.getFullName());
            try {
                String pan = com.example.govrecords.util.EncryptionUtil.decrypt(c.getPanEncrypted());
                String aad = com.example.govrecords.util.EncryptionUtil.decrypt(c.getAadhaarEncrypted());
                m.put("panMasked", com.example.govrecords.util.EncryptionUtil.mask(pan,4));
                m.put("aadhaarMasked", com.example.govrecords.util.EncryptionUtil.mask(aad,4));
            } catch (Exception ex) {
                m.put("panMasked", "****");
                m.put("aadhaarMasked", "****");
            }
            view.add(m);
        }
        model.addAttribute("citizens", view);
        return "list";
    }
}
