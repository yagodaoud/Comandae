package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.model.Currency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
public class BitcoinApiService {
    private static final Logger logger = Logger.getLogger(BitcoinApiService.class.getName());
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=";
    private final RestTemplate restTemplate;

    public BitcoinApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getBitcoinPrice(Currency currency) {
        try {
            String currencyCode = currency.toString().toLowerCase();
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                    COINGECKO_API_URL + currencyCode,
                    JsonNode.class
            );

            if (response.getBody() != null) {
                return new BigDecimal(
                        response.getBody()
                                .get("bitcoin")
                                .get(currencyCode)
                                .asText()
                );
            }

            throw new RuntimeException("Unable to fetch Bitcoin price: Empty response");
        } catch (RestClientException e) {
            logger.severe("Error fetching Bitcoin price: " + e.getMessage());
            throw new RuntimeException("Failed to fetch Bitcoin price", e);
        }
    }

    public BigDecimal getCurrentBitcoinPriceInBrl() {
        return getBitcoinPrice(Currency.BRL);
    }
}