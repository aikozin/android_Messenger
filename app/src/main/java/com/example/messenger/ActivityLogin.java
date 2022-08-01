package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.api.RetrofitClient;
import com.example.messenger.fragments.FragmentLogin;
import com.example.messenger.fragments.FragmentRegistrationConfirm;
import com.example.messenger.fragments.FragmentRegistrationGetCode;
import com.example.messenger.model.UserAuthorization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Основной активити для авторизации пользователя
 * При запуске выводит фрагмент FragmentLogin.
 */
public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        Вывод фрагмента на экран
         */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cvLogin, FragmentLogin.class, null)
                .commit();
    }
}