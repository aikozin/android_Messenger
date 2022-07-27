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
                String stringPassword;

                String string = number_or_email.getText().toString();
                string = string.trim();
                if (string.length() <= 0) {
                    error_number_or_email.setText("Данное поле обязательно для заполнения");
                    string = "";
                } else {
                    if (string.toCharArray()[0] == '+') { // проблема дальше
                        string = string.replaceAll("^[A-Za-zА-Яа-я!-/:-~]", "");
                        if (string.length() != 11) {
                            error_number_or_email.setText("Введите верный номер телефона или email");
                            string = "";
                        } else {
                            string = string.substring(2, 11);
                            stringPhone = "+7" + string;
                        }
                    } else { // дальше все Ок
                        string = string.replaceAll("^[А-Яа-я!-,/:-?^`{-~]", "");
                        char[] chars_e = new char[string.length()];
                        int char_n = string.indexOf("@");
                        if (char_n <= 0) {
                            error_number_or_email.setText("Введите верный номер телефона или email");
                            string = "";
                        } else {
                            String new_string = string.substring(char_n);
                            char_n = new_string.indexOf(".");
                            if (char_n == -1) {
                                error_number_or_email.setText("Введите верный номер телефона или email");
                                string = "";
                            }
                            stringEmail = string;
                        }
                    }
                }
                string = password.getText().toString();
                string = string.trim();
                if (string.equals("")) {
                    error_password.setText("Введите пароль");
                    string = "";
                } else {
                    if (string.length() <= 7) {
                        error_password.setText("Введите пароль, длиной более 7 символов, состоящий из букв (A-z), цифр (0-9) и спец. символов");
                        string = "";
                    }
                }
                string = string.replaceAll("^[А-Яа-я]", "");
                stringPassword = string;
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
                                //Toast.makeText(ActivityLogin.this, error, Toast.LENGTH_SHORT).show();

                                TextView error_server = findViewById(R.id.error_server);
                                error_server.setText(error);

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