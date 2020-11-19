package com.example.mitiendita.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.R;
import com.example.mitiendita.ViewHolder.OrderDetailAdapter;

public class OrderDetailFragment extends Fragment {

    //Declaracion de variables
    private TextView order_id;
    private TextView order_phone;
    private TextView order_address;
    private TextView order_total;
    private TextView order_comment;
    private String orderID = "";
    private RecyclerView rvFoodDetail;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = this.getArguments();
        if (data != null) {
            orderID = data.getString("orderID");
        }//if
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        rvFoodDetail = view.findViewById(R.id.rvFoodDetail);

        order_id = view.findViewById(R.id.lblOrderDetailID);
        order_phone = view.findViewById(R.id.lblOrderDetailPhone);
        order_address = view.findViewById(R.id.lblOrderDetailAddress);
        order_total = view.findViewById(R.id.lblOrderDetailTotal);
        order_comment = view.findViewById(R.id.lblOrderDetailComment);

        order_id.setText(orderID);
        order_phone.setText(Common.currentSolicitudModel.getPhone());
        order_total.setText(Common.currentSolicitudModel.getTotal());
        order_address.setText(Common.currentSolicitudModel.getAddress());
        order_comment.setText(Common.currentSolicitudModel.getComment());

        rvFoodDetail.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvFoodDetail.setLayoutManager(layoutManager);

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentSolicitudModel.getCarritoModelList());
        adapter.notifyDataSetChanged();
        rvFoodDetail.setAdapter(adapter);

        return view;
    }//onCreateView
}//OrderDetailFragment