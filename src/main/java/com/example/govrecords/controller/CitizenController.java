package com.example.govrecords.controller;

import com.example.govrecords.dto.CitizenDto;
import com.example.govrecords.model.Citizen;
import com.example.govrecords.service.CitizenService;
import com.example.govrecords.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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
        // For UI we show masked last4 only — do not decrypt by default
        List<Map<String,Object>> view = new ArrayList<>();
        for (Citizen c : all) {
            Map<String,Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("fullName", c.getFullName());
            // try to decrypt locally to mask — if key missing, show placeholders
            try {
                String pan = EncryptionUtil.decrypt(c.getPanEncrypted());
                String aad = EncryptionUtil.decrypt(c.getAadhaarEncrypted());
                m.put("panMasked", EncryptionUtil.mask(pan,4));
                m.put("aadhaarMasked", EncryptionUtil.mask(aad,4));
            } catch (Exception ex) {
                m.put("panMasked", "****");
                m.put("aadhaarMasked", "****");
            }
            view.add(m);
        }
        model.addAttribute("citizens", view);
        return "list";
    }

    // Admin-only decrypt endpoint (protected by ADMIN_API_KEY header)
    @GetMapping("/api/citizen/{id}/decrypt")
    @ResponseBody
    public ResponseEntity<?> decrypt(@PathVariable Long id, @RequestHeader(name="X-ADMIN-KEY", required=false) String apiKey) {
        String expected = System.getenv("ADMIN_API_KEY");
        if (expected == null || !expected.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        Optional<Citizen> opt = service.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        Citizen c = opt.get();
        try {
            Map<String,Object> out = new HashMap<>();
            out.put("id", c.getId());
            out.put("fullName", c.getFullName());
            out.put("pan", EncryptionUtil.decrypt(c.getPanEncrypted()));
            out.put("aadhaar", EncryptionUtil.decrypt(c.getAadhaarEncrypted()));
            return ResponseEntity.ok(out);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Decryption failed: " + ex.getMessage());
        }
    }
}
