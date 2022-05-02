package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.HeaderConfig;
import com.example.valuepaljava.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AutoCompleteService {

    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final HeaderConfig headerConfig;
    private StringBuilder uri;

    @Autowired
    public AutoCompleteService(HeaderConfig headerConfig) {
        this.headerConfig = headerConfig;
    }

    public String getAutoCompleteResponse(String input) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);

        uri = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/auto-complete");
        uri.append("?q=");
        uri.append(input);

        try {
            ResponseEntity<String> quoteResponse = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[API] getAutoCompleteResponse called: duration %s/ms", duration));
            return quoteResponse.getBody();
        } catch (Exception e) {
            logger.info("ERROR INSIDE API CALL");
            throw new InvalidInputException("An error has occurred, please try again.");
        }
    }

}
