package ru.mephi.finance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User extends Entity {
    private String username;
    private String password;

    @JsonIgnore
    private Wallet wallet = new Wallet();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s |", getId(), getUsername());
    }
}
