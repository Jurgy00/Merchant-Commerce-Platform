package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.config.MpesaConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class DarajaAuthService {

    private final MpesaConfig mpesaConfig;
    private final RestClient restClient;

    private String cachedToken;
    private Instant tokenExpiry;

    public DarajaAuthService(MpesaConfig mpesaConfig) {
        this.mpesaConfig = mpesaConfig;
        this.restClient = RestClient.create();
    }

    public synchronized String getAccessToken() {

        if (cachedToken != null
                && tokenExpiry != null
                && Instant.now().isBefore(tokenExpiry)) {

            return cachedToken;
        }

        String credentials =
                mpesaConfig.getConsumerKey()
                        + ":"
                        + mpesaConfig.getConsumerSecret();

        String encodedCredentials = Base64.getEncoder()
                .encodeToString(
                        credentials.getBytes(StandardCharsets.UTF_8)
                );

        DarajaAuthResponse response = restClient.get()
                .uri(mpesaConfig.getAuthUrl())
                .header("Authorization", "Basic " + encodedCredentials)
                .retrieve()
                .body(DarajaAuthResponse.class);

        if (response == null || response.access_token() == null) {
            throw new IllegalStateException(
                    "Failed to obtain Daraja access token"
            );
        }

        cachedToken = response.access_token();

        tokenExpiry = Instant.now()
                .plusSeconds(
                        Long.parseLong(response.expires_in()) - 60
                );

        return cachedToken;
    }

    private record DarajaAuthResponse(
            String access_token,
            String expires_in
    ) {
    }
}
