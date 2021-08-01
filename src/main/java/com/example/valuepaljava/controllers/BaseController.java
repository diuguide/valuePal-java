package com.example.valuepaljava.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class BaseController {
    @GetMapping
    public void awsHealthCheck(HttpServletResponse response) {
        response.setStatus(200);
    }
}
