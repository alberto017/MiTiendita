package com.example.mitiendita.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mitiendita.Interface.IItemClickListener;
import com.example.mitiendita.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{

    public TextView lblCardItemName;
    public TextView lblCardItemPrice;
    public ImageView imgCardItemCount;
    public ImageView cart_image;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    private IItemClickListener iItemClickListener;

    public void setCartName(TextView lblCartName) {
        this.lblCardItemName = lblCartName;
    }//setCartName

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        lblCardItemName = itemView.findViewById(R.id.lblCartItemName);
        lblCardItemPrice = itemView.findViewById(R.id.lblCartItemPrice);
        imgCardItemCount = itemView.findViewById(R.id.imgCartItemCount);
        cart_image = itemView.findViewById(R.id.imgCartImage);

        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);
    }//CartViewHolder

    @Override
    public void onClick(View view) {

    }//onClick

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }//onCreateContextMenu
}//CartViewHolder
