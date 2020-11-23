package com.learntodroid.ubereatsdriver.sharedmodel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    // todo restrict a cart to a restaurant
    private List<CartItem> cartItems;
    private double deliveryFee;

    public ShoppingCart() {
        this.cartItems = new ArrayList<>();
        this.deliveryFee = 0;
    }

    public void addToCart(MenuItem item, int quantity, String notes) {
        boolean match = false;
        for (CartItem cartItem: cartItems) {
            if (cartItem.getMenuItem().equals(item) && cartItem.getNotes().equals(notes)) {
                match = true;
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
        }

        if (!match) {
            cartItems.add(new CartItem(item, quantity, notes));
        }
    }

    public double calculateSubtotal() {
        double price = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            price += cartItems.get(i).getMenuItem().getPrice() * cartItems.get(i).getQuantity();
        }
        return price;
    }

    public List<PriceItem> calculatePrices() {
        List<PriceItem> priceItems = new ArrayList<>();

        double subtotal = calculateSubtotal();
        double promotion = 0;       // todo implement promotion
        double total = subtotal + promotion + deliveryFee;

        priceItems.add(new PriceItem("Subtotal", subtotal));
        priceItems.add(new PriceItem("Promotion", promotion));
        priceItems.add(new PriceItem("Delivery Fee", deliveryFee));
        priceItems.add(new PriceItem("Total", total));

        return priceItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
