package com.example.valuepaljava.models;
import javax.persistence.*;

@Entity
@Table(name ="holding_records")
public class HoldingRecord {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="wallet_id")
    private int wallet_id;

    @Column(name="ticker")
    private String ticker;

    @Column(name="quantity")
    private int quantity;

    @Column(name="avgprice")
    private double avgprice;

    public HoldingRecord(int wallet_id, int id, String ticker, int quantity, double avgprice) {
        this.wallet_id = wallet_id;
        this.id = id;
        this.ticker = ticker;
        this.quantity = quantity;
        this.avgprice = avgprice;
    }

    public HoldingRecord() {};

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAvgPrice() {
        return avgprice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgprice = avgPrice;
    }
}
