package com.learntodroid.ubereatsdriver.orderslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.learntodroid.ubereatsdriver.R;

import static com.learntodroid.ubereatsdriver.orderslist.OrdersFragmentStateAdapter.ORDERS_CATEGORY_AWAITING_COLLECTION;
import static com.learntodroid.ubereatsdriver.orderslist.OrdersFragmentStateAdapter.ORDERS_CATEGORY_DELIVERED;
import static com.learntodroid.ubereatsdriver.orderslist.OrdersFragmentStateAdapter.ORDERS_CATEGORY_DELIVERING;

public class OrdersFragment extends Fragment {
    private OrdersFragmentStateAdapter ordersFragmentStateAdapter;
    private ViewPager2 viewPager;
    private OrdersListViewModel ordersListViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersListViewModel = new ViewModelProvider(this).get(OrdersListViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ordersFragmentStateAdapter = new OrdersFragmentStateAdapter(this);
        viewPager = view.findViewById(R.id.fragment_orders_viewpager);
        viewPager.setAdapter(ordersFragmentStateAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        ordersListViewModel.queryOrders(ORDERS_CATEGORY_AWAITING_COLLECTION);
                        break;
                    case 1:
                        ordersListViewModel.queryOrders(ORDERS_CATEGORY_DELIVERING);
                        break;
                    case 2:
                        ordersListViewModel.queryOrders(ORDERS_CATEGORY_DELIVERED);
                        break;
                }
            }
        });

        TabLayout tabLayout = view.findViewById(R.id.fragment_orders_tablayout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(ORDERS_CATEGORY_AWAITING_COLLECTION);
                        break;
                    case 1:
                        tab.setText(ORDERS_CATEGORY_DELIVERING);
                        break;
                    case 2:
                        tab.setText(ORDERS_CATEGORY_DELIVERED);
                        break;
                }
            }
        }).attach();
    }
}
