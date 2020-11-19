package com.example.mitiendita.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<CartModel> orderModelList = new ArrayList<>();
    private Context context;

    public CartAdapter(List<CartModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }//CartAdapter

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }//onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Picasso.with(context.getApplicationContext())
                .load(orderModelList.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + orderModelList.get(position).getQuantity(), Color.RED);
        holder.imgCardItemCount.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt((orderModelList.get(position).getPrice())) * (Integer.parseInt(orderModelList.get(position).getQuantity())));
        holder.lblCardItemPrice.setText(numberFormat.format(price));
        holder.lblCardItemName.setText(orderModelList.get(position).getProductName());
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }//getItemCount

    public CartModel getItem(int position){
        return orderModelList.get(position);
    }//getItem

    public void removeItem(int position){
        orderModelList.remove(position);
        notifyItemRemoved(position);
    }//removeItem

    public void restoreItem(CartModel item,int position){
        orderModelList.add(position,item);
        notifyItemInserted(position);
    }//restoreItem

}//CartAdapter

