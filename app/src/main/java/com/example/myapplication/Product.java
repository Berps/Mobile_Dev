package com.example.myapplication;

public class Product {
    private String name;
    private String branch;
    private String expiryDate;

    public Product(String name, String branch, String expiryDate) {
        this.name = name;
        this.branch = branch;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
