package com.example.valuepaljava.Yahoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class HeaderConfig {

    private final ApiConfig apiConfig;

    @Autowired
    public HeaderConfig(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public HttpHeaders yahooHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-rapidapi-key", apiConfig.getYahooKey());
        headers.set("x-rapidapi-host", apiConfig.getYahooHost());
        return headers;
    }

    public HttpHeaders newsHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("x-rapidapi-key", apiConfig.getYahooKey());
        headers.set("x-rapidapi-host", apiConfig.getYahooHost());
        headers.set("data", "");
        return headers;
    }

}
