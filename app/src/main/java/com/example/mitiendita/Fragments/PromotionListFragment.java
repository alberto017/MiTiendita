package com.example.mitiendita.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Interface.IItemClickListener;
import com.example.mitiendita.Model.PromotionModel;
import com.example.mitiendita.R;
import com.example.mitiendita.ViewHolder.PromotionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class PromotionListFragment extends Fragment {

    //Inicializacion de variables
    private FrameLayout flPromociones;
    private TextView lblTituloPromocion;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView lblCategoryID;
    private String promotionID = "";

    private RecyclerView rvPromocion;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<PromotionModel, PromotionViewHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_list, container, false);

        flPromociones = view.findViewById(R.id.flPromociones);
        lblTituloPromocion = view.findViewById(R.id.lblTituloPromocion);
        rvPromocion = view.findViewById(R.id.rvPromocion);

        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Promotion");

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblTituloPromocion.setTypeface(typeface);

        //Cargar platillos
        rvPromocion.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvPromocion.setLayoutManager(layoutManager);
        cargarPromocion();

        return view;
    }//onCreateView


    private void cargarPromocion() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Espere un momento...");
        progressDialog.show();

        //Equivalete a select * from food where MenuID =
        adapter = new FirebaseRecyclerAdapter<PromotionModel, PromotionViewHolder>(PromotionModel.class, R.layout.promotion_item, PromotionViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(PromotionViewHolder promocionViewHolder, final PromotionModel promocionModel, int i) {
                promocionViewHolder.lblPromotion_Title.setText(promocionModel.getTitle());
                Picasso.with(getActivity().getBaseContext()).load(promocionModel.getImage())
                        .into(promocionViewHolder.ImgPromotion_image);
                promocionViewHolder.lblPromotion_RestaurantName.setText(promocionModel.getRestaurantName());
                promocionViewHolder.lblPromotion_price.setText(promocionModel.getPrice());
                promocionViewHolder.lblPromotion_DateEnd.setText(promocionModel.getDateEnd());

                //Cambiamos color segun la disponibilidad de promocion
                if (promocionModel.getStatus().equals("Disponible")) {
                    promocionViewHolder.ImgPromotion_image.clearColorFilter();
                    promocionViewHolder.lblPromotion_Title.setTextColor(Color.YELLOW);
                } else {
                    promocionViewHolder.ImgPromotion_image.setColorFilter(0x88888888, PorterDuff.Mode.MULTIPLY);
                    promocionViewHolder.lblPromotion_Title.setTextColor(Color.DKGRAY);
                }//else

                progressDialog.dismiss();

                //Asignacion de la fuente
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
                promocionViewHolder.lblPromotion_Title.setTypeface(typeface);

                final PromotionModel clickItem = promocionModel;
                promocionViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //Validamos la disponibilidad del platillo en evento onClick
                        if (promocionModel.getStatus().equals("Disponible")) {
                            PromotionDetailFragment promotionDetailFragment = new PromotionDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("PromotionID", adapter.getRef(position).getKey());
                            promotionDetailFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(flPromociones.getId(), promotionDetailFragment);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(),"¡Promocion no disponible!",Toast.LENGTH_LONG).show();
                        }//else

                    }//onClick
                });
            }//populateViewHolder
        };
        rvPromocion.setAdapter(adapter);
    }//cargarPromocion

}//PromotionListFragment