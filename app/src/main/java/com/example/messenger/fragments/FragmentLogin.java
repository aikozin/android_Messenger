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

import com.example.messenger.ActivityLogin;
import com.example.messenger.R;
import com.example.messenger.api.RetrofitClient;
import com.example.messenger.model.UserAuthorization;
import com.example.messenger.utils.DataValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Фрагмент для авторизации пользователя. ВИ0001 Авторизация пользователя на первом экране
 * приложения при открытии его неавторизованным пользователем
 */
public class FragmentLogin extends Fragment {

    //объявляем все визуальные компоненты с экрана
    private EditText etPhoneOrEmail, etPassword;
    private TextView btForgotPassword, btSignUp, tvErrorMessage;
    private CardView btLogin;

    //переменные для хранения введенных телефона, почты и пароля
    private String phone, email, password;

    //переменная для ИД сессии, который приходит с сервера после авторизации
    private String sessionId;

    //библиотека для валидации данных
    private DataValidation dataValidation;

    public FragmentLogin() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //ищем все визуальные элементы на экране
        etPhoneOrEmail = view.findViewById(R.id.etPhoneOrEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btForgotPassword = view.findViewById(R.id.btForgotPassword);
        btSignUp = view.findViewById(R.id.btSignUp);
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        btLogin = view.findViewById(R.id.btLogin);

        //функция нажатия на кнопку "Войти"
        btLogin.setOnClickListener(v -> {
            //получаем введенные на форму данные
            String phoneOrEmail = etPhoneOrEmail.getText().toString();
            password = etPassword.getText().toString();

            //если поле "Номер телефона или email" содержит цифры и символ "-" - то это номер
            //телефона. Делаем валидацию
            if (phoneOrEmail.matches("(?=.*[0-9])(?=.*[+])[0-9+]{8,}")) {
                //обработка того, что введен номер телефона
                String phoneValidationMessage = dataValidation.phoneValidation(phoneOrEmail, "");
                if (phoneValidationMessage != null) {
                    tvErrorMessage.setText(phoneValidationMessage);
                    return;
                }
                phone = phoneOrEmail;
                email = "";
            } else {
                //обработка того, что введен email
                String emailValidationMessage = dataValidation.emailValidation(phoneOrEmail);
                if (emailValidationMessage != null) {
                    tvErrorMessage.setText(emailValidationMessage);
                    return;
                }
                phone = "";
                email = phoneOrEmail;
            }
            //валидация пароля
            String passwordValidationMessage = dataValidation.passwordValidation(password);
            if (passwordValidationMessage != null) {
                tvErrorMessage.setText(passwordValidationMessage);
                return;
            }

            //вывод фрагмента с анимацией загрузки
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.cvRegistration, FragmentLoading.class, null)
                    .commit();

            HashMap<String, String> json = new HashMap<>();
            json.put("phone", phone);
            json.put("email", email);
            json.put("password", password);

            Call<UserAuthorization> call = RetrofitClient.getInstance().getMyApi().userAuthorization(json);
            call.enqueue(new Callback<UserAuthorization>() {
                @Override
                public void onResponse(Call<UserAuthorization> call, Response<UserAuthorization> response) {
                    if (response.isSuccessful()) {
                        sessionId = response.body().sessionId;
                        Toast.makeText(getActivity(), sessionId, Toast.LENGTH_SHORT).show();
                        // TODO: Пользователь авторизован. Реализовать переход в чаты
                    } else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            String error = jsonError.getString("errors");
                            tvErrorMessage.setText(error);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserAuthorization> call, Throwable t) {
                    Toast.makeText(getActivity(), "Пожалуйста, повторите попытку позже", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}