package com.example.valuepaljava.service;

import com.example.valuepaljava.Yahoo.ApiConfig;
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
    private final ApiConfig apiConfig;
    private final SummaryRepository summaryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public StockService(ApiConfig apiConfig, SummaryRepository summaryRepository) {
        this.apiConfig = apiConfig;
        this.summaryRepository = summaryRepository;
    }

    public HttpEntity yahooHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-rapidapi-key", apiConfig.getYahooKey());
        headers.set("x-rapidapi-host", apiConfig.getYahooHost());
        return new HttpEntity(headers);
    }

    public ResponseEntity<String> summaryApiCall() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = yahooHeaders();

        ResponseEntity<String> response = restTemplate.exchange(apiConfig.getYahooSummaryUrl(), HttpMethod.GET, request, String.class, 1);
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

    public String getTickerData(String... ticker){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = yahooHeaders();
        StringBuilder uri = new StringBuilder(apiConfig.getYahooTickerURL());
        uri.append("?symbols=");
        for(String el : ticker) {
            uri.append(el).append(",");
        }
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class, 1);
        return response.getBody();
    }
}
