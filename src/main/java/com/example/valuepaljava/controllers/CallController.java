package com.example.valuepaljava.controllers;

import com.example.valuepaljava.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final StockService stockService;

    @Autowired
    public CallController(StockService stockService) {
        this.stockService = stockService;
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
