package com.jurgens.merchantplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "mpesa")
@Getter
@Setter
public class MpesaConfig {

    private String consumerKey;

    private String consumerSecret;

    private String shortCode;

    private String passkey;

    private String callbackUrl;

    private String authUrl;

    private String stkPushUrl;
}
