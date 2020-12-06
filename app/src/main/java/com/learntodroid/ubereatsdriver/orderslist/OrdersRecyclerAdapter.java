package com.learntodroid.ubereatsdriver.orderslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.ubereatsdriver.R;
import com.learntodroid.ubereatsdriver.sharedmodel.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnOrderProgressionClickListener listener;

    public OrdersRecyclerAdapter(OnOrderProgressionClickListener listener) {
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView itemsRecyclerView;
        private TextView orderStatusTextView, orderDateTimeTextView, orderIdTextView, userIdTextView, totalPriceTextView;
        private Button progressButton;
        private CartRecyclerAdapter cartRecyclerAdapter;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderStatusTextView = itemView.findViewById(R.id.item_order_status);
            orderDateTimeTextView = itemView.findViewById(R.id.item_order_orderdatetime);
            orderIdTextView = itemView.findViewById(R.id.item_order_orderid);
            userIdTextView = itemView.findViewById(R.id.item_order_userid);
            totalPriceTextView = itemView.findViewById(R.id.item_order_totalprice);

            progressButton = itemView.findViewById(R.id.item_order_progressorder);

            itemsRecyclerView = itemView.findViewById(R.id.item_order_items);

            cartRecyclerAdapter = new CartRecyclerAdapter();
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            itemsRecyclerView.setAdapter(cartRecyclerAdapter);
        }

        public void bind(final Order order) {
            orderStatusTextView.setText(String.format("Order %s", order.getStatus().toLowerCase()));
            userIdTextView.setText(String.format("User Id: %s", order.getAccount().getUserId()));
            totalPriceTextView.setText(String.format("Total: %s", NumberFormat.getCurrencyInstance().format(order.getCart().calculatePrices().get(3).getPrice())));
            orderIdTextView.setText(String.format("Order Id: %s", order.getDocumentId()));

            cartRecyclerAdapter.setCartItems(order.getCart().getCartItems());

            switch (order.getStatus()) {
                case "Awaiting Collection":
                    progressButton.setVisibility(View.VISIBLE);
                    progressButton.setText("Take Job");
                    progressButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onOrderReserved(order);
                        }
                    });
                    break;
                case "Delivering":
                    progressButton.setVisibility(View.VISIBLE);
                    progressButton.setText("Mark Delivered");
                    progressButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onOrderDelivered(order);
                        }
                    });
                    break;
                default:
                    progressButton.setVisibility(View.INVISIBLE);
                    progressButton.setOnClickListener(null);
                    break;
            }
        }
    }
}
