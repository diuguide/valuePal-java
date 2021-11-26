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
    private double avg_price;

    @Column(name="change", precision=6, scale=2)
    private double change;

    @Column(name="totalValue", precision=10, scale=2)
    private double totalValue;

    public Holding() {
    }

    public Holding(int walletId, String ticker, int quantity, double price) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
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

    @Override
    public String toString() {
        return "Holding{" +
                "id=" + id +
                ", wallet=" + walletId +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalValue=" + totalValue +
                '}';
    }
}
