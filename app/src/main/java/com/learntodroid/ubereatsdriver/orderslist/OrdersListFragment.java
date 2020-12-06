package com.learntodroid.ubereatsdriver.orderslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.ubereatsdriver.R;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

import java.util.List;

public class OrdersListFragment extends Fragment implements OnOrderProgressionClickListener {
    public static final String ARG_ORDERS_CATEGORY = "ARG_ORDERS_CATEGORY";

    private OrdersListViewModel ordersListViewModel;
    private OrdersRecyclerAdapter ordersRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderslist, container, false);

        ordersListViewModel = new ViewModelProvider(this).get(OrdersListViewModel.class);

        ordersRecyclerAdapter = new OrdersRecyclerAdapter(this);

        RecyclerView ordersRecyclerView = view.findViewById(R.id.fragment_orderslist_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(ordersRecyclerAdapter);

        ordersListViewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null) {
                    ordersRecyclerAdapter.setOrders(orders);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        ((TextView) view.findViewById(R.id.fragment_orderslist_type)).setText(String.format("%s Orders", args.getString(ARG_ORDERS_CATEGORY)));
    }

    @Override
    public void onOrderReserved(Order order) {
        ordersListViewModel.reserveOrder(order);
        Navigation.findNavController(getView()).navigate(R.id.action_ordersFragment_to_restaurantCollectionFragment);
    }

    @Override
    public void onOrderDelivered(Order order) {
        ordersListViewModel.updateOrderStatus(order, "Delivered");
    }
}
