package com.learntodroid.ubereatsdriver.sharedmodel;

public class PaymentMethod {
    private String method;

    public PaymentMethod() {

    }

    public PaymentMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
