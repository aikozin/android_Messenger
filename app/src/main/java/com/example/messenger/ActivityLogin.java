package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
                EditText number_or_email = findViewById(R.id.number_or_email);
                EditText password = findViewById(R.id.password);

                TextView error_number_or_email = findViewById(R.id.error_number_or_email);
                TextView error_password = findViewById(R.id.error_password);

                error_number_or_email.setText("");
                error_password.setText("");

                String stringPhone = "";
                String stringEmail = "";
                String stringPassword = "";

                /*
                if (number_or_email.getText().toString().equals("") == true ){
                    error_number_or_email.setText("Данное поле обязательно для заполнения"); // не заполнен телефон или почта
                } else if (number_or_email.getText().toString().substring(0,2).equals("+7") == true) {
                    stringPhone = number_or_email.getText().toString();
                } else if (number_or_email.getText().toString().indexOf('@') != 0 || number_or_email.getText().toString().indexOf('@') != -1 || number_or_email.getText().toString().indexOf('.') != 0 || number_or_email.getText().toString().indexOf('.') != -1 ||  number_or_email.getText().toString().indexOf('.') != number_or_email.getText().toString().length()){
                    error_number_or_email.setText("Введите верный номер телефона или email"); // неверно заполнен телефон или почта
                } else {
                    stringEmail = number_or_email.getText().toString();
                }

                if (password.getText().toString().equals("")) {
                    error_password.setText("Введите пароль"); // не заполнен пароль
                } else {
                    char[] mas_incoming = password.getText().toString().toCharArray();
                    String variables = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
                    char[] mas_outgoing = variables.toCharArray();
                    boolean logic;
                    int kol = 0;
                    for (int j = 0; j < 94; j++) {
                        for (int i = 0; i < password.getText().toString().length(); i++) {
                            logic = false;
                            if (mas_incoming[i] == mas_outgoing[j]){
                                logic = true;
                            }
                            if (logic == true){
                                kol++;
                            }
                        }
                    }
                    if (kol != mas_incoming.length + 1)
                        error_password.setText("Введите пароль, длиной более 7 символов, состоящий из букв (A-z), цифр (0-9) и спец. символов"); // неверно заполнен пароль
                    else
                        stringPassword = password.getText().toString();
                }
                */

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