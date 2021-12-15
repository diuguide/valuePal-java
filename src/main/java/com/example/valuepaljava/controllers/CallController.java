package com.example.valuepaljava.controllers;

import com.example.valuepaljava.models.Quote;
import com.example.valuepaljava.service.StockService;
import com.example.valuepaljava.util.JsonUtil;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
        logger.info("Stock api called - YahooFinance - /market/getQuote");
        return stockService.getTickerData(1, ticker);
    }

    @PostMapping(value="/getQuoteYH")
    public Set<Quote> getQuoteYH(@RequestParam String... ticker) throws ParseException {
        logger.info("Stock API called - YH Finance - /market/getQuote");
        return jsonUtil.jsonParser(stockService.getTickerData(2, ticker));
    }

    @PostMapping(value="/getHistory")
    public String getTickerHistory(@RequestHeader HttpHeaders headers, @RequestParam int api, @RequestParam String interval, @RequestParam String range, @RequestParam String... ticker) {
        logger.info("Stock history api called");
        return stockService.getTickerHistory(api, interval, range, ticker);
    }

}
