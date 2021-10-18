package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WatchListService {

    private final Logger logger = LoggerFactory.getLogger(WatchListService.class);
    private final ApiConfig apiConfig;

    @Autowired
    public WatchListService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    



}
