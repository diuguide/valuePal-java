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
    private int totalCash = 0;

    @Column(name="totalValue")
    private int totalValue;

    public Wallet() {
    }

    public Wallet(int walletId, int totalCash, int totalValue) {
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

    public int getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(int totalCash) {
        this.totalCash = totalCash;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
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
