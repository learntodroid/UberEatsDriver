package com.learntodroid.ubereatsdriver.sharedmodel;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;

public class Order {
    private Restaurant restaurant;
    private UberEatsAccount account;
    private ShoppingCart cart;
    private String status;
    private HashMap<String, Boolean> notifications;
    private String driverId;

    @DocumentId
    private String documentId;

    public Order() {

    }

    public Order(Restaurant restaurant, UberEatsAccount account, ShoppingCart cart, String status, String driverId) {
        this.restaurant = restaurant;
        this.account = account;
        this.cart = cart;
        this.status = status;
        this.notifications = new HashMap<>();
        this.notifications.put("New", false);
        this.notifications.put("Preparing", false);
        this.notifications.put("Awaiting Collection", false);
        this.notifications.put("Delivering", false);
        this.notifications.put("Delivered", false);
        this.driverId = driverId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public UberEatsAccount getAccount() {
        return account;
    }

    public void setAccount(UberEatsAccount account) {
        this.account = account;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, Boolean> getNotifications() {
        return notifications;
    }

    public void setNotifications(HashMap<String, Boolean> notifications) {
        this.notifications = notifications;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
