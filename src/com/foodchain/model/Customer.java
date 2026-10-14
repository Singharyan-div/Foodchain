package com.foodchain.model;

public class Customer {
    private final String customerId;
    private String name;
    private String market;
    private String location;

    public Customer(String customerId, String name, String market, String location) {
        this.customerId = customerId;
        this.name = name;
        this.market = market;
        this.location = location;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
