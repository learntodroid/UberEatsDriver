package com.learntodroid.ubereatsdriver.sharedmodel;

import java.util.ArrayList;
import java.util.List;

public class UberEatsAccount {
    private String userId;
    private List<Address> addresses;
    private List<PaymentMethod> paymentMethods;

    public UberEatsAccount() {

    }

    public UberEatsAccount(String userId) {
        this.userId = userId;
        this.addresses = new ArrayList<>();
        this.paymentMethods = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
