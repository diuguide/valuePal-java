package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.ApiConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NewsService {

    private final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final ApiConfig apiConfig;

    @Autowired
    public NewsService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public HttpEntity newsHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("x-rapidapi-key", apiConfig.getYahooKey());
        headers.set("x-rapidapi-host", apiConfig.getYahooHost());
        headers.set("data", "");
        return new HttpEntity<>(headers);
    }

    public String newsApiCall() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = newsHeaders();
        ResponseEntity<String> response = restTemplate.postForEntity(apiConfig.getYahooNewsFeedUrl(), request, String.class);
        logger.info("Api news call fired!");
        return response.getBody();
    };

}
