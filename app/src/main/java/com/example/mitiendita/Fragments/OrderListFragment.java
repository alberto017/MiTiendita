package com.example.mitiendita.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Model.SolicitudModel;
import com.example.mitiendita.R;
import com.example.mitiendita.Remote.IAPIService;
import com.example.mitiendita.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class OrderListFragment extends Fragment {

    //Inicializacion de variables
    public RecyclerView rvOrden;
    public RecyclerView.LayoutManager layoutManager;

    //Agregar layout de menu
    private MaterialEditText edtCategoryName;
    private FrameLayout flOrderLista;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<SolicitudModel, OrderViewHolder> adapter;
    private IAPIService mService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        flOrderLista = view.findViewById(R.id.flOrderLista);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Request");

        rvOrden = view.findViewById(R.id.rvOrden);
        rvOrden.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvOrden.setLayoutManager(layoutManager);

        //Inicializar servicio
        mService = Common.getFCMClient();

        cargarOrdenes();

        return view;
    }//onCreateView

    private void cargarOrdenes() {
        adapter = new FirebaseRecyclerAdapter<SolicitudModel, OrderViewHolder>(
                SolicitudModel.class,
                R.layout.order_item,
                OrderViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final OrderViewHolder orderViewHolder, final SolicitudModel solicitudModel, final int position) {
                orderViewHolder.lblOrderItemName.setText(adapter.getRef(position).getKey());
                orderViewHolder.lblOrderItemStatus.setText(Common.convertCodeToStatus(solicitudModel.getStatus()));
                orderViewHolder.lblOrderItemAddress.setText(solicitudModel.getAddress());
                orderViewHolder.lblOrderItemPhone.setText(solicitudModel.getPhone());
                orderViewHolder.lblOrderItemDate.setText(solicitudModel.getDate());

                //Evento Detalle Orden
                orderViewHolder.btnOrderDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                        Common.currentSolicitudModel = solicitudModel;
                        Bundle bundle = new Bundle();
                        bundle.putString("orderID", adapter.getRef(position).getKey());
                        orderDetailFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(flOrderLista.getId(), orderDetailFragment);
                        fragmentTransaction.commit();
                    }//onClick
                });

            }//populateViewHolder
        };
        adapter.notifyDataSetChanged();
        rvOrden.setAdapter(adapter);
    }//loadOrdenes

}//OrderListFragment