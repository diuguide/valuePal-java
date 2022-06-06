package com.example.valuepaljava.models;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Entity
@Table(name="bankLedger")
public class BankLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="wallet_id")
    private int wallet_id;

    @Column(name="username")
    private String username;

    @Column(name="timestamp")
    private Date timestamp;

    @Column(name="amount")
    private double amount;

    @Column(name="status")
    private String status;

    @Column(name="type")
    private String type;

    public BankLedger() {
    }

    public BankLedger(int wallet_id, String username, Date timestamp, double amount, String status, String type) {
        this.wallet_id = wallet_id;
        this.username = username;
        this.timestamp = timestamp;
        this.amount = amount;
        this.status = status;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
