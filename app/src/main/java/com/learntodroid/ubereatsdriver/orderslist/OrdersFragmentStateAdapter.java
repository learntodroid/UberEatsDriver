package com.learntodroid.ubereatsdriver.orderslist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersFragmentStateAdapter extends FragmentStateAdapter {
    public static final String ORDERS_CATEGORY_AWAITING_COLLECTION = "Awaiting Collection";
    public static final String ORDERS_CATEGORY_DELIVERING = "Delivering";
    public static final String ORDERS_CATEGORY_DELIVERED = "Delivered";

    public OrdersFragmentStateAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new OrdersListFragment();
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putString(OrdersListFragment.ARG_ORDERS_CATEGORY, ORDERS_CATEGORY_AWAITING_COLLECTION);
                break;
            case 1:
                args.putString(OrdersListFragment.ARG_ORDERS_CATEGORY, ORDERS_CATEGORY_DELIVERING);
                break;
            case 2:
                args.putString(OrdersListFragment.ARG_ORDERS_CATEGORY, ORDERS_CATEGORY_DELIVERED);
                break;
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
