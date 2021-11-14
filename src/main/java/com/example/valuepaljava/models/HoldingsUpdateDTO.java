package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

@Component
public class HoldingsUpdateDTO {

    private String ticker;
    private double price;
    private double change;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
