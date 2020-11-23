package com.learntodroid.ubereatsdriver.sharedmodel;

public class Address {
    private String address;
    private String addressType;
    private double latitude, longitude;

    public Address() {

    }

    public Address(String address, double latitude, double longitude, String addressType) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
