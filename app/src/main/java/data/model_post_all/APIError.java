package data.model_post_all;

import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("httpStatus")
    private int statusCode;
    @SerializedName("errorMessage")
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
