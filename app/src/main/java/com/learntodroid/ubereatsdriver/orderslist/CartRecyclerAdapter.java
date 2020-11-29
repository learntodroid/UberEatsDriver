package com.learntodroid.ubereatsdriver.orderslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.ubereatsdriver.R;
import com.learntodroid.ubereatsdriver.sharedmodel.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartItemViewHolder> {
    private List<CartItem> cartItems;

    public CartRecyclerAdapter() {
        cartItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartitem, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView quantity, title, subtitle, price;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            quantity = itemView.findViewById(R.id.item_cartitem_quantity);
            title = itemView.findViewById(R.id.item_cartitem_title);
            subtitle = itemView.findViewById(R.id.item_cartitem_subtitle);
            price = itemView.findViewById(R.id.item_cartitem_price);
        }

        public void bind(CartItem cartItem) {
            quantity.setText(String.format("×%d", cartItem.getQuantity()));
            title.setText(cartItem.getMenuItem().getTitle());
            subtitle.setText(cartItem.getNotes());
            price.setText(NumberFormat.getCurrencyInstance().format(cartItem.getMenuItem().getPrice() * cartItem.getQuantity()));
        }
    }
}
