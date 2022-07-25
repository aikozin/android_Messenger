package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.messenger.api.RetrofitClient;
import com.example.messenger.model.UserAuthorization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView button_authorization = findViewById(R.id.button_registration);
        button_authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText number = findViewById(R.id.number);
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);

                String stringPhone = number.getText().toString();
                String stringEmail = email.getText().toString();
                String stringPassword = password.getText().toString();

                authorizate(stringPhone, stringEmail, stringPassword);
            }
        });
    }

    private void authorizate(String stringPhone, String stringEmail, String stringPassword) {
        HashMap<String, String> json = new HashMap<>();
        json.put("phone", stringPhone);
        json.put("email", stringEmail);
        json.put("password", stringPassword);

        Call<UserAuthorization> call = RetrofitClient.getInstance().getMyApi().userAuthorization(json);
        call.enqueue(new Callback<UserAuthorization>() {
            @Override
            public void onResponse(Call<UserAuthorization> call, Response<UserAuthorization> response) {
                if (response.isSuccessful()) {
                    sessionId = response.body().sessionId;
                    Toast.makeText(ActivityLogin.this, sessionId, Toast.LENGTH_SHORT).show();
                    getTasks();
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String error = jsonError.getString("errors");
                        Toast.makeText(ActivityLogin.this, error, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAuthorization> call, Throwable t) {
                Toast.makeText(ActivityLogin.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTasks() {

        Toast toast = Toast.makeText(getApplicationContext(),
                "Успех!", Toast.LENGTH_SHORT);
        toast.show();

        //Здесь писать задачи, при успешном входе

    }
}