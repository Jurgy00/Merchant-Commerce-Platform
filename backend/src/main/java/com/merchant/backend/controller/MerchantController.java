package com.merchant.backend.controllers;

import com.merchant.backend.entities.Merchant;
import com.merchant.backend.repositories.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MerchantController {

    private final MerchantRepository merchantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Merchant> getMerchantById(@PathVariable Long id) {
        return merchantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
