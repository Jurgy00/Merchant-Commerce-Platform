package com.jurgens.merchantplatform.services;

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

    public Merchant createMerchant(Merchant merchant) {
        if (merchantRepository.findByEmail(merchant.getEmail()).isPresent()) {
            throw new RuntimeException("Merchant with email already exists");
        }

        return merchantRepository.save(merchant);
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found with id: " + id
                        ));
    }

    public Merchant updateMerchant(Long id, Merchant merchantDetails) {
        Merchant existingMerchant = getMerchantById(id);

        if (!existingMerchant.getEmail().equals(merchantDetails.getEmail())) {
            if (merchantRepository.findByEmail(merchantDetails.getEmail()).isPresent()) {
                throw new RuntimeException("Merchant with email already exists");
            }
        }

        existingMerchant.setBusinessName(merchantDetails.getBusinessName());
        existingMerchant.setEmail(merchantDetails.getEmail());
        existingMerchant.setPhoneNumber(merchantDetails.getPhoneNumber());

        return merchantRepository.save(existingMerchant);
    }

    public void deleteMerchant(Long id) {
        Merchant merchant = getMerchantById(id);
        merchantRepository.delete(merchant);
    }
}