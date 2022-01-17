package com.example.valuepaljava.models;

import javax.persistence.*;

@Entity
@Table(name="holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="walletId")
    private int walletId;

    @Column(name="ticker")
    private String ticker;

    @Column(name="quantity")
    private int quantity;

    @Column(name="price", precision=10, scale=2)
    private double price;

    @Column(name="avg_purchase_price", precision=10, scale=2)
    private Double avg_purchase_price;

    @Column(name="change", precision=6, scale=2)
    private double change;

    @Column(name="totalValue", precision=10, scale=2)
    private double totalValue;

    @Column(name="last_cost", precision=10, scale=2)
    private Double last_cost;

    @Column(name="process_flag")
    private char process_flag = 'H';

    public char getProcess_flag() {
        return process_flag;
    }

    public void setProcess_flag(char process_flag) {
        this.process_flag = process_flag;
    }

    public Holding() {
    }

    public Holding(int walletId, String ticker, int quantity, double price, double totalValue, Double avg_purchase_price) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
        this.avg_purchase_price = avg_purchase_price;
    }

    public Double getAvg_price() {
        return avg_purchase_price;
    }

    public void setAvg_price(Double avg_price) {
        this.avg_purchase_price = avg_price;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue() {
        this.totalValue = price * quantity;
    }

    public int getWallet() {
        return walletId;
    }

    public void setWallet(int wallet_id) {
        this.walletId = wallet_id;
    }

    public Double getLast_cost() {
        return last_cost;
    }

    public void setLast_cost(double price, double quantity) {
        this.last_cost = price * quantity;
    }

    @Override
    public String toString() {
        return "Holding{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", avg_purchase_price=" + avg_purchase_price +
                ", change=" + change +
                ", totalValue=" + totalValue +
                ", last_cost=" + last_cost +
                '}';
    }
}
