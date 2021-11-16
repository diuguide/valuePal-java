package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="walletId")
    private Integer walletId;

    @Column(name="ticker")
    private String ticker;

    @Column(name="price", precision=10, scale=2)
    private double price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="status")
    private String status;

    public Order() {
    }

    public Order(Integer walletId, String ticker, double price, Integer quantity) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public double getPrice() {
        return price;
    }

    public double getTotalValue() {
        return price * quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
