package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {

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
                                        }


            }
        });

    }
}