package com.example.valuepaljava.util;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.Quote;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class JsonUtil {

    private final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public Set<Quote> jsonParser(String jsonString) throws ParseException {
        long startTime = System.currentTimeMillis();
        long duration = 0;
        Set<Quote> quotes = new HashSet<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");
        JSONArray result = (JSONArray) quoteResponse.get("result");
        if(result.size() > 0) {
            for (Object o : result) {
                JSONObject current = (JSONObject) o;
                Quote currentQuote = new Quote(
                        (String) current.get("symbol"),
                        (String) current.get("longName"),
                        (double) current.get("regularMarketPrice"),
                        (double) current.get("regularMarketChange"),
                        (double) current.get("regularMarketChangePercent"),
                        (long) current.get("regularMarketTime"));
                quotes.add(currentQuote);
            }
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[PARSE] JSON String parsed, duration %s/ms", duration));
            return quotes;
        } else {
            throw new InvalidInputException("Stock data not available!");
        }

    }

}
