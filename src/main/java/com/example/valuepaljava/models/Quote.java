package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

@Component
public class Quote {

    private String symbol;
    private String longName;
    private double price;
    private double change;
    private double changePercent;
    private long time;

    public Quote(String symbol, String longName, double price, double change, double changePercent, long time) {
        this.symbol = symbol;
        this.longName = longName;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.time = time;
    }

    public Quote() {};

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
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

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "symbol='" + symbol + '\'' +
                ", longName='" + longName + '\'' +
                ", price=" + price +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", time='" + time + '\'' +
                '}';
    }
}
