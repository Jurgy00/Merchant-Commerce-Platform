package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.CreateMerchantRequest;
import com.jurgens.merchantplatform.dto.MerchantResponse;
import com.jurgens.merchantplatform.dto.UpdateMerchantRequest;
import com.jurgens.merchantplatform.entities.Merchant;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public MerchantResponse createMerchant(CreateMerchantRequest request) {

        if (merchantRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Merchant with email already exists");
        }

        Merchant merchant = new Merchant();

        merchant.setBusinessName(request.getBusinessName());
        merchant.setEmail(request.getEmail());
        merchant.setPhoneNumber(request.getPhoneNumber());

        Merchant savedMerchant = merchantRepository.save(merchant);

        return convertToResponse(savedMerchant);
    }

    public List<MerchantResponse> getAllMerchants() {

        return merchantRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public MerchantResponse getMerchantById(Long id) {

        Merchant merchant = getMerchantEntityById(id);

        return convertToResponse(merchant);
    }

    public MerchantResponse updateMerchant(
            Long id,
            UpdateMerchantRequest request) {

        Merchant existingMerchant = getMerchantEntityById(id);

        if (!existingMerchant.getEmail().equals(request.getEmail())) {

            if (merchantRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Merchant with email already exists");
            }
        }

        existingMerchant.setBusinessName(request.getBusinessName());
        existingMerchant.setEmail(request.getEmail());
        existingMerchant.setPhoneNumber(request.getPhoneNumber());

        Merchant updatedMerchant = merchantRepository.save(existingMerchant);

        return convertToResponse(updatedMerchant);
    }

    public void deleteMerchant(Long id) {

        Merchant merchant = getMerchantEntityById(id);

        merchantRepository.delete(merchant);
    }

    private Merchant getMerchantEntityById(Long id) {

        return merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found with id: " + id
                        ));
    }

    private MerchantResponse convertToResponse(Merchant merchant) {

        MerchantResponse response = new MerchantResponse();

        response.setId(merchant.getId());
        response.setBusinessName(merchant.getBusinessName());
        response.setEmail(merchant.getEmail());
        response.setPhoneNumber(merchant.getPhoneNumber());

        return response;
    }
}