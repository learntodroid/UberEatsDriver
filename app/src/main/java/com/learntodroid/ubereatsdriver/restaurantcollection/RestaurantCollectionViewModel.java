package com.learntodroid.ubereatsdriver.restaurantcollection;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.learntodroid.ubereatsdriver.loginsignup.UberEatsDriverRepository;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

public class RestaurantCollectionViewModel extends ViewModel {
    private UberEatsDriverRepository uberEatsDriverRepository;
    private MutableLiveData<Order> selectedOrderLiveData;
    private MutableLiveData<Location> driverLocationLiveData;
    private MutableLiveData<DirectionsResult> restaurantDirectionsResultLiveData;

    public RestaurantCollectionViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        selectedOrderLiveData = uberEatsDriverRepository.getSelectedOrderLiveData();
        driverLocationLiveData = uberEatsDriverRepository.getDriverLocationLiveData();
        restaurantDirectionsResultLiveData = uberEatsDriverRepository.getRestaurantDirectionsResultLiveData();
    }

    public void updateDriverLocation(Location location) {
        uberEatsDriverRepository.updateDriverLocation(location);
    }

    public void calculateDirections(GeoApiContext geoApiContext, LatLng start, LatLng end) {
        uberEatsDriverRepository.calculateDirections(geoApiContext, start, end);
    }

    public MutableLiveData<Order> getSelectedOrderLiveData() {
        return selectedOrderLiveData;
    }

    public MutableLiveData<Location> getDriverLocationLiveData() {
        return driverLocationLiveData;
    }

    public MutableLiveData<DirectionsResult> getRestaurantDirectionsResultLiveData() {
        return restaurantDirectionsResultLiveData;
    }
}
