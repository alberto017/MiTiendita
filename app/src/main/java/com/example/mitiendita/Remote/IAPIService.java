package com.example.mitiendita.Remote;

import com.example.mitiendita.Model.MyResponse;
import com.example.mitiendita.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEkMdQaE:APA91bED42KfhG5Q2kTAxkTwa7DydADPt22BxumsoOr-hxeM00W_5TmmIhwlQ39OODgmRXzzyA-Z8r70OvhoVzs6ClWb56fbcuvcnGn77VWaKYsuzGf9QfM8sP8GyR9ajmWWsPQGLNCX"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}//IAPIService
