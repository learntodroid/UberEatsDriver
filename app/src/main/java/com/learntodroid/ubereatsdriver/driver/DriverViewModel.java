package com.learntodroid.ubereatsdriver.driver;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.learntodroid.ubereatsdriver.loginsignup.UberEatsDriverRepository;

public class DriverViewModel extends ViewModel {
    private UberEatsDriverRepository uberEatsDriverRepository;
    private MutableLiveData<Boolean> acceptingDeliveriesLiveData;

    public DriverViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        acceptingDeliveriesLiveData = uberEatsDriverRepository.getAcceptingDeliveriesLiveData();
    }

    public void toggleAcceptingDeliveries() {
        uberEatsDriverRepository.toggleAcceptingDeliveries();
    }

    public MutableLiveData<Boolean> getAcceptingDeliveriesLiveData() {
        return acceptingDeliveriesLiveData;
    }
}
