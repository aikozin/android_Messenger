package com.example.messenger.utils;

import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Класс для валидации данных, преобразования их в другой формат
 * да и просто для часто используемых кусочков кода
 */
public class DataValidation {

    private HashMap<Integer, String> codes;

    /**
     * Конструктор класса, подготавливающий его для дальнейшей работы
     */
    public DataValidation() {
        /*
        Вытягиваем в глобальную переменную codes коды телефонов
        для всех стран и их название в двубуквенном формате. Например, 7 : RU
         */
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Set<String> regions = phoneNumberUtil.getSupportedRegions();
        codes = new HashMap<>();
        for (String region : regions) {
            codes.put(phoneNumberUtil.getCountryCodeForRegion(region), region);
        }
    }

    /**
     * Метод для получения двубуквенного обозначения страны по ее коду
     * @param code - телефонный код страны. Например, 7 (+7)
     * @return - двубуквенное обозначение страны. Например, "RU"
     */
    public String phoneGetRegionByCode(int code) {
        return codes.getOrDefault(code, null);
    }

    /**
     * Валидация номера телефона
     * @param phone - номер телефона из поля для ввода. Может содержать любые символы,
     *              например, знаки тире между наборами цифр. Например, +7 951-123 45 78
     * @param code - двубуквенное обозначение страны. Например, "RU"
     * @return - ошибка "Заполните все обязательные поля" если номер пустой,
     *          - ошибка "Введите верный номер телефона" если номер невозможно спарсить библиотекой,
     *          либо если номер короче 8 символов или длиннее 20
     */
    public String phoneValidation(String phone, String code) {
        String error = "Введите верный номер телефона\n";

        if (phone.equals("+"))
            return "Заполните все обязательные поля\n";

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = null;
        try {
            numberProto = phoneNumberUtil.parse(phone, code);
        } catch (Exception e) {
            return error;
        }

        if (Long.toString(numberProto.getNationalNumber()).length() < 8 ||
                Long.toString(numberProto.getNationalNumber()).length() > 20)
            return error;

        return null;
    }

    /**
     * Метод для получения форматированного номера телефона в формате +71234567890
     * @param phone - номер телефона из поля для ввода. Может содержать любые символы,
     *              например, знаки тире между наборами цифр. Например, +7 951-123 45 78
     * @param code - двубуквенное обозначение страны. Например, "RU"
     * @return - возвращает отформатированный номер телефона
     */
    public String phoneGetNumberFormat(String phone, String code) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = null;
        try {
            numberProto = phoneNumberUtil.parse(phone, code);
            return phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Метод для валидации адреса электронной почты
     * @param email - введенный пользователем адрес email
     * @return - ошибка "Заполните все обязательные поля", если строка пустая
     *          - ошибка "Введите верный адрес почтового ящика", если адрес не содержит
     *          символа "@" или "."
     */
    public String emailValidation(String email) {
        String error = "Введите верный адрес почтового ящика\n";

        if (email.equals(""))
            return "Заполните все обязательные поля\n";

        email = email.trim();
        email = email.toLowerCase(Locale.ROOT);
        if (!email.contains("@") || !email.contains("."))
            return error;

        return null;
    }

    /**
     * Метод для валидации пароля
     * @param password - введенный пользователем пароль
     * @return - ошибка "Заполните все обязательные поля", если строка пустая
     *          - ошибка "Введите пароль длиной более 7 символов, состоящий из букв (A-z),
     *          цифр (0-9) и спец. символов", если пароль не содержит указанных символов
     */
    public String passwordValidation(String password) {
        String error = "Введите пароль длиной более 7 символов, состоящий \nиз букв (A-z), цифр (0-9) и спец. символов";

        if (password.equals(""))
            return "Заполните все обязательные поля\n";

        if (!password.matches("(?=.*[A-z])(?=.*[0-9])(?=.*[`~!@#№$;%^:&?*()_=+'|,<.>/])[0-9A-z`~!@#№$;%^:&?*()_=+'|,<.>/]{8,}"))
            return error;

        return null;
    }
}
