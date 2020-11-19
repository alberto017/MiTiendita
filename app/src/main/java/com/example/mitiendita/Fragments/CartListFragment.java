package com.example.mitiendita.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Database.Database;
import com.example.mitiendita.Helper.RecyclerItemTouchHelper;
import com.example.mitiendita.Inicio;
import com.example.mitiendita.Interface.RecyclerItemTouchHelperListener;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Model.SolicitudModel;
import com.example.mitiendita.Model.UsuarioModel;
import com.example.mitiendita.R;
import com.example.mitiendita.Remote.IAPIService;
import com.example.mitiendita.Reservacion;
import com.example.mitiendita.SignUp02;
import com.example.mitiendita.ViewHolder.CartAdapter;
import com.example.mitiendita.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartListFragment extends Fragment implements RecyclerItemTouchHelperListener {

    //Declaracion de variables
    private FrameLayout flCart;
    private TextView lblCardTotal;
    private TextView cart_total;
    private FButton btnCardAdd;
    private RecyclerView rvCart;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<CartModel> orderModelList;
    private CartAdapter cartAdapter;
    private CartModel carritoModel;
    private SolicitudModel solicitudModel;
    private String total = "";
    private IAPIService mService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);

        flCart = view.findViewById(R.id.flCart);
        rvCart = view.findViewById(R.id.rvCart);
        lblCardTotal = view.findViewById(R.id.lblCardTotal);
        cart_total = view.findViewById(R.id.cart_total);
        btnCardAdd = view.findViewById(R.id.btnCardAdd);

        rvCart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvCart.setLayoutManager(layoutManager);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        cart_total.setTypeface(typeface);
        lblCardTotal.setTypeface(typeface);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Request");

        //Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvCart);

        //Inicializar Servicio
         /*
        mService = Common.getFCMService();
         */

        loadListFood();

        return view;
    }//onCreateView

    private void loadListFood() {
        orderModelList = new Database(getActivity()).getCart(Common.currentUsuarioModel.getPhone());
        cartAdapter = new CartAdapter(orderModelList, getActivity());
        rvCart.setAdapter(cartAdapter);

        for (CartModel carritoModel : orderModelList) {
            total += (Integer.parseInt(carritoModel.getPrice())) * (Integer.parseInt(carritoModel.getQuantity()));
            Locale locale = new Locale("es", "US");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            //lblCardTotal.setText(numberFormat.format(total));
            lblCardTotal.setText(total);
        }//for

    }//loadListFood

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderModelList.size() > 0) {

                    SolicitudModel solicitudModel = new SolicitudModel(Common.currentUsuarioModel.getPhone(),
                            Common.currentUsuarioModel.getName(),
                            "ubicacion",
                            total,
                            "comentario",
                            "fecha",
                            orderModelList);

                    //Asignamos currentMillis como nuestra key en carrito
                    String order_number = String.valueOf(System.currentTimeMillis());

                    //Llenamos el constructor y cargamos los datos en la BD
                    databaseReference.child(order_number)
                            .setValue(solicitudModel);

                    //Eliminamos carrito
                    new Database(getActivity()).clearCart(Common.currentUsuarioModel.getPhone());

                    //Llamamos nuestro actividad y pasamos el total a la actividad

                    Intent reservacion = new Intent(getActivity(), Reservacion.class);
                    reservacion.putExtra("orderNumber", order_number);
                    startActivity(reservacion);
                    getActivity().finish();


                } else {
                    Toast.makeText(getActivity(), "¡El carrito se encuentra vacio!", Toast.LENGTH_SHORT).show();
                }//else
            }//onClick
        });

    }//onViewCreated

    /*
    private void sendNotificationOrder(String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    //Creacion de playload send
                    Notification notification = new Notification("Inviatto", "Tienes una nueva Orden");
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(getActivity(), "¡Orden realizada con exito!", Toast.LENGTH_LONG).show();
                                            //getActivity().finish();
                                        } else {
                                            Toast.makeText(getActivity(), "¡Ocurio un error!", Toast.LENGTH_LONG).show();
                                        }//else
                                    }//if
                                }//onResponse

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }//onFailure
                            });
                }//for
            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//sendNotificationOrder
     */

    @Override
    public void onswiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        int total = 0;
        int deleteProduct = 0;
        NumberFormat numberFormat = null;

        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) rvCart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final CartModel deleteItem = ((CartAdapter) rvCart.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            cartAdapter.removeItem(deleteIndex);
            //deleteProduct += (Integer.parseInt(deleteItem.getPrice()));
            new Database(getActivity().getBaseContext()).removeFromCart(deleteItem.getProductID(), Common.currentUsuarioModel.getPhone());
            Toast.makeText(getActivity(), "Costo del articulo eliminado " + deleteProduct, Toast.LENGTH_LONG).show();

            List<CartModel> carritoModels = new Database(getActivity().getBaseContext()).getCart(Common.currentUsuarioModel.getPhone());
            for (CartModel item : carritoModels)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("en", "US");
            numberFormat = NumberFormat.getCurrencyInstance(locale);

            lblCardTotal.setText(numberFormat.format(total));

            Snackbar snackbar = Snackbar.make(flCart, name + " ¡Eliminado del carrito! ", Snackbar.LENGTH_LONG);
            snackbar.setAction("DESHACER", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getActivity().getBaseContext()).addToCart(deleteItem);

                    int total = 0;
                    List<CartModel> carritoModels = new Database(getActivity().getBaseContext()).getCart(Common.currentUsuarioModel.getPhone());
                    for (CartModel item : carritoModels)
                        total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en", "US");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                    lblCardTotal.setText(numberFormat.format(total));
                }//onClick
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }//if
        //lblCardTotal.setText(numberFormat.format(total - deleteProduct));
    }//onswiped

}//ubicacionAutomatica