package com.example.valuepaljava.Yahoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YahooFinanceSummary {

    private static final Logger logger = LoggerFactory.getLogger(YahooFinanceSummary.class);
    private final String apiUri = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-summary?region:US";
    private RestTemplate restTemplate;

    @Autowired
    public YahooFinanceSummary(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getRequestTest() {

    }
}
