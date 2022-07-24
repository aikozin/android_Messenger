package data.model_post_all;

import data.model_post_all.PostUser;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Headers;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/user/")
    Call<PostUser> getToken(
            @Query("login") String login,
            @Query("password") String password);
}
