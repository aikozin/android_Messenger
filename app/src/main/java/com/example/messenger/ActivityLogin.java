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

                String experimental_phone_or_email = number_or_email.getText().toString();
                String experimental_password = password.getText().toString();

                if (experimental_phone_or_email.equals("")) {
                    error_number_or_email.setText("Данное поле обязательно для заполнения");
                } else {
                    char[] mas_char_1 = experimental_phone_or_email.toCharArray();
                    if (mas_char_1[0] == '+') {
                        char[] mas_Char_2 = {'+', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
                        char[] mas_char_symbol = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
                        for (int i = 1; i <= 12; i++) {
                            for (int j = 0; j <= 9; j++) {
                                if (mas_char_1[i] == mas_char_symbol[j]) {
                                    mas_Char_2[i] = mas_char_symbol[j];
                                }
                            }
                            if (mas_Char_2[i] == ' ') {
                                error_number_or_email.setText("Введите верный номер телефона или email");
                            }
                        }
                        stringPhone = mas_Char_2.toString();
                    } else {
                        char[] mas_char_symbol = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray();
                        char[] mas_Char_2 = new char[experimental_phone_or_email.length()];
                        for (int i = 0; i <= experimental_phone_or_email.length(); i++) {
                            for (int j = 0; j <= "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".length(); j++) {
                                if (mas_char_1[i] == mas_char_symbol[j]) {
                                    mas_Char_2[i] = mas_char_symbol[j];
                                } else {
                                    mas_Char_2[i] = ' ';
                                }
                            }
                            if (mas_Char_2[i] == ' ') {
                                error_number_or_email.setText("Введите верный номер телефона или email");
                            }
                        }
                        stringEmail = mas_Char_2.toString();
                    }
                }
                if (experimental_password.equals("")) {
                    error_password.setText("Введите пароль");
                } else {
                    char[] mas_char_symbol = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray();
                    char[] mas_Char_2 = new char[experimental_password.length()];
                    char[] mas_char_1 = experimental_password.toCharArray();
                    for (int i = 0; i <= experimental_password.length(); i++) {
                        for (int j = 0; j <= "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".length(); j++) {
                            if (mas_char_1[i] == mas_char_symbol[j]) {
                                mas_Char_2[i] = mas_char_symbol[j];
                            } else {
                                mas_Char_2[i] = ' ';
                            }
                        }
                        if (mas_Char_2[i] == ' ') {
                            error_password.setText("Введите пароль, длиной более 7 символов, состоящий из букв (A-z), цифр (0-9) и спец. символов");
                        }
                    }
                    stringPassword = mas_Char_2.toString();
                }
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
        });
    }
}