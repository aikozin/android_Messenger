package com.example.messenger.api;

import com.example.messenger.model.UserAuthorization;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://80.254.124.90:5000/";

    /**
     * Регистрация пользователя. Шаг 1 - запрос регистрационного кода
     */
    @Headers({"Content-Type:application/json"})
    @POST("users/reg/getcode/")
    Call<Void> userRegistrationGetCode(
            @Body HashMap<String, String> json
    );

    /**
     * Регистрация пользователя. Шаг 2 - подтверждение с кодом регистрации
     */
    @Headers({"Content-Type:application/json"})
    @POST("users/reg/confirm/")
    Call<Void> userRegistrationConfirm(
            @Body HashMap<String, String> json
    );

    /**
     * Авторизация пользователя
     */
    @Headers({"Content-Type:application/json"})
    @POST("users/auth/")
    Call<UserAuthorization> userAuthorization(
            @Body HashMap<String, String> json
    );
}
