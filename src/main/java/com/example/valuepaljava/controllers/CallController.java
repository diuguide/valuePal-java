package com.example.valuepaljava.controllers;


import com.example.valuepaljava.models.SummaryObject;
import com.example.valuepaljava.repos.SummaryRepository;
import com.example.valuepaljava.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/calls")
public class CallController {

    private final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final StockService stockService;
    private final SummaryRepository summaryRepository;

    @Autowired
    public CallController(StockService stockService, SummaryRepository summaryRepository) {
        this.stockService = stockService;
        this.summaryRepository = summaryRepository;
    }

    @GetMapping(value="/summaryUpdate")
    public void testController() throws JsonProcessingException {
        stockService.addSummaryRecord();
        logger.info("Stock summary data updated manually");
    }

    @GetMapping(value="/getSummary", produces=APPLICATION_JSON_VALUE)
    public List<SummaryObject> getSummary() {
        return summaryRepository.findAll();
    }

}
