package com.example.valuepaljava.controllers;

import com.example.valuepaljava.Yahoo.ApiConfig;
import com.example.valuepaljava.models.SummaryObject;
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
    private final ApiConfig apiConfig;

    @Autowired
    public CallController(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @GetMapping(value="/test")
    public List<Map<String, Object>> testController() throws JsonProcessingException {
        System.out.println("test controller works");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("x-rapidapi-key", apiConfig.getYahooKey());
        headers.set("x-rapidapi-host", apiConfig.getYahooHost());

        HttpEntity request = new HttpEntity(headers);



        ResponseEntity<String> response = restTemplate.exchange("https://" + apiConfig.getYahooHost() + "/market/v2/get-summary?region=US", HttpMethod.GET, request, String.class, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = response.getBody();
        JsonNode jsonRes = objectMapper.readTree(jsonResponse);
        System.out.println("String response: " + response.getBody());

        String result = jsonRes.get("marketSummaryAndSparkResponse").get("result").toString();
        System.out.println("Result" + result);

        List<Map<String, Object>> mapped = objectMapper.readValue(result, new TypeReference<List<Map<String, Object>>>(){});
        System.out.println("Mapped" + mapped);
        mapToObjects(mapped);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Request Successful.");
            return mapped;
        } else {
            System.out.println("Request Failed");
            return null;
        }
//        String res = null;
//        String apiUrl = "http://testenv-env.eba-xk2s4afv.us-east-1.elasticbeanstalk.com/calls/test";
//        String response = restTemplate.getForObject(apiUrl, String.class);


    }

    public void mapToObjects(List<Map<String, Object>> mapped) {
        for (Map<String, Object> each: mapped
             ) {
            SummaryObject newSummary = new SummaryObject();
            newSummary.setFullExchangeName(each.get("fullExchangeName").toString());
            newSummary.setExchange(each.get("exchange").toString());

            Map<String, Object> mp = (Map<String, Object>) each.get("spark");

            List<Integer> ln = (List<Integer>) mp.get("timestamp");
            List<Double> tn = (List<Double>) mp.get("close");

            newSummary.setTimestamp(ln);
            newSummary.setClose(tn);

            System.out.println(newSummary);


        }
    }


}
