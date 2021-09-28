package com.example.valuepaljava.controllers;

import com.example.valuepaljava.Yahoo.ApiConfig;
import com.example.valuepaljava.models.SummaryObject;
import com.example.valuepaljava.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final StockService stockService;

    @Autowired
    public CallController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(value="/test")
    public void testController() throws JsonProcessingException {
        System.out.println("test controller works");
        stockService.addSummaryRecord();

    }

}
