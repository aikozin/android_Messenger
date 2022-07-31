package com.example.messenger.api;

import com.example.messenger.model.UserAuthorization;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://80.254.124.90:5000/";

    @Headers({"Content-Type:application/json"})
    @POST("users/auth/")
    Call<UserAuthorization> userAuthorization(
        @Body HashMap<String, String> json
    );
}
