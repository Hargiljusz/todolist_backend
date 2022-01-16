package com.iwaniuk.todolist_auth.models;

public class RefreshResponse {
    private String jwt;
    private String userId;

    public RefreshResponse() {
    }

    public RefreshResponse(String jwt, String userId) {
        this.jwt = jwt;
        this.userId = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
