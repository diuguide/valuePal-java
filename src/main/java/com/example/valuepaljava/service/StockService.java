package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.HeaderConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StockService {

    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final HeaderConfig headerConfig;
    private StringBuilder uri;

    @Autowired
    public StockService(HeaderConfig headerConfig) {
        this.headerConfig = headerConfig;
    }

    public String getTickerData(int api, String... ticker){
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        if(api == 1) {
            uri = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-quotes");
        } else if (api == 2) {
            uri = new StringBuilder("https://yh-finance.p.rapidapi.com/market/v2/get-quotes");
        }
        uri.append("?symbols=");
        for(String el : ticker) {
            uri.append(el).append(",");
        }
        logger.info(uri.toString());
        try {
            ResponseEntity<String> quoteResponse = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
            long endTime = System.currentTimeMillis();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(quoteResponse.getBody()));
            duration = endTime - startTime;
            logger.info(String.format("[API] getTickerData called: duration %s/ms", duration));
            return quoteResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "An error has occurred!";
    }

    public String getTickerHistory(int api, String interval, String range, String... ticker) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        if(api == 1) {
            uri = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-spark");
        } else if (api == 2) {
            uri = new StringBuilder("https://yh-finance.p.rapidapi.com/market/get-spark");
        }
        uri.append("?symbols=");
        for(String el : ticker) {
            uri.append(el).append(",");
        }
        uri.append("&interval=").append(interval).append("&range=").append(range);
        logger.info(uri.toString());
        try {
            ResponseEntity<String> historyResponse = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[API] getTickerHistory called: duration %s/ms", duration));
            return historyResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return "An Error has occurred!";
    }



}
