package com.example.mitiendita.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView quantity;
    public TextView price;
    public TextView discount;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.lblOrderDetailItemName);
        quantity = itemView.findViewById(R.id.lblOrderDetailItemQuantity);
        price = itemView.findViewById(R.id.lblOrderDetailItemPrice);
        discount = itemView.findViewById(R.id.lblOrderDetailItemDiscount);
    }//MyViewHolder

}//MyViewHolder

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<CartModel> myOrders;

    public OrderDetailAdapter(List<CartModel> myOrders) {
        this.myOrders = myOrders;
    }//OrderDetailAdapter

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(view);
    }//MyViewHolder

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartModel carritoModel = myOrders.get(position);
        holder.name.setText(String.format("Name : %s",carritoModel.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s",carritoModel.getQuantity()));
        holder.price.setText(String.format("Price : %s",carritoModel.getPrice()));
        holder.discount.setText(String.format("Discount : %s",carritoModel.getDiscount()));
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return myOrders.size();
    }//getItemCount
}//OrderDetailAdapter
