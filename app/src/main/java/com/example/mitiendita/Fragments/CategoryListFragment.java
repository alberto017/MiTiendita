package com.example.mitiendita.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Interface.IItemClickListener;
import com.example.mitiendita.Model.CategoriaModel;
import com.example.mitiendita.R;
import com.example.mitiendita.ViewHolder.CategoriaViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class CategoryListFragment extends Fragment {

    //Inicializacion de variables
    private FrameLayout flCategoriaLista;
    private TextView lblTituloCategoria;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private RecyclerView rvCategoria;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<CategoriaModel, CategoriaViewHolder> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        //Enlazamos los componente
        flCategoriaLista = view.findViewById(R.id.flCategoriaLista);
        lblTituloCategoria = view.findViewById(R.id.lblTituloCategoria);
        rvCategoria = view.findViewById(R.id.rvCategoria);

        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblTituloCategoria.setTypeface(typeface);

        //Cargar categorias
        rvCategoria.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvCategoria.setLayoutManager(layoutManager);
        cargarCategoria();

        return view;
    }//onCreateView


    private void cargarCategoria() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Espere un momento...");
        progressDialog.show();

        adapter = new FirebaseRecyclerAdapter<CategoriaModel, CategoriaViewHolder>(CategoriaModel.class, R.layout.category_item, CategoriaViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CategoriaViewHolder menuViewHolder, CategoriaModel categoriaModel, int i) {
                menuViewHolder.lblCategory_title.setText(categoriaModel.getName());
                Picasso.with(getActivity().getBaseContext()).load(categoriaModel.getImage())
                        .into(menuViewHolder.lblCategory_image);

                progressDialog.dismiss();
                
                //Asignacion de la fuente
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
                menuViewHolder.lblCategory_title.setTypeface(typeface);

                final CategoriaModel clickItem = categoriaModel;

                menuViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClik) {

                        ProductListFragment productListFragment = new ProductListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryID", adapter.getRef(position).getKey());
                        productListFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(flCategoriaLista.getId(), productListFragment);
                        fragmentTransaction.commit();

                    }//onClick
                });
            }//populateViewHolder
        };
        rvCategoria.setAdapter(adapter);
    }//cargarCategoria

}//CategoryListFragment