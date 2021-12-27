package com.example.valuepaljava.controllers;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.Quote;
import com.example.valuepaljava.service.StockService;
import com.example.valuepaljava.util.JsonUtil;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/calls")
public class CallController {

    private final Logger logger = LoggerFactory.getLogger(CallController.class);
    private final StockService stockService;
    private final JsonUtil jsonUtil;

    @Autowired
    public CallController(StockService stockService, JsonUtil jsonUtil) {
        this.stockService = stockService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping(value="/ticker")
    public String getTickerData(@RequestParam String... ticker) {
        logger.info("[API] Ticker api called - YahooFinance - /market/getQuote");
        try {
            return stockService.getTickerData(1, ticker);
        } catch (Exception e) {
            return "Something went wrong...";
        }
    }

    @PostMapping(value="/getQuoteYH")
    public ResponseEntity<Object> getQuoteYH(@RequestParam String... ticker) throws ParseException {
        logger.info("[API] Ticker api called - YH Finance - /market/getQuote");
        try {
            return ResponseEntity.ok().body(jsonUtil.jsonParser(stockService.getTickerData(2, ticker)));
        } catch(InvalidInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value="/getHistory")
    public ResponseEntity<Object> getTickerHistory(@RequestHeader HttpHeaders headers, @RequestParam int api, @RequestParam String interval, @RequestParam String range, @RequestParam String... ticker) {
        logger.info("[API] Ticker history api called");
        try {
            return ResponseEntity.ok().body(stockService.getTickerHistory(api, interval, range, ticker));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
