package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.config.MpesaConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class MpesaService {

    private final MpesaConfig mpesaConfig;
    private final DarajaAuthService darajaAuthService;
    private final RestClient restClient;

    public MpesaService(
            MpesaConfig mpesaConfig,
            DarajaAuthService darajaAuthService
    ) {
        this.mpesaConfig = mpesaConfig;
        this.darajaAuthService = darajaAuthService;
        this.restClient = RestClient.create();
    }

    public StkPushResponse initiateStkPush(
            int amount,
            String phoneNumber,
            String accountReference
    ) {

        String accessToken = darajaAuthService.getAccessToken();

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String passwordString =
                mpesaConfig.getShortCode()
                        + mpesaConfig.getPasskey()
                        + timestamp;

        String password = Base64.getEncoder()
                .encodeToString(
                        passwordString.getBytes(StandardCharsets.UTF_8)
                );

        Map<String, Object> request = new HashMap<>();

        request.put("BusinessShortCode", mpesaConfig.getShortCode());
        request.put("Password", password);
        request.put("Timestamp", timestamp);
        request.put("TransactionType", "CustomerPayBillOnline");
        request.put("Amount", amount);
        request.put("PartyA", phoneNumber);
        request.put("PartyB", mpesaConfig.getShortCode());
        request.put("PhoneNumber", phoneNumber);
        request.put("CallBackURL", mpesaConfig.getCallbackUrl());
        request.put("AccountReference", accountReference);
        request.put("TransactionDesc", "Merchant Commerce Platform Payment");

        return restClient.post()
                .uri(mpesaConfig.getStkPushUrl())
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .retrieve()
                .body(StkPushResponse.class);
    }

    public record StkPushResponse(
            String MerchantRequestID,
            String CheckoutRequestID,
            String ResponseCode,
            String ResponseDescription,
            String CustomerMessage
    ) {
    }
}