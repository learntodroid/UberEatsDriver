package com.learntodroid.ubereatsdriver.orderslist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.learntodroid.ubereatsdriver.loginsignup.UberEatsDriverRepository;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

import java.util.List;

public class OrdersListViewModel extends ViewModel {
    private UberEatsDriverRepository uberEatsDriverRepository;
    private MutableLiveData<List<Order>> ordersLiveData;

    public OrdersListViewModel() {
        uberEatsDriverRepository = UberEatsDriverRepository.getInstance();
        ordersLiveData = uberEatsDriverRepository.getOrdersLiveData();
    }

    public void queryOrders(String ordersStatus) {
        uberEatsDriverRepository.queryOrders(ordersStatus);
    }

    public void updateOrderStatus(Order order, String newStatus) {
        uberEatsDriverRepository.updateOrderStatus(order, newStatus);
    }

    public void setSelectedOrder(Order order) {
        uberEatsDriverRepository.setSelectedOrder(order);
    }

    public MutableLiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }
}
