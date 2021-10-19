package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.ApiConfig;

import com.example.valuepaljava.Yahoo.HeaderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NewsService {

    private final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final HeaderConfig headerConfig;

    @Autowired
    public NewsService(HeaderConfig headerConfig) {
        this.headerConfig = headerConfig;
    }

    public String newsApiCall() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.newsHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        String uri = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US";
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        logger.info("News API fired");
        return response.getBody();
    };

}
