package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int walletId;

    @Column(name="total_cash")
    private double totalCash = 0;

    @Column(name="totalValue")
    private double totalValue;

    public Wallet() {
    }

    public Wallet(double startingCash) {
        this.totalCash = startingCash;
    }

    public Wallet(int walletId, double totalCash, double totalValue) {
        this.walletId = walletId;
        this.totalCash = totalCash;
        this.totalValue = totalValue;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", totalCash=" + totalCash +
                ", totalValue=" + totalValue +
                '}';
    }
}
