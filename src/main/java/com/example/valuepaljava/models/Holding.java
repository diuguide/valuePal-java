package com.example.valuepaljava.models;

import javax.persistence.*;

@Entity
@Table(name="holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="wallet_id", nullable=false)
    private Wallet wallet;

    @Column(name="ticker")
    private String ticker;

    @Column(name="quantity")
    private int quantity;

    @Column(name="price")
    private double price;

    @Column(name="totalValue")
    private double totalValue;

    public Holding() {
    }

    public Holding(Wallet wallet, String ticker, int quantity, double price) {
        this.wallet = wallet;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = quantity * price;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "Holding{" +
                "id=" + id +
                ", wallet=" + wallet +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalValue=" + totalValue +
                '}';
    }
}
