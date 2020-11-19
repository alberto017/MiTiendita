package com.example.mitiendita.Model;

public class Token {

    private String token;
    private boolean isServerToken;

    public Token() {
    }//Constructor vacio

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;
    }//Constructor

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsServerToken() {
        return isServerToken;
    }

    public void setIsServerToken(boolean isServerToken) {
        this.isServerToken = isServerToken;
    }
}
