package com.example.valuepaljava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ApiQueue {

    private final Logger logger = LoggerFactory.getLogger(ApiQueue.class);
    private static ApiQueue apiQueue = null;
    private static List<ResponseEntity<String>> queue = new LinkedList<>();

    public static ApiQueue getInstance() {
        if(apiQueue == null) {
            apiQueue = new ApiQueue();
        }
        return apiQueue;
    }

    public static void addToQueue(ResponseEntity<String> request) {
        queue.add(request);
    }

    public ResponseEntity<String> runQueue() {
        while (queue.size() > 0) {

        }
    }


}
