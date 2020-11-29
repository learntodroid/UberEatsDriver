package com.learntodroid.ubereatsdriver.driver;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.ubereatsdriver.loginsignup.UberEatsDriverRepository;
import com.learntodroid.ubereatsdriver.sharedmodel.Driver;

public class DriverViewModel extends ViewModel {
    private UberEatsDriverRepository uberEatsDriverRepository;
    private MutableLiveData<Boolean> acceptingDeliveriesLiveData;
    private MutableLiveData<Driver> driverLiveData;
    private MutableLiveData<FirebaseUser> userLiveData;

    public DriverViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        acceptingDeliveriesLiveData = uberEatsDriverRepository.getAcceptingDeliveriesLiveData();
        driverLiveData = uberEatsDriverRepository.getDriverLiveData();
        userLiveData = uberEatsDriverRepository.getUserLiveData();
        uberEatsDriverRepository.queryDriver();
    }

    public void toggleAcceptingDeliveries() {
        uberEatsDriverRepository.toggleAcceptingDeliveries();
    }

    public void createDriver(Driver driver) {
        uberEatsDriverRepository.createDriver(driver);
    }

    public MutableLiveData<Boolean> getAcceptingDeliveriesLiveData() {
        return acceptingDeliveriesLiveData;
    }

    public MutableLiveData<Driver> getDriverLiveData() {
        return driverLiveData;
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}
