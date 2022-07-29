package com.example.messenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messenger.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegistrationConfirm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistrationConfirm extends Fragment {

    private static final String ARG_PHONE = "phone";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PASSWORD = "password";

    private String phone;
    private String email;
    private String password;

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

        phone = getArguments().getString(ARG_PHONE);
        email = getArguments().getString(ARG_EMAIL);
        password = getArguments().getString(ARG_PASSWORD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_confirm, container, false);
    }
}