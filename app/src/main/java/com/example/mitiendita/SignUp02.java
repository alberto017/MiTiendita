package com.example.mitiendita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Common.Constants;
import com.example.mitiendita.Model.UsuarioModel;
import com.example.mitiendita.Service.FetchAddressIntentService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp02 extends AppCompatActivity {

    //Declaracion de variables
    private TextView lblTitulo_Ubicacion02;
    private EditText edtReferencia02;
    private TextView lblUbicacion02;
    private Button btnSignUp02_Register;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private boolean validacion = false;

    LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up02);

        //Enlazar controladores
        lblTitulo_Ubicacion02 = findViewById(R.id.lblTitulo_Ubicacion02);
        edtReferencia02 = findViewById(R.id.edtReferencia02);
        lblUbicacion02 = findViewById(R.id.lblUbicacion02);
        btnSignUp02_Register = findViewById(R.id.btnSignUp02_Register);

        //Obtenemos datos de SignUp01
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();

        final String usuario = extra.getString("usuario");
        final String telefono = extra.getString("telefono");
        final String contrasena = extra.getString("contrasena");
        final String introduccion = extra.getString("introduccion");
        final String status = extra.getString("status");
        final String codigo = extra.getString("codigo");

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitulo_Ubicacion02.setTypeface(typeface);

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        //Verificar permisos
        if (ContextCompat.checkSelfPermission(
                SignUp02.this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    SignUp02.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }//else

        //Instanciamos nuestra clase AddressResultReceiver
        resultReceiver = new AddressResultReceiver(new Handler());

        btnSignUp02_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(SignUp02.this);
                progressDialog.setMessage("Espere un momento...");
                progressDialog.show();

                validaciones();

                if (Common.isConnectedToInternet(getBaseContext())) {

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (validacion) {
                                progressDialog.dismiss();
                                UsuarioModel usuarioModel = new UsuarioModel(usuario, telefono, contrasena, introduccion, codigo, lblUbicacion02.getText().toString(), status, edtReferencia02.getText().toString());
                                databaseReference.child(telefono).setValue(usuarioModel);
                                Toast.makeText(SignUp02.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();

                                Intent signIn = new Intent(SignUp02.this,Inicio.class);
                                startActivity(signIn);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp02.this, "¡Existen campos vacios!", Toast.LENGTH_SHORT).show();
                            }//else
                        }//onDataChange

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }//onCancelled
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp02.this, "¡Revisa tu Conexion a Internet!", Toast.LENGTH_LONG).show();
                    return;
                }//else
            }//onClick
        });

    }//Create

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(SignUp02.this, "Permisos denegados", Toast.LENGTH_LONG).show();
            }//else
        }//if
    }//onRequestPermissionsResult

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(SignUp02.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SignUp02.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }//if

        try {
            LocationServices.getFusedLocationProviderClient(SignUp02.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(SignUp02.this)
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                fetchAddressFromLatLng(location);
                            }//if
                        }//onLocationResult
                    }, Looper.getMainLooper());
        } catch (Exception e) {
            Toast.makeText(SignUp02.this, "Null object Reference", Toast.LENGTH_LONG).show();
        }//catch
    }//getCurrentLocation

    private void fetchAddressFromLatLng(Location location) {
        Intent intent = new Intent(SignUp02.this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }//fetchAddressFromLatLng

    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }//AddressResultReiver

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                lblUbicacion02.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(SignUp02.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_LONG).show();
            }//else
        }//onReceiveResult
    }//AddressResultReceiver

    public void validaciones() {
        if (edtReferencia02.getText().toString().equals("")) {
            validacion = false;
        } else {
            validacion = true;
        }//else
    }//validaciones

}//SignUp02