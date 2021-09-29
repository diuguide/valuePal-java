package com.example.valuepaljava.ScheduledTasks;

import com.example.valuepaljava.Yahoo.ApiConfig;
import com.example.valuepaljava.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final StockService stockService;

    public ScheduledTasks(StockService stockService) {
        this.stockService = stockService;
    }

    @Scheduled(cron = "0 5,20,35,50 * * * MON-FRI")
    public void scheduledTaskTest() throws JsonProcessingException {
        logger.info("Stock Data Gathered at {}", dateFormat.format(new Date()));
        stockService.addSummaryRecord();
    }


}
