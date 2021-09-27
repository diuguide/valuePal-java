package com.example.valuepaljava.controllers;

import com.example.valuepaljava.Yahoo.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/calls")
public class CallController {

    private Logger logger = LoggerFactory.getLogger(CallController.class);
    private ApiConfig apiConfig;

    @Autowired
    public CallController(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @GetMapping(value="/test")
    public String testController() {
        System.out.println("test controller works");
        return apiConfig.getYahooHost();
    }


}
