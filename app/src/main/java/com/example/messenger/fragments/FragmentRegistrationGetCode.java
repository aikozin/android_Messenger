package com.example.messenger.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.api.RetrofitClient;
import com.example.messenger.utils.DataValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Фрагмент для регистрации пользователя. ВИ0003 Регистрация нового пользователя - Шаг 1. Запрос
 * регистрационного кода с переходом на FragmentRegistrationConfirm
 */
public class FragmentRegistrationGetCode extends Fragment {

    private EditText etPhoneCode, etPhoneNumber, etEmail, etPassword, etConfirm;
    private CardView btRegistration;
    private TextView btSignIn, tvInfo;

    private String phone,  email, password;

    private DataValidation dataValidation;
    private String codeCountry;

    public FragmentRegistrationGetCode() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataValidation = new DataValidation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_get_code, container, false);

        etPhoneCode = view.findViewById(R.id.etPhoneCode);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirm = view.findViewById(R.id.etConfirm);
        btRegistration = view.findViewById(R.id.btRegistration);
        btSignIn = view.findViewById(R.id.btSignIn);
        tvInfo = view.findViewById(R.id.tvInfo);

        etPhoneCode.addTextChangedListener(textPhoneCodeWatcher);
        EditText[] editTexts = {etPhoneCode, etPhoneNumber, etEmail, etPassword, etConfirm};
        for (int i = 0; i < editTexts.length; i++)
            editTexts[i].setOnFocusChangeListener(hideErrorMessage);

        btRegistration.setOnClickListener(v -> {
            phone = "+" + etPhoneCode.getText().toString() + etPhoneNumber.getText().toString();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            String phoneValidationMessage = dataValidation.phoneValidation(phone, codeCountry);
            if (phoneValidationMessage != null) {
                tvInfo.setText(phoneValidationMessage);
                return;
            }
            String emailValidationMessage = dataValidation.emailValidation(email);
            if (emailValidationMessage != null) {
                tvInfo.setText(emailValidationMessage);
                return;
            }
            String passwordValidationMessage = dataValidation.passwordValidation(password);
            if (passwordValidationMessage != null) {
                tvInfo.setText(passwordValidationMessage);
                return;
            }
            if (!password.equals(etConfirm.getText().toString())) {
                tvInfo.setText("Указанные пароли не совпадают");
                return;
            }

            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.cvRegistration, FragmentLoading.class, null)
                    .commit();

            HashMap<String, String> json = new HashMap<>();
            json.put("phone", dataValidation.phoneGetNumberFormat(phone, codeCountry));
            json.put("email", email);
            json.put("password", password);

            Call<Void> call = RetrofitClient.getInstance().getMyApi().userRegistrationGetCode(json);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        FragmentRegistrationConfirm fragmentRegistrationConfirm =
                                FragmentRegistrationConfirm.newInstance(
                                        dataValidation.phoneGetNumberFormat(phone, codeCountry),
                                        email,
                                        password
                                );
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

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.cvLogin, FragmentLogin.class, null)
                        .commit();
            }
        });

        return view;
    }

    TextWatcher textPhoneCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            codeCountry = dataValidation.phoneGetRegionByCode(Integer.parseInt(s.toString()));
            etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher(codeCountry));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnFocusChangeListener hideErrorMessage = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            tvInfo.setText("При нажатии на кнопку на указанный e-mail\nбудет отправлен проверочный код");
        }
    };
}