package com.example.messenger.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegistrationGetCode extends Fragment {

    public FragmentRegistrationGetCode() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_get_code, container, false);

        EditText etPhone = view.findViewById(R.id.etPhone),
                etEmail = view.findViewById(R.id.etEmail),
                etPassword = view.findViewById(R.id.etPassword),
                etConfirm = view.findViewById(R.id.etConfirm);
        CardView btRegistration = view.findViewById(R.id.btRegistration);
        TextView btSignIn = view.findViewById(R.id.btSignIn);

        btRegistration.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.fragment_container_view, FragmentLoading.class, null)
                    .commit();

            HashMap<String, String> json = new HashMap<>();
            json.put("phone", etPhone.getText().toString());
            json.put("email", etEmail.getText().toString());
            json.put("password", etPassword.getText().toString());

            Call<Void> call = RetrofitClient.getInstance().getMyApi().userRegistrationGetCode(json);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        FragmentRegistrationConfirm fragmentRegistrationConfirm =
                                FragmentRegistrationConfirm.newInstance(
                                        etPhone.getText().toString(),
                                        etEmail.getText().toString(),
                                        etPassword.getText().toString()
                                );
                        fm.beginTransaction()
                                .replace(R.id.fragment_container_view, fragmentRegistrationConfirm)
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

        return view;
    }
}