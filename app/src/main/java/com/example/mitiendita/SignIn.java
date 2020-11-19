package com.example.mitiendita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Model.UsuarioModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    //Declaracion de variables
    private ImageView imgSignIn_signIn;
    private TextView lblTitle;
    private EditText edtPhone;
    private EditText edtPassword;
    private CheckBox cbSignIn;
    private Button btnSignIn;
    private TextView lblRecovery;
    private TextView lblHelp;
    private DatabaseReference databaseReference = null;
    private FirebaseDatabase firebaseDatabase;
    String latlng;
    UsuarioModel usuarioModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Referencia de controladores
        lblTitle = findViewById(R.id.lblTitle_signIn);
        edtPhone = findViewById(R.id.edtPhone_signIn);
        edtPassword = findViewById(R.id.edtPassword_signIn);
        cbSignIn = findViewById(R.id.cbSignIn);
        btnSignIn = findViewById(R.id.btnEnter_signIn);
        lblHelp = findViewById(R.id.lblHelp);
        lblRecovery = findViewById(R.id.lblRecovery_signIn);
        imgSignIn_signIn = findViewById(R.id.imgSignIn_signIn);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitle.setTypeface(typeface);

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        //Iniciamos Paper para almacenamiento de variables
        Paper.init(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (cbSignIn.isChecked()) {
                        Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                    }//if

                    final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                    progressDialog.setMessage("Espere un momento...");
                    progressDialog.show();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (validaciones()) {
                                //Verificamos que exista el usiario posteriormente la contrasena
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    progressDialog.dismiss();
                                    UsuarioModel usuarioModel = dataSnapshot.child(edtPhone.getText().toString()).getValue(UsuarioModel.class);
                                    usuarioModel.setPhone(edtPhone.getText().toString());
                                    if (usuarioModel.getPassword().equals(edtPassword.getText().toString())) {

                                        Intent menuLateral = new Intent(SignIn.this, MenuLateral.class);
                                        Common.currentUsuarioModel = usuarioModel; //Obtenemos el usuario actual
                                        startActivity(menuLateral);
                                        finish();

                                        /*
                                        if (usuarioModel.getSliderStatus().equals("activado")) {
                                            Intent sliderWalkthrough = new Intent(SignIn.this, SliderWalkthrough.class);
                                            Common.currentUsuarioModel = usuarioModel; //Obtenemos el usuario actual
                                            startActivity(sliderWalkthrough);
                                            finish();
                                        } else {
                                            Intent menuLateral = new Intent(SignIn.this, MenuLateral.class);
                                            Common.currentUsuarioModel = usuarioModel; //Obtenemos el usuario actual
                                            startActivity(menuLateral);
                                            finish();
                                        }//else
                                         */

                                    } else {
                                        Toast.makeText(SignIn.this, "¡Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                                    }//else
                                } else {
                                    Toast.makeText(SignIn.this, "¡Usuario incorecto!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }//else
                            } else {
                                progressDialog.dismiss();
                            }//else
                        }//onDataChange

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }//onCancelled
                    });
                } else {
                    Toast.makeText(SignIn.this, "¡Revisa tu Conexion a Internet!", Toast.LENGTH_LONG).show();
                    return;
                }//else
            }//onClick
        });

        lblHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ayudaUsuario = new Intent(SignIn.this,AyudaCliente.class);
                startActivity(ayudaUsuario);
                finish();
            }//onClick
        });
    }//onCreate

    public boolean validaciones() {
        if (edtPhone.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignIn.this, "¡Existen campos vacios!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }//else
    }//validaciones

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lblRecovery_signIn:
                showForgotPassword();
                break;
        }//swtch
    }//onClick

    private void showForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Contraseña");
        builder.setMessage("Inserta tu Codigo de Seguridad");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout, null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_baseline_security_24);

        final EditText edtPhone_signUpCard = forgot_view.findViewById(R.id.edtPhone_signUpCard);
        final EditText edtSecureCode_signUpCard = forgot_view.findViewById(R.id.edtSecureCode_signUpCard);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuarioModel = dataSnapshot.child(edtPhone_signUpCard.getText().toString())
                                .getValue(UsuarioModel.class);
                        if (usuarioModel.getSecureCode().equals(edtSecureCode_signUpCard.getText().toString()))
                            Toast.makeText(SignIn.this, "Su Contraseña es: " + usuarioModel.getPassword(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignIn.this, "¡Datos incorrectos! ", Toast.LENGTH_LONG).show();
                    }//onDataChange

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }//onCancelled
                });
            }//onClick
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }//onClick
        });
        builder.show();
    }//showForgotPassword

}//SignIn