package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.messenger.fragments.FragmentRegistrationGetCode;

/**
 * Основной активити для регистрации пользователя
 * При запуске выводит фрагмент FragmentRegistrationGetCode.
 */
public class ActivityRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        /*
        вывод фрагмента на экран
         */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cvRegistration, FragmentRegistrationGetCode.class, null)
                .commit();
    }
}