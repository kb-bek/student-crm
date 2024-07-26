package com.kaitech.student_crm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Component
public class OAuth2TokenValidator {

    private final RestTemplate restTemplate;

    @Autowired
    public OAuth2TokenValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateToken(String accessToken) {
        try {
            String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.error("Token validation failed", e);
            return false;
        }
    }
}

