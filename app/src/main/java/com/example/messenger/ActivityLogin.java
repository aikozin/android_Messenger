package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import data.model_post_all.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import data.model_post_all.PostUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private static String token;
    private static boolean tokenAcquired;
    private String stringNumber;
    private String stringEmail;
    private String stringPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView button_registration = findViewById(R.id.button_registration);
        button_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText number = findViewById(R.id.number);
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);

                if (number.getText().toString() == "") {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Пропущено заполнение ключивого поля: телефон", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                    if (number.getText().toString().substring(0, 2) != "+7"){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Неверно заполнено ключивое поле: телефон(Код страны)", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                        if (number.getText().toString().length() + 1 != 12){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Неверно заполнено ключивое поле: телефон(длина)", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                            if(email.getText().toString() == ""){
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Пропущено заполнение ключивого поля: электронная почта", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else
                                if (email.getText().toString().indexOf('@') == 0 || email.getText().toString().indexOf('@') == -1){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Неверно заполнено ключивое поле: электронная почта(-@mail.ru, -@gmail.com)", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else
                                    if (email.getText().toString().indexOf('.') == 0 || email.getText().toString().indexOf('.') == -1){
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Неверно заполнено ключивое поле: электронная почта(-@mail.ru, -@gmail.com)", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else
                                        if (password.getText().toString() == ""){
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "Пропущено заполнение ключивого поля: пароль", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        else
                                        {
                                            String stringNumber = number.getText().toString();
                                            String stringEmail = email.getText().toString();
                                            String stringPassword = password.getText().toString();

                                            getToken();
                                        }
            }
        });
    }

    private void getToken() {
        Call<PostUser> call = ApiFactory.getService().getToken(stringEmail, stringPassword);
        call.enqueue(new Callback<PostUser>() {
            @Override
            public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                if (response.isSuccessful()) {
                    ActivityLogin.token = response.body().getData().getToken();
                    ActivityLogin.tokenAcquired = true;
                    Log.d("TOKEN", token);
                    getTasks();
                } else {
                    ActivityLogin.token = "";
                    ActivityLogin.tokenAcquired = false;
                    Log.d("TOKEN", ErrorUtils.errorMessage(response));
                }
            }

            @Override
            public void onFailure(Call<PostUser> call, Throwable t) {}
        });
    }

    private void getTasks() {

        Toast toast = Toast.makeText(getApplicationContext(),
                "Успех!", Toast.LENGTH_SHORT);
        toast.show();

        //Здесь писать задачи, при успешном входе

    }
}