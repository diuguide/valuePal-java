package com.example.valuepaljava.models;

import java.util.List;
import java.util.Set;

public class UserInfoDTO {

    private String first_name;
    private String username;
    private Set<Holding> holdings;
    private String role;
    private Integer wallet_id;

    public UserInfoDTO(String first_name, String username, Set<Holding> holdings, String role, Integer wallet_id) {
        this.first_name = first_name;
        this.username = username;
        this.holdings = holdings;
        this.role = role;
        this.wallet_id = wallet_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(Set<Holding> holdings) {
        this.holdings = holdings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "first_name='" + first_name + '\'' +
                ", username='" + username + '\'' +
                ", holdings=" + holdings +
                ", role='" + role + '\'' +
                ", wallet_id=" + wallet_id +
                '}';
    }
}
