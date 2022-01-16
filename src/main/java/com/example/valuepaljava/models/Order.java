package com.example.valuepaljava.models;

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

    @Column(name="type")
    private char orderType;

    @Column(name="total_cost", precision=10, scale=2)
    private double total_cost;

    public Order() {
    }

    public Order(Integer walletId, String ticker, double price, Integer quantity) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
    }

    public char getOrderType() {
        return orderType;
    }

    public void setOrderType(char orderType) {
        this.orderType = orderType;
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

    public void setTotal_cost() { this.total_cost = price * quantity;}

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
                "id=" + id +
                ", walletId=" + walletId +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", orderType=" + orderType +
                ", total_cost=" + total_cost +
                '}';
    }
}
