package com.learntodroid.ubereatsdriver.customerdelivery;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.learntodroid.ubereatsdriver.loginsignup.UberEatsDriverRepository;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

public class CustomerDeliveryViewModel extends ViewModel {
    private UberEatsDriverRepository uberEatsDriverRepository;
    private MutableLiveData<Order> selectedOrderLiveData;
    private MutableLiveData<Location> customerDriverLocationLiveData;
    private MutableLiveData<DirectionsResult> customerDirectionsResultLiveData;

    public CustomerDeliveryViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        selectedOrderLiveData = uberEatsDriverRepository.getSelectedOrderLiveData();
        customerDriverLocationLiveData = uberEatsDriverRepository.getCustomerDriverLocationLiveData();
        customerDirectionsResultLiveData = uberEatsDriverRepository.getCustomerDirectionsResultLiveData();
    }

    public void updateCustomerDriverLocation(Location location) {
        uberEatsDriverRepository.updateCustomerDriverLocation(location);
    }

    public void calculateCustomerDirections(GeoApiContext geoApiContext, LatLng start, LatLng end) {
        uberEatsDriverRepository.calculateCustomerDirections(geoApiContext, start, end);
    }

    public MutableLiveData<Order> getSelectedOrderLiveData() {
        return selectedOrderLiveData;
    }

    public MutableLiveData<Location> getDriverLocationLiveData() {
        return customerDriverLocationLiveData;
    }

    public MutableLiveData<DirectionsResult> getCustomerDirectionsResultLiveData() {
        return customerDirectionsResultLiveData;
    }
}
