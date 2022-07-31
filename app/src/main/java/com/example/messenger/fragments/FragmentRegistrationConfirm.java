package com.example.messenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Фрагмент для регистрации пользователя. ВИ0003 Регистрация нового пользователя - Шаг 2. Ввод
 * регистрационного кода с автоматической авторизацией
 */
public class FragmentRegistrationConfirm extends Fragment {

    private static final String ARG_PHONE = "phone";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PASSWORD = "password";

    private String phone, email, password;

    private EditText etPhone;
    private TextView tvInfoTimer, tvInfoClicker;

    //количество секунд до возможности повторного получения регистрационног кода
    private long returnCodeSec = 60;

    public FragmentRegistrationConfirm() {
        // Required empty public constructor
    }

    public static FragmentRegistrationConfirm newInstance(String param_phone, String param_email, String param_password) {
        FragmentRegistrationConfirm fragment = new FragmentRegistrationConfirm();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, param_phone);
        args.putString(ARG_EMAIL, param_email);
        args.putString(ARG_PASSWORD, param_password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //сохранение переданных фрагменту аргументов
        phone = getArguments().getString(ARG_PHONE);
        email = getArguments().getString(ARG_EMAIL);
        password = getArguments().getString(ARG_PASSWORD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_confirm, container, false);

        etPhone = view.findViewById(R.id.etCodeConfirm);
        tvInfoTimer = view.findViewById(R.id.tvInfoTimer);
        tvInfoClicker = view.findViewById(R.id.tvInfoClicker);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String time = String.format("%d:%02d", returnCodeSec / 60, returnCodeSec % 60);
                try {
                    getActivity().runOnUiThread(() -> {
                            tvInfoTimer.setText("Повторить отправку через " + time);
                            tvInfoClicker.setText("");
                    });
                } catch (Exception e) {};
                returnCodeSec--;
                if (returnCodeSec == 0) {
                    timer.cancel();
                    getActivity().runOnUiThread(() -> {
                        tvInfoTimer.setText("");
                        tvInfoClicker.setText("Повторить отправку");
                        tvInfoClicker.setOnClickListener(v -> {
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction()
                                    .replace(R.id.cvRegistration, FragmentLoading.class, null)
                                    .commit();
                            
                            HashMap<String, String> json = new HashMap<>();
                            json.put("phone", phone);
                            json.put("email", email);
                            json.put("password", password);

                            Call<Void> call = RetrofitClient.getInstance().getMyApi().userRegistrationGetCode(json);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        FragmentRegistrationConfirm fragmentRegistrationConfirm =
                                                FragmentRegistrationConfirm.newInstance(phone, email, password);
                                        fm.beginTransaction()
                                                .replace(R.id.cvRegistration, fragmentRegistrationConfirm)
                                                .commit();
                                    } else {
                                        try {
                                            JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                            String error = jsonError.getString("errors");

                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getActivity(), "Пожалуйста, повторите попытку позже", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    });
                }
            }
        }, 0, 1000);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 5) {
                    FragmentManager fm = getParentFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.cvRegistration, FragmentLoading.class, null)
                            .commit();

                    HashMap<String, String> json = new HashMap<>();
                    json.put("phone", phone);
                    json.put("email", email);
                    json.put("password", password);
                    json.put("reg_code", s.toString());

                    Call<Void> call = RetrofitClient.getInstance().getMyApi().userRegistrationConfirm(json);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("MY", "Зареганы!");
                                // TODO: Переход на авторизацию
                            } else {
                                try {
                                    JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                    String error = jsonError.getString("errors");

                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getActivity(), "Пожалуйста, повторите попытку позже", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}