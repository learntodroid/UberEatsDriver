package com.learntodroid.ubereatsdriver.orderslist;

import com.learntodroid.ubereatsdriver.sharedmodel.Order;

public interface OnOrderProgressionClickListener {
    void onOrderReserved(Order order);
    void onOrderDelivered(Order order);
}
