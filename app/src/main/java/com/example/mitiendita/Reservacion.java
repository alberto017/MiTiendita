package com.example.mitiendita;

import androidx.appcompat.app.AppCompatActivity;
import info.hoang8f.widget.FButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.example.mitiendita.Database.Database;
import com.example.mitiendita.Model.CartModel;
import com.example.mitiendita.Model.SolicitudModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Reservacion extends AppCompatActivity implements View.OnClickListener {

    //Declaracion de variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<CartModel> orderModelList;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    String formattedDate;
    String orderNumber;

    //Widgets
    private TextView lblTitleReservation;
    private EditText edtDate_Reservation;
    private ImageButton btnDate_Reservation;
    private EditText edtHour01_Reservation;
    private EditText edtHour02_Reservation;
    private ImageButton btnHour_Reservation;
    private TextView lblMensajeReservacion;
    private FButton btnReservation;
    boolean validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservacion);

        lblTitleReservation = findViewById(R.id.lblTitleReservation);
        edtDate_Reservation = findViewById(R.id.edtDate_Reservation);
        btnDate_Reservation = findViewById(R.id.btnDate_Reservation);
        edtHour01_Reservation = findViewById(R.id.edtHour01_Reservation);
        edtHour02_Reservation = findViewById(R.id.edtHour02_Reservation);
        btnHour_Reservation = findViewById(R.id.btnHour_Reservation);
        lblMensajeReservacion = findViewById(R.id.lblMensajeReservacion);
        btnReservation = findViewById(R.id.btnReservation);

        btnDate_Reservation.setOnClickListener(this);
        btnHour_Reservation.setOnClickListener(this);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        lblTitleReservation.setTypeface(typeface);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Request");

        //Obtener fecha y hora
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formattedDate = df.format(c.getTime());
        //lblMensajeReservacion.setText(formattedDate);

        //Recibir parametros
        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            orderNumber = parametros.getString("orderNumber");
        }//if

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ejecutamos el metodo de validacion
                validaciones();
                if (validation) {
                    showDialog();
                } else {
                    Toast.makeText(Reservacion.this, "¡Existen campos vacios!", Toast.LENGTH_SHORT).show();
                }//else
            }//onClick
        });
    }//onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHour_Reservation:
                obtenerHora();
                break;
            case R.id.btnDate_Reservation:
                obtenerFecha();
                break;
        }//switch
    }//onClick

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }//else
                //Muestro la hora con el formato deseado
                edtHour01_Reservation.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                edtHour02_Reservation.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }//onTimeSet
        }, hora, minuto, false);
        recogerHora.show();
    }//obtenerHora


    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                edtDate_Reservation.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }//onDateSet
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }//obtenerFecha


    public void validaciones() {
        if ((edtDate_Reservation.getText().toString().isEmpty() || edtHour01_Reservation.getText().toString().isEmpty()) || (edtDate_Reservation.getText().toString().isEmpty() && edtHour01_Reservation.getText().toString().isEmpty())) {
            validation = false;
        } else {
            validation = true;
        }//else
    }//validaciones


    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Reservacion.this);
        alertDialog.setTitle("Domicilio de Orden");
        alertDialog.setMessage(Common.currentUsuarioModel.getLocation());

        alertDialog.setIcon(R.drawable.ic_baseline_location_on_24);

        alertDialog.setPositiveButton("NUEVA UBICACION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                HashMap hashMap = new HashMap();
                String fecha = formattedDate;
                hashMap.put("date", fecha);
                databaseReference.child(orderNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(Reservacion.this, "Datos cargados", Toast.LENGTH_LONG).show();
                    }//onSuccess
                });

                ubicacionAutomatica();
                dialogInterface.dismiss();
            }//onClick
        });

        alertDialog.setNegativeButton("CONSERVAR UBICACION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                HashMap hashMap = new HashMap();
                String ubicacion = Common.currentUsuarioModel.getLocation();
                String fecha = formattedDate;
                hashMap.put("address", ubicacion);
                hashMap.put("date", fecha);
                databaseReference.child(orderNumber).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(Reservacion.this, "Datos cargados", Toast.LENGTH_LONG).show();
                    }//onSuccess
                });

                //Regresams a menu lateral
                menuLateral();

                Toast.makeText(Reservacion.this, "¡Su orden ha sido realizada exitosamente!", Toast.LENGTH_LONG).show();

                //sendNotificationOrder(order_number);

                dialogInterface.dismiss();
            }//onClick
        });
        alertDialog.show();
    }//showDialog

    private void menuLateral() {
        Intent menuLateral = new Intent(Reservacion.this, MenuLateral.class);
        startActivity(menuLateral);
        finish();
    }//menuLateral

    public void ubicacionAutomatica() {
        Intent ubicacionAutomatica = new Intent(Reservacion.this, UbicacionAutomatica.class);
        ubicacionAutomatica.putExtra("orderNumber", orderNumber);
        startActivity(ubicacionAutomatica);
        finish();
    }//ubicacionAutomatica

}//Prueba