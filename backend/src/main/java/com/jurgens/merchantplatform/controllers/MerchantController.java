package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.dto.CreateMerchantRequest;
import com.jurgens.merchantplatform.dto.MerchantResponse;
import com.jurgens.merchantplatform.dto.UpdateMerchantRequest;
import com.jurgens.merchantplatform.services.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<MerchantResponse> createMerchant(
            @RequestBody CreateMerchantRequest request) {

        MerchantResponse created =
                merchantService.createMerchant(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MerchantResponse>> getAllMerchants() {

        return ResponseEntity.ok(
                merchantService.getAllMerchants()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchantById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                merchantService.getMerchantById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantResponse> updateMerchant(
            @PathVariable Long id,
            @RequestBody UpdateMerchantRequest request) {

        return ResponseEntity.ok(
                merchantService.updateMerchant(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(
            @PathVariable Long id) {

        merchantService.deleteMerchant(id);

        return ResponseEntity.noContent().build();
    }
}