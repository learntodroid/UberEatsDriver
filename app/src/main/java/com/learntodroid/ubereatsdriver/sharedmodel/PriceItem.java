package com.learntodroid.ubereatsdriver.sharedmodel;

public class PriceItem {
    private String label;
    private double price;

    public PriceItem() {
    }

    public PriceItem(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
