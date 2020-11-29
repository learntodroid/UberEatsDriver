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
    private MutableLiveData<Location> restaurantDriverLocationLiveData;
    private MutableLiveData<DirectionsResult> restaurantDirectionsResultLiveData;

    public RestaurantCollectionViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        selectedOrderLiveData = uberEatsDriverRepository.getSelectedOrderLiveData();
        restaurantDriverLocationLiveData = uberEatsDriverRepository.getRestaurantDriverLocationLiveData();
        restaurantDirectionsResultLiveData = uberEatsDriverRepository.getRestaurantDirectionsResultLiveData();
    }

    public void updateRestaurantDriverLocation(Location location) {
        uberEatsDriverRepository.updateRestaurantDriverLocation(location);
    }

    public void calculateRestaurantDirections(GeoApiContext geoApiContext, LatLng start, LatLng end) {
        uberEatsDriverRepository.calculateRestaurantDirections(geoApiContext, start, end);
    }

    public void updateOrderStatus(Order order, String newStatus) {
        uberEatsDriverRepository.updateOrderStatus(order, newStatus);
    }

    public MutableLiveData<Order> getSelectedOrderLiveData() {
        return selectedOrderLiveData;
    }

    public MutableLiveData<Location> getRestaurantDriverLocationLiveData() {
        return restaurantDriverLocationLiveData;
    }

    public MutableLiveData<DirectionsResult> getRestaurantDirectionsResultLiveData() {
        return restaurantDirectionsResultLiveData;
    }
}
