package com.example.messenger.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UserAuthorization {

    @SerializedName("session_id")
    public String sessionId = "";

    public UserAuthorization(String sessionId) {
        this.sessionId = sessionId;
    }
}
