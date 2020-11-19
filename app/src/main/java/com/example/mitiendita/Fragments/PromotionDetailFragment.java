package com.example.mitiendita.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mitiendita.Database.Database;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Model.PromotionModel;
import com.example.mitiendita.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class PromotionDetailFragment extends Fragment {

    //Declaracion de variables
    private ImageView imgPromocionDetalle;
    private TextView lblNombreDetalle;
    private TextView lblPrecioDetalle;
    private TextView lblRestauranteDetalle;
    private TextView lblIngredientesDetalle;
    private TextView lblLimiteDetalle;
    private TextView lblTituloCantidadDetalle;
    private TextView lblTituloPrecioDetalle;
    private TextView lblTituloRestauranteDetalle;
    private TextView lblTituloIngredientesDetalle;
    private TextView lblTituloLimiteDetalle;

    private ElegantNumberButton spinnerCantidadDetalle;
    private FloatingActionButton btnAgregarDetalle;
    private CollapsingToolbarLayout collapsingDetalle;
    private AppBarLayout app_bar_layout;
    private String promocionID = "";
    String SpinnerValue = "";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    PromotionModel promocionModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = this.getArguments();
        if (data != null) {
            promocionID = data.getString("PromotionID");
            //Toast.makeText(getActivity(),platilloID,Toast.LENGTH_LONG).show();
        }//if
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promotion_detail, container, false);

        collapsingDetalle = view.findViewById(R.id.collapsingDetalle);
        app_bar_layout = view.findViewById(R.id.app_bar_layout);
        imgPromocionDetalle = view.findViewById(R.id.imgPromocionDetalle);

        lblTituloCantidadDetalle = view.findViewById(R.id.lblTituloCantidadDetalle);
        lblTituloRestauranteDetalle = view.findViewById(R.id.lblTituloRestauranteDetalle);
        lblTituloPrecioDetalle = view.findViewById(R.id.lblTituloPrecioDetalle);
        lblTituloIngredientesDetalle = view.findViewById(R.id.lblTituloIngredientesDetalle);
        lblTituloLimiteDetalle = view.findViewById(R.id.lblTituloLimiteDetalle);

        lblNombreDetalle = view.findViewById(R.id.lblNombreDetalle);
        lblPrecioDetalle = view.findViewById(R.id.lblPrecioDetalle);
        lblRestauranteDetalle = view.findViewById(R.id.lblRestauranteDetalle);
        lblIngredientesDetalle = view.findViewById(R.id.lblIngredientesDetalle);
        lblLimiteDetalle = view.findViewById(R.id.lblLimiteDetalle);

        spinnerCantidadDetalle = view.findViewById(R.id.spinnerCantidadDetalle);
        btnAgregarDetalle = view.findViewById(R.id.btnAgregarDetalle);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblNombreDetalle.setTypeface(typeface);

        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Promotion");

        cargarDetalle(promocionID);

        return view;
    }//onCreateView


    private void cargarDetalle(String platilloID) {
        databaseReference.child(platilloID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                promocionModel = dataSnapshot.getValue(PromotionModel.class);

                //Set Image
                Picasso.with(getActivity().getBaseContext()).load(promocionModel.getImage())
                        .into(imgPromocionDetalle);

                //collapsingDetalle.setTitle(promocionModel.getName());
                lblNombreDetalle.setText(promocionModel.getTitle());
                lblPrecioDetalle.setText(promocionModel.getPrice());
                lblRestauranteDetalle.setText(promocionModel.getRestaurantID());
                lblLimiteDetalle.setText(promocionModel.getDateEnd());
            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }//cargarDetalle

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lblIngredientesDetalle.setText(promocionID);

        btnAgregarDetalle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Database(getActivity().getBaseContext()).addToCart(new CartModel(
                        promocionID,
                        promocionModel.getTitle(),
                        spinnerCantidadDetalle.getNumber(),
                        promocionModel.getPrice(),
                        promocionModel.getImage()
                ));
                Toast.makeText(getActivity(), "Agregado al carrito", Toast.LENGTH_LONG).show();
            }//onClick
        });
    }//onViewCreated

}//PromotionDetailFragment