package com.example.mitiendita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Common.Constants;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Service.FetchAddressIntentService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UbicacionAutomatica extends AppCompatActivity {

    //Declaracion de variables
    private TextView lblTitulo_SelecionarUbicacion;
    private MaterialEditText edtReferenciaAutomaticoMaps;
    private Button btnUbicacionAutomatica;
    private TextView lblUbicacionManual;
    private TextView lblUbicacion;
    String ubicacion;
    private String orderTotal = "";
    List<CartModel> orderList;
    private Calendar c;
    private SimpleDateFormat df;
    private String formattedDate;
    private boolean locationStatus = false;
    String orderNumber;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_automatica);

        //Enlazar controladores
        lblTitulo_SelecionarUbicacion = findViewById(R.id.lblTitulo_SelecionarUbicacion01);
        edtReferenciaAutomaticoMaps = findViewById(R.id.edtReferenciaAutomaticoMaps01);
        btnUbicacionAutomatica = findViewById(R.id.btnUbicacionAutomatica01);
        lblUbicacionManual = findViewById(R.id.lblUbicacionManual01);
        lblUbicacion = findViewById(R.id.lblUbicacion01);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitulo_SelecionarUbicacion.setTypeface(typeface);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Request");

        //Recibir parametros
        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            orderNumber = parametros.getString("orderNumber");
        }//if

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }//else

        //Instanciamos nuestra clase AddressResultReceiver
        resultReceiver = new AddressResultReceiver(new Handler());

        btnUbicacionAutomatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap hashMap = new HashMap();
                String ubicacion = Common.currentUsuarioModel.getLocation();
                hashMap.put("address", lblUbicacion.getText().toString());
                databaseReference.child(orderNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UbicacionAutomatica.this, "Datos cargados", Toast.LENGTH_LONG).show();
                    }//onSuccess
                });

                //Regresams a menu lateral
                menuLateral();

                Toast.makeText(UbicacionAutomatica.this, "¡Su orden ha sido realizada exitosamente!", Toast.LENGTH_LONG).show();

                //sendNotificationOrder(order_number);

            }//onClick
        });

    }//onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(UbicacionAutomatica.this, "Permisos denegados", Toast.LENGTH_LONG).show();
            }//else
        }//if
    }//onRequestPermissionsResult

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(UbicacionAutomatica.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UbicacionAutomatica.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }//if

        try {
            LocationServices.getFusedLocationProviderClient(UbicacionAutomatica.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(UbicacionAutomatica.this)
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
            Toast.makeText(UbicacionAutomatica.this, "Null object Reference", Toast.LENGTH_LONG).show();
        }//catch
    }//getCurrentLocation

    private void fetchAddressFromLatLng(Location location) {
        Intent intent = new Intent(UbicacionAutomatica.this, FetchAddressIntentService.class);
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
                lblUbicacion.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(UbicacionAutomatica.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_LONG).show();
            }//else
        }//onReceiveResult
    }//AddressResultReceiver

    private void menuLateral() {
        //Regresamos al menu lateral
        Intent intent = new Intent(UbicacionAutomatica.this, MenuLateral.class);
        startActivity(intent);
        finish();
        Toast.makeText(UbicacionAutomatica.this, "¡Orden realizada con exito!", Toast.LENGTH_SHORT).show();
    }

}//UbicacionAutomatica