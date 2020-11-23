package com.learntodroid.ubereatsdriver.sharedmodel;

public class MenuItem {
    private String title;
    private String description;
    private String imageUri;
    private double price;

    public MenuItem() {

    }

    public MenuItem(String title, String description, String imageUri, double price) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
