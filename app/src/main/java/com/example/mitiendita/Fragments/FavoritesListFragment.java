package com.example.mitiendita.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mitiendita.R;

public class FavoritesListFragment extends Fragment {

    //Inicializacion de variables
    private TextView lblTitleFavoritos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        //Enlazamos la variable con nuestro componente
        lblTitleFavoritos = view.findViewById(R.id.lblTitleFavoritos);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitleFavoritos.setTypeface(typeface);

        return view;
    }//onCreateView
}//FavoritesListFragment