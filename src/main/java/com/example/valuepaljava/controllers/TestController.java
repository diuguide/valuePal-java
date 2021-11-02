package com.example.valuepaljava.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping(value="/student")
    public String testStudentRole(){
        return "Test Student Route Working!";
    }

    @GetMapping(value="/extend/ball")
    public String extendBall(){
        return "Extend Ball Route Working!";
    }

}
