package com.example.valuepaljava.models;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name="timestamp")
    private Date timestamp;

    public Holding() {
    }

    public Holding(int walletId, String ticker, int quantity, double price, double totalValue, Double avg_purchase_price) {
        this.walletId = walletId;
        this.ticker = ticker;
        this.quantity = quantity;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
