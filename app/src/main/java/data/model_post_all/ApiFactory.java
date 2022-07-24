package data.model_post_all;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String ROOT_URL = "http://80.254.124.90:5000";

    static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getService() {
        return buildRetrofit().create(ApiService.class);
    }
}
