package com.example.mitiendita.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mitiendita.Model.SolicitudModel;
import com.example.mitiendita.Model.UsuarioModel;
import com.example.mitiendita.Remote.FCMRetrofitClient;
import com.example.mitiendita.Remote.IAPIService;

public class Common {

    public static UsuarioModel currentUsuarioModel;
    public static SolicitudModel currentSolicitudModel;
    private static final String BASE_URL = "https://fcm.googleapis.com/";
    /*
    public static IAPIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(IAPIService.class);
    }//getFCMService
     */
    public static String topicNews = "News";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final String fcmUrl = "https://fcm.googleapis.com/";

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Enviada";
        else if (status.equals("1"))
            return "En camino";
        else
            return "Finalizada";
    }//convertCodeToStatus

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }//if
                }//for
            }//if
        }//if
        return false;
    }//isConnectedToInternet

    public static IAPIService getFCMClient() {
        return FCMRetrofitClient.getClient(fcmUrl).create(IAPIService.class);
    }//IGeoCoordenates

}//Common
