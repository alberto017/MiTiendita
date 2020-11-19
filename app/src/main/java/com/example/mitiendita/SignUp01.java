package com.example.mitiendita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Model.UsuarioModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp01 extends AppCompatActivity {

    //Declaracion de variables
    private TextView lblTitle;
    private EditText edtName;
    private EditText edtPhone;
    private Spinner spnStatus;
    private EditText edtPassword;
    private EditText edtSecureCode;
    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up01);

        //Enlazar controladores
        lblTitle = findViewById(R.id.lblTitle_signUp);
        edtName = findViewById(R.id.edtUser_signUp01);
        edtPhone = findViewById(R.id.edtPhone_signUp01);
        spnStatus = findViewById(R.id.spnStatus01);
        edtSecureCode = findViewById(R.id.edtSecureCode);
        edtPassword = findViewById(R.id.edtPassword_signUp01);
        btnSignUp = findViewById(R.id.btnSignUp_signUp01);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitle.setTypeface(typeface);

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        // Lista de elementos que se pondrán en el spinner
        String[] categorias = {"Hogar", "Hotel", "Restaurante"};
        spnStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog progressDialog = new ProgressDialog(SignUp01.this);
                    progressDialog.setMessage("Espere un momento...");
                    progressDialog.show();

                    Intent intent = new Intent(SignUp01.this, SignUp02.class);
                    intent.putExtra("usuario", edtName.getText().toString());
                    intent.putExtra("telefono", edtPhone.getText().toString());
                    intent.putExtra("contrasena", edtPassword.getText().toString());
                    intent.putExtra("introduccion", "activado");
                    intent.putExtra("status", spnStatus.getSelectedItem().toString());
                    intent.putExtra("codigo", edtSecureCode.getText().toString());
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(SignUp01.this, "¡Revisa tu Conexion a Internet!", Toast.LENGTH_LONG).show();
                    return;
                }//else
            }//onClick
        });
    }//onCreate

    /*
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }//onSupportNavigateUp
     */

}//SignUp