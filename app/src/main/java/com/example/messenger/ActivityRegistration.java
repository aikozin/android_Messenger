package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.messenger.fragments.FragmentRegistrationGetCode;

public class ActivityRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, FragmentRegistrationGetCode.class, null)
                .commit();
    }
}