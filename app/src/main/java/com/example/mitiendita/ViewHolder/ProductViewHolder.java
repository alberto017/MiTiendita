package com.example.mitiendita.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mitiendita.Interface.IItemClickListener;
import com.example.mitiendita.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView lblFood_title;
    public ImageView lblFood_image;
    public TextView lblFood_price;
    public TextView lblTitulo_foodPrice;
    public TextView lblFood_discount;
    public TextView lblTitulo_foodDiscount;
    public TextView lblFood_status;

    private IItemClickListener iItemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        lblFood_title = itemView.findViewById(R.id.food_title);
        lblFood_image = itemView.findViewById(R.id.food_image);
        lblFood_price = itemView.findViewById(R.id.food_price);
        lblTitulo_foodPrice = itemView.findViewById(R.id.lblTitulo_foodPrice);
        lblFood_discount = itemView.findViewById(R.id.food_discount);
        lblTitulo_foodDiscount = itemView.findViewById(R.id.lblTitulo_foodDiscount);
        lblFood_status = itemView.findViewById(R.id.food_status);

        itemView.setOnClickListener(this);
    }//PlatillosViewHolder

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.iItemClickListener = itemClickListener;
    }//setItemClickListener

    @Override
    public void onClick(View view) {
        iItemClickListener.onClick(view, getAdapterPosition(), false);
    }//onClick
}//ProductViewHolder
