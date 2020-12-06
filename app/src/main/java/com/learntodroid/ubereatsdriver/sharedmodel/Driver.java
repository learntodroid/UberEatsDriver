package com.learntodroid.ubereatsdriver.sharedmodel;

import com.google.firebase.firestore.DocumentId;

public class Driver {
    private String name;
    private String userId;
    private double latitude;
    private double longitude;

    @DocumentId
    private String documentId;

    public Driver() {
    }

    public Driver(String name, String userId, double latitude, double longitude) {
        this.name = name;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDocumentId() {
        return documentId;
    }
}
