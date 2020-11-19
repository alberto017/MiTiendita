package com.example.mitiendita.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Interface.IItemClickListener;
import com.example.mitiendita.Model.ProductModel;
import com.example.mitiendita.R;
import com.example.mitiendita.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    //Inicializacion de variables
    private FrameLayout flProductList;
    private TextView lblTitleProducto;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String categoryID = "";

    private RecyclerView rvProduct;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> adapter;

    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar searchBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = this.getArguments();
        if (data != null) {
            categoryID = data.getString("categoryID");
        }//if
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        //Enlazamos la variable con nuestro componente
        flProductList = view.findViewById(R.id.flProductList);
        lblTitleProducto = view.findViewById(R.id.lblTitleProducto);
        searchBar = view.findViewById(R.id.searchBar);
        rvProduct = view.findViewById(R.id.rvProduct);

        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Food");

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitleProducto.setTypeface(typeface);

        //Cargar platillos
        rvProduct.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(layoutManager);

        cargarProducto(categoryID);

        //Search
        searchBar.setHint("Inserta el producto");

        loadSuggest();
        searchBar.setLastSuggestions(suggestList);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }//beforeTextChanged

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }//if
                }//for
            }//onTextChanged

            @Override
            public void afterTextChanged(Editable editable) {

            }//afterTextChanged
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Restauramos el adaptador cuando la busqueda se cierra
                if (!enabled) {
                    rvProduct.setAdapter(adapter);
                }//if
            }//onSearchStateChanged

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Mostramos resultado cuando la busqueda termina
                startSearch(text);
            }//onSearchConfirmed

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        return view;
    }//onCreateView


    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.product_item,
                ProductViewHolder.class,
                databaseReference.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder productViewHolder, ProductModel productModel, int i) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Espere un momento...");
                progressDialog.show();

                productViewHolder.lblFood_title.setText(productModel.getName());
                Picasso.with(getActivity().getBaseContext()).load(productModel.getImage())
                        .into(productViewHolder.lblFood_image);
                productViewHolder.lblFood_price.setText(productModel.getPrice());
                productViewHolder.lblFood_discount.setText(productModel.getDiscount());
                productViewHolder.lblFood_status.setText(productModel.getStatus());

                //Asignacion de la fuente
                Typeface typeface01 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Regular.otf");
                productViewHolder.lblFood_title.setTypeface(typeface01);
                productViewHolder.lblFood_price.setTypeface(typeface01);
                productViewHolder.lblFood_discount.setTypeface(typeface01);
                productViewHolder.lblFood_status.setTypeface(typeface01);

                //Cambiamos color segun la disponibilidad del restaurante
                if (productModel.getStatus().equals("Disponible")) {
                    productViewHolder.lblFood_image.clearColorFilter();
                    productViewHolder.lblFood_status.setTextColor(Color.GREEN);
                } else {
                    productViewHolder.lblFood_image.setColorFilter(0x88888888, PorterDuff.Mode.MULTIPLY);
                    productViewHolder.lblFood_status.setTextColor(Color.RED);
                }//else

                progressDialog.dismiss();

                final ProductModel producto = productModel;
                productViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        ProductoDetalleFragment productoDetalleFragment = new ProductoDetalleFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("FoodID", searchAdapter.getRef(position).getKey());
                        productoDetalleFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        fragmentTransaction.replace(flProductList.getId(), productoDetalleFragment);
                        fragmentTransaction.commit();
                    }//onClick
                });
            }//populateViewHolder
        };
        rvProduct.setAdapter(searchAdapter);
    }//startSearch

    private void loadSuggest() {

        databaseReference.orderByChild("menuID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ProductModel item = postSnapshot.getValue(ProductModel.class);
                            suggestList.add(item.getName());
                        }//for
                    }//onDataChange

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }//onCancelled
                });

    }//loadSuggest


    private void cargarProducto(String categoryID) {

        //Equivalete a select * from food where MenuID =
        adapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(ProductModel.class, R.layout.product_item, ProductViewHolder.class, databaseReference.orderByChild("menuID").equalTo(categoryID)) {
            @Override
            protected void populateViewHolder(ProductViewHolder productViewHolder, final ProductModel productModel, int i) {
                productViewHolder.lblFood_title.setText(productModel.getName());
                Picasso.with(getActivity().getBaseContext()).load(productModel.getImage())
                        .into(productViewHolder.lblFood_image);
                productViewHolder.lblFood_price.setText(productModel.getPrice());
                productViewHolder.lblFood_discount.setText(productModel.getDiscount());
                productViewHolder.lblFood_status.setText(productModel.getStatus());

                //Asignacion de la fuente
                Typeface typeface01 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Regular.otf");
                productViewHolder.lblFood_title.setTypeface(typeface01);
                productViewHolder.lblFood_price.setTypeface(typeface01);
                productViewHolder.lblTitulo_foodPrice.setTypeface(typeface01);
                productViewHolder.lblFood_discount.setTypeface(typeface01);
                productViewHolder.lblTitulo_foodDiscount.setTypeface(typeface01);
                productViewHolder.lblFood_status.setTypeface(typeface01);

                //Cambiamos color segun la disponibilidad del restaurante
                if (productModel.getStatus().equals("Disponible")) {
                    productViewHolder.lblFood_image.clearColorFilter();
                    productViewHolder.lblFood_status.setTextColor(Color.GREEN);
                } else {
                    productViewHolder.lblFood_image.setColorFilter(0x88888888, PorterDuff.Mode.MULTIPLY);
                    productViewHolder.lblFood_status.setTextColor(Color.RED);
                }//else


                final ProductModel producto = productModel;
                productViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //Validamos la disponibilidad del platillo en evento onClick
                        if (productModel.getStatus().equals("Disponible")) {
                            ProductoDetalleFragment productoDetalleFragment = new ProductoDetalleFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("FoodID", adapter.getRef(position).getKey());
                            productoDetalleFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(flProductList.getId(), productoDetalleFragment);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), "¡Producto no disponible!", Toast.LENGTH_LONG).show();
                        }//else

                    }//onClick
                });
            }//populateViewHolder
        };
        rvProduct.setAdapter(adapter);
    }//cargarPlatillo


    //Llamo fragment de menu de productos
    private void setFrament(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(flProductList.getId(), fragment);
        fragmentTransaction.commit();
    }//setFrament

}//ProductListFragment