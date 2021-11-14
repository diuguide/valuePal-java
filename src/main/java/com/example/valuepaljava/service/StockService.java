package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.HeaderConfig;
import com.example.valuepaljava.models.SummaryObject;
import com.example.valuepaljava.repos.SummaryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StockService {

    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final HeaderConfig headerConfig;
    private final SummaryRepository summaryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private StringBuilder uri;

    @Autowired
    public StockService(HeaderConfig headerConfig, SummaryRepository summaryRepository) {
        this.headerConfig = headerConfig;
        this.summaryRepository = summaryRepository;
    }

    public ResponseEntity<String> summaryApiCall() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        String uri = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-summary?region=BR";
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class, 1);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Request Successful.");
        } else {
            logger.info("Request Failed");
        }
        return response;
    }

    public void addSummaryRecord() throws JsonProcessingException {
        ResponseEntity<String> response = summaryApiCall();
        String jsonResponse = response.getBody();
        JsonNode jsonRes = objectMapper.readTree(jsonResponse);
        String result = jsonRes.get("marketSummaryAndSparkResponse").get("result").toString();
        List<Map<String, Object>> mapped = objectMapper.readValue(result, new TypeReference<List<Map<String, Object>>>(){});
        List<SummaryObject> returnRecord = mapToObjects(mapped);

        try {
            summaryRepository.deleteAll();
            logger.info("delete all records from market table");
            for (SummaryObject obj: returnRecord) {
                summaryRepository.save(obj);
                logger.info("Stock data stored to db");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SummaryObject> mapToObjects(List<Map<String, Object>> mapped) {
        List<SummaryObject> record = new LinkedList<>();
        for (Map<String, Object> each: mapped) {
            SummaryObject newSummary = new SummaryObject();
            newSummary.setFullExchangeName(each.get("fullExchangeName").toString());
            newSummary.setExchange(each.get("exchange").toString());
            Map<String, Object> mp = (Map<String, Object>) each.get("spark");
            List<Integer> ln = (List<Integer>) mp.get("timestamp");
            List<Double> tn = (List<Double>) mp.get("close");
            newSummary.setTimestamp(ln);
            newSummary.setClose(tn);
            record.add(newSummary);
        }
        return filterDJIData(record);

    }

    public List<SummaryObject> filterDJIData(List<SummaryObject> dataList) {
        List<String> markets = Arrays.asList("OSA", "DJI", "HKE", "SAO", "BUE", "MEX");
        List<SummaryObject> singleObj = dataList.stream()
                .filter(obj -> markets.contains(obj.getExchange()))
                .collect(Collectors.toList());
        return singleObj;
    }

    public String getTickerData(int api, String... ticker){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        if(api == 1) {
            uri = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-quotes");
        } else if (api == 2) {
            uri = new StringBuilder("https://yh-finance.p.rapidapi.com/market/v2/get-quotes");
        }
        uri.append("?symbols=");
        for(String el : ticker) {
            uri.append(el).append(",");
        }
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
        return response.getBody();
    }

    public String getTickerHistory(int api, String interval, String range, String... ticker) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headerConfig.yahooHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        if(api == 1) {
            uri = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-spark");
        } else if (api == 2) {
            uri = new StringBuilder("https://yh-finance.p.rapidapi.com/market/get-spark");
        }
        uri.append("?symbols=");
        for(String el : ticker) {
            uri.append(el).append(",");
        }
        uri.append("&interval=").append(interval).append("&range=").append(range);
        logger.info(uri.toString());
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
        return response.getBody();
    }
}
