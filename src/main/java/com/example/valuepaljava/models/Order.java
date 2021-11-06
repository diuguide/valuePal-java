package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

@Component
public class Order {

    private Integer walletId;
    private String ticker;
    private Integer price;
    private Integer quantity;

    public Order() {
    }

    public Order(Integer walletId, String ticker, Integer price, Integer quantity) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTotalValue() {
        return price * quantity;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "walletId=" + walletId +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
