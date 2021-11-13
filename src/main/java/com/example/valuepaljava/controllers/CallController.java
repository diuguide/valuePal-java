package com.example.valuepaljava.controllers;


import com.example.valuepaljava.models.SummaryObject;
import com.example.valuepaljava.repos.SummaryRepository;
import com.example.valuepaljava.service.NewsService;
import com.example.valuepaljava.service.StockService;
import com.example.valuepaljava.service.WatchListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/calls")
public class CallController {

    private final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final StockService stockService;
    private final SummaryRepository summaryRepository;
    private final NewsService newsService;
    private final WatchListService watchListService;

    @Autowired
    public CallController(NewsService newsService, StockService stockService, SummaryRepository summaryRepository, WatchListService watchListService) {
        this.stockService = stockService;
        this.summaryRepository = summaryRepository;
        this.newsService = newsService;
        this.watchListService = watchListService;
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

    @GetMapping(value="/getNewsFeed", produces=APPLICATION_JSON_VALUE)
    public String getNewsFeed() {
        try {
            return newsService.newsApiCall();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Api call Failed!";
        }
    }

    @PostMapping(value="/ticker")
    public String getTickerData(@RequestParam String... ticker) {
        logger.info("Stock api called - YahooFinance - /market/getQuote");
        return stockService.getTickerData(1, ticker);
    }

    @PostMapping(value="/getQuoteYH")
    public String getQuoteYH(@RequestParam String... ticker) {
        logger.info("Stock API called - YH Finance - /market/getQuote");
        return stockService.getTickerData(2, ticker);
    }

    @PostMapping(value="/getHistory")
    public String getTickerHistory(@RequestHeader HttpHeaders headers, @RequestParam int api, @RequestParam String interval, @RequestParam String range, @RequestParam String... ticker) {
        logger.info("Stock history api called");
        return stockService.getTickerHistory(api, interval, range, ticker);
    }

}
