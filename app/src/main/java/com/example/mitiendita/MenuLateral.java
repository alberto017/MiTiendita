package com.example.mitiendita;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Model.Token;
import com.example.mitiendita.Model.UsuarioModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

public class MenuLateral extends AppCompatActivity {

    //Declaracion de variables
    private AppBarConfiguration mAppBarConfiguration;
    private TextView lblUserName_Header;
    private long backPressedTime;
    private Toast backToast;

    //Instancias firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Asignar usuario
        View headerView = navigationView.getHeaderView(0);
        lblUserName_Header = (TextView) headerView.findViewById(R.id.lblUserName_Header);
        lblUserName_Header.setText(Common.currentUsuarioModel.getName());

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        //Iniciamos Paper para almacenamiento de variables
        Paper.init(this);

        /*
        UsuarioModel usuarioModel = new UsuarioModel(Common.currentUsuarioModel.getName(),
                Common.currentUsuarioModel.getPhone(),
                Common.currentUsuarioModel.getPassword(),
                "desactivado",
                Common.currentUsuarioModel.getSecureCode(),
                Common.currentUsuarioModel.getLocation(),
                "Cliente",
                Common.currentUsuarioModel.getReference());
        //UsuarioModel usuarioModel = new UsuarioModel(Common.currentUsuarioModel.getName(), Common.currentUsuarioModel.getPhone(), Common.currentUsuarioModel.getPassword(), "desactivado", Common.currentUsuarioModel.getSecureCode());
        databaseReference.child(Common.currentUsuarioModel.getPhone()).setValue(usuarioModel);
        */

        //Cambiamos el status del sliderWalkthrough
        /*
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsuarioModel usuarioModel = new UsuarioModel(Common.currentUsuarioModel.getName(),
                        Common.currentUsuarioModel.getPhone(),
                        Common.currentUsuarioModel.getPassword(),
                        "desactivado",
                        Common.currentUsuarioModel.getSecureCode(),
                        Common.currentUsuarioModel.getLocation(),
                        Common.currentUsuarioModel.getUserType(),
                        Common.currentUsuarioModel.getReference());
                databaseReference.child(Common.currentUsuarioModel.getPhone()).setValue(usuarioModel);
            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }//onCancelled
        });
         */

        //databaseReference.child(Common.currentUsuarioModel.getPhone()).child("sliderStatus").setValue("desactivado");

        /*
        HashMap hashMap = new HashMap();
        String sliderStatus = "desactivado";
        hashMap.put("sliderStatus",sliderStatus);
        databaseReference.child(Common.currentUsuarioModel.getPhone()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MenuLateral.this,"Introduccion desactivada..",Toast.LENGTH_LONG).show();
            }//onSuccess
        });
         */

        /*
        Notificacion
        updateToken(FirebaseInstanceId.getInstance().getToken());
        */

    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salir, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                Intent actionExit = new Intent(getApplicationContext(), SignIn.class);
                actionExit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(actionExit);
                //Destruimos almacenamiento de Paper
                Paper.book().destroy();
        }//switch
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }//onSupportNavigateUp

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.show();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(MenuLateral.this, "¡Presiona nuevamente para salir!", Toast.LENGTH_SHORT);
            backToast.show();
        }//else
        backPressedTime = System.currentTimeMillis();
    }//onBackPressed

    private void updateToken(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokens = database.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(Common.currentUsuarioModel.getPhone()).setValue(data);
    }//updateTokenToFirebase

}//MenuLateral