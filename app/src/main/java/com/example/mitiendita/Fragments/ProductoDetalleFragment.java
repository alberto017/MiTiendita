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
import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Database.Database;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Model.ProductModel;
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

public class ProductoDetalleFragment extends Fragment {

    //Declaracion de variables
    private ImageView imgFoodDetalle;
    private TextView lblNombreDetalle;
    private TextView lblPrecioDetalle;
    private TextView lblDescuentoDetalle;
    private TextView lblIngredientesDetalle;
    private TextView lblTituloCantidadDetalle;

    private TextView lblTituloPrecioDetalle;
    private TextView lblTituloDescuentoDetalle;
    private TextView lblTituloIngredientesDetalle;

    private ElegantNumberButton spinnerCantidadDetalle;
    private FloatingActionButton btnAgregarDetalle;
    private CollapsingToolbarLayout collapsingDetalle;
    private AppBarLayout app_bar_layout;
    private String platilloID = "";
    String SpinnerValue = "";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ProductModel productModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = this.getArguments();
        if (data != null) {
            platilloID = data.getString("FoodID");
        }//if
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_producto_detalle, container, false);

        collapsingDetalle = view.findViewById(R.id.collapsingDetalle);
        app_bar_layout = view.findViewById(R.id.app_bar_layout);
        imgFoodDetalle = view.findViewById(R.id.imgFoodDetalle);

        lblTituloPrecioDetalle = view.findViewById(R.id.lblTituloPrecioDetalle);
        lblTituloDescuentoDetalle = view.findViewById(R.id.lblTituloDescuentoDetalle);
        lblTituloIngredientesDetalle = view.findViewById(R.id.lblTituloIngredientesDetalle);
        lblTituloCantidadDetalle = view.findViewById(R.id.lblTituloCantidadDetalle);

        lblNombreDetalle = view.findViewById(R.id.lblNombreDetalle);
        lblPrecioDetalle = view.findViewById(R.id.lblPrecioDetalle);
        lblDescuentoDetalle = view.findViewById(R.id.lblDescuentoDetalle);
        lblIngredientesDetalle = view.findViewById(R.id.lblIngredientesDetalle);

        spinnerCantidadDetalle = view.findViewById(R.id.spinnerCantidadDetalle);
        btnAgregarDetalle = view.findViewById(R.id.btnAgregarDetalle);

        //Asignacion de la fuente
        Typeface typeface01 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Regular.otf");
        lblDescuentoDetalle.setTypeface(typeface01);
        lblIngredientesDetalle.setTypeface(typeface01);
        lblPrecioDetalle.setTypeface(typeface01);
        lblDescuentoDetalle.setTypeface(typeface01);

        lblTituloCantidadDetalle.setTypeface(typeface01);
        lblTituloIngredientesDetalle.setTypeface(typeface01);
        lblTituloPrecioDetalle.setTypeface(typeface01);
        lblTituloDescuentoDetalle.setTypeface(typeface01);

        //Asignacion de la fuente
        Typeface typeface02 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblNombreDetalle.setTypeface(typeface02);


        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Food");

        cargarDetalle(platilloID);

        return view;
    }//onCreateView


    private void cargarDetalle(String platilloID) {
        databaseReference.child(platilloID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModel = dataSnapshot.getValue(ProductModel.class);

                //Set Image
                Picasso.with(getActivity().getBaseContext()).load(productModel.getImage())
                        .into(imgFoodDetalle);

                collapsingDetalle.setTitle(productModel.getName());
                lblNombreDetalle.setText(productModel.getName());
                lblPrecioDetalle.setText(productModel.getPrice());
                lblDescuentoDetalle.setText(productModel.getDiscount());
                lblIngredientesDetalle.setText(productModel.getDescription());
            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }//cargarDetalle

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lblIngredientesDetalle.setText(platilloID);

        btnAgregarDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Database(getActivity().getBaseContext()).addToCart(new CartModel(
                        platilloID,
                        productModel.getName(),
                        spinnerCantidadDetalle.getNumber(),
                        productModel.getPrice(),
                        productModel.getDiscount(),
                        productModel.getImage(),
                        Common.currentUsuarioModel.getPhone()
                ));

                Toast.makeText(getActivity(), "Agregado al carrito", Toast.LENGTH_LONG).show();
            }//onClick
        });
    }//onViewCreated
}//ProductoDetalleFragment